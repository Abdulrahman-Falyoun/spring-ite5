package com.ite5year.controllers;


import com.ite5year.daos.CarDao;
import com.ite5year.messagingrabbitmq.RabbitMQSender;
import com.ite5year.models.*;
import com.ite5year.payload.exceptions.ResourceNotFoundException;
import com.ite5year.repositories.CarRepository;
import com.ite5year.services.CarServiceImpl;
import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ite5year.utils.GlobalConstants.BASE_URL;

@RestController()
@RequestMapping(BASE_URL + "/cars")
public class CarController {
    @Resource(name = "sharedParametersMap")
    private Map<String, Object> parametersMap;

    private CarRepository carRepository;
    private CarServiceImpl carService;
    RabbitMQSender rabbitMQSender;

    @Autowired
    private CarDao carDao;

    @Autowired
    public void setRabbitMQSender(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    public Map<String, Object> getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(Map<String, Object> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public CarServiceImpl getCarService() {
        return carService;
    }

    @Autowired
    public void setCarService(CarServiceImpl carService) {
        this.carService = carService;
    }

    public CarRepository getCarRepository() {
        return carRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public List<Car> retrieveAllCars() {
        return carRepository.findAll();
    }


    @GetMapping("/{id}")
    @Cacheable(value = "cars", key = "#id")
    public ResponseEntity<Car> retrieveCarById(@PathVariable long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car with id " + id + " is not found!"));
        return ResponseEntity.ok().body(car);

    }


    @DeleteMapping("/{id}")
    @CacheEvict(value = "cars", allEntries = true)
    public Map<String, Boolean> deleteCar(@PathVariable long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found for this id :: " + id));
        carRepository.delete(car);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @PostMapping("/create")
    public @ResponseBody
    Car createNewCar(@RequestBody Car car) throws Exception {
        System.out.println(car);
        if (car.getSeatsNumber() <= 0) {
            int seatsNumber = Integer.parseInt(parametersMap.get("seatsNumber").toString());
            car.setSeatsNumber(seatsNumber);
        }
        if (car.getDateOfSale() != null || car.getPayerName() != null) {
            throw new Exception("Cannot provide " + car.getDateOfSale() + " or " + car.getPayerName() + "  when you're creating the car");
        }

        return carRepository.save(car);
    }


    @PostMapping("/create/with-optimistic-lock")
    public @ResponseBody
    Car createNewCarWithOptimisticLock(@RequestBody Car car) throws Exception {
        if (car.getSeatsNumber() <= 0) {
            int seatsNumber = Integer.parseInt(parametersMap.get("seatsNumber").toString());
            car.setSeatsNumber(seatsNumber);
        }
        if (car.getDateOfSale() != null || car.getPayerName() != null) {
            throw new Exception("Cannot provide " + car.getDateOfSale() + " or " + car.getPayerName() + "  when you're creating the car");
        }
        return carDao.saveCarByJDBC(car);
    }

    @Transactional
    @PutMapping("/{id}")
    @CachePut(value = "cars", key = "#updatedCar.id")
    public ResponseEntity<Object> updateCar(@RequestBody Car updatedCar) throws ResourceNotFoundException {

        Car car = carRepository.findById(updatedCar.getId()).orElseThrow(() -> new ResourceNotFoundException("Car with id " + updatedCar.getId() + " is not found!"));
        car.setSeatsNumber(updatedCar.getSeatsNumber());
        car.setPayerName(updatedCar.getPayerName());
        car.setDateOfSale(updatedCar.getDateOfSale());
        car.setPriceOfSale(updatedCar.getPriceOfSale());
        car.setName(updatedCar.getName());

        final Car responseCar = carRepository.save(car);
        return ResponseEntity.ok(responseCar);
    }


    @Transactional
    @PutMapping("/purchase/{id}")
    public ResponseEntity<Object> purchaseCar(@RequestBody PurchaseCarObject purchaseCarObject, @PathVariable long id) {
        return carService.purchaseCar(id, purchaseCarObject);
    }


    @GetMapping("/un-sold")
    public @ResponseBody
    List<Car> getAllUnSoldCars() {
        return carService.findAllUnSoldCar();
    }

    @GetMapping("/selling-date/{sellingDate}")
    public @ResponseBody
    List<Car> getAllSoldCarInMonth(@PathVariable String sellingDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime dt = LocalDateTime.parse(sellingDate, formatter);
        try {
            return carService.findAllSoldCardByDate(dt);
        } catch (Exception e) {
            System.out.println("Exc: " + e);
            return null;
        }
    }


    @PostMapping("/report")
    public RabbitMessage generateReportForCars(@RequestBody RabbitMessage rabbitMessage) {
        return rabbitMQSender.send(rabbitMessage);
    }
}
