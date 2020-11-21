package com.ite5year.controllers;


import com.ite5year.daos.CarDao;
import com.ite5year.messagingrabbitmq.RabbitMQSender;
import com.ite5year.models.*;
import com.ite5year.repositories.CarRepository;
import com.ite5year.services.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ite5year.utils.GlobalConstants.BASE_URL;

@RestController()
@RequestMapping(BASE_URL + "/cars")
public class CarController {
    @Resource(name="sharedParametersMap")
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
    public Car retrieveCarById(@PathVariable long id) throws Exception {
        Optional<Car> car = carRepository.findById(id);
        if(!car.isPresent()) {
            throw new Exception("Car with id " + id + " is not found!");
        }

        return car.get();
    }


    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable long id) {
        carRepository.deleteById(id);
    }


    @PostMapping("/create")
    public @ResponseBody  Car createNewCar(@RequestBody Car car) throws Exception {
        if(car.getSeatsNumber() <= 0) {
            int seatsNumber = Integer.parseInt(parametersMap.get("seatsNumber").toString());
            car.setSeatsNumber(seatsNumber);
        }
        if(car.getDateOfSale() != null || car.getPayerName() != null) {
            throw new Exception("Cannot provide " + car.getDateOfSale() + " or " + car.getPayerName()  + "  when you're creating the car");
        }

        return carRepository.save(car);
    }


    @PostMapping("/create/with-optimistic-lock")
    public @ResponseBody  Car createNewCarWithOptimisticLock(@RequestBody Car car) throws Exception {
        if(car.getSeatsNumber() <= 0) {
            int seatsNumber = Integer.parseInt(parametersMap.get("seatsNumber").toString());
            car.setSeatsNumber(seatsNumber);
        }
        if(car.getDateOfSale() != null || car.getPayerName() != null) {
            throw new Exception("Cannot provide " + car.getDateOfSale() + " or " + car.getPayerName()  + "  when you're creating the car");
        }
       return carDao.saveCarByJDBC(car);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCar(@RequestBody Car car, @PathVariable long id) {

        Optional<Car> carOptional = carRepository.findById(id);
        if (!carOptional.isPresent())
            return ResponseEntity.notFound().build();

        car.setId(id);
        carRepository.save(car);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/purchase/{id}")
    public ResponseEntity<Object> purchaseCar(@RequestBody PurchaseCarObject purchaseCarObject, @PathVariable long id) {
        return carService.purchaseCar(id, purchaseCarObject);
    }


    @GetMapping("/un-sold")
    public @ResponseBody List<Car> getAllUnSoldCars() {
        return carService.findAllUnSoldCar();
    }

    @GetMapping("/selling-date/{sellingDate}")
    public @ResponseBody List<Car> getAllSoldCarInMonth(@PathVariable String sellingDate) {
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
