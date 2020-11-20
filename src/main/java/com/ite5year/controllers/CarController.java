package com.ite5year.controllers;


import com.ite5year.enums.SearchOperation;
import com.ite5year.models.Car;
import com.ite5year.models.GenericSpecification;
import com.ite5year.models.SearchCriteria;
import com.ite5year.repositories.CarRepository;
import com.ite5year.services.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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


    @PostMapping
    public @ResponseBody  Car createNewCar(@RequestBody Car car) {
        if(car.getSeatsNumber() <= 0) {
            int seatsNumber = Integer.parseInt(parametersMap.get("seatsNumber").toString());
            car.setSeatsNumber(seatsNumber);
        }
        return carRepository.save(car);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Car student, @PathVariable long id) {

        Optional<Car> studentOptional = carRepository.findById(id);

        if (!studentOptional.isPresent())
            return ResponseEntity.notFound().build();

        student.setId(id);
        carRepository.save(student);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/un-sold")
    public @ResponseBody List<Car> getAllUnSoldCars() {
        return carService.findAllUnSoldCar();
    }

    @GetMapping("/selling-date/{sellingDate}")
    public @ResponseBody List<Car> getAllSoldCarInMonth(@PathVariable String sellingDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Parsing or conversion
        final LocalDateTime dt = LocalDateTime.parse(sellingDate, formatter);


        try {
            return carService.findAllSoldCardByDate(dt);
        } catch (Exception e) {
            System.out.println("Exc: " + e);
            return null;
        }

    }
}
