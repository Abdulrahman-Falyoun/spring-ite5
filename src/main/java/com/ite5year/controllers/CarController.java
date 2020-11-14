package com.ite5year.controllers;


import com.ite5year.models.Car;
import com.ite5year.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CarController {
    @Autowired private CarRepository carRepository;


    @GetMapping("/cars")
    public List<Car> retrieveAllCars() {
        return carRepository.findAll();
    }


    @GetMapping("/cars/{id}")
    public Car retrieveCarById(@PathVariable long id) throws Exception {
        Optional<Car> car = carRepository.findById(id);
        if(!car.isPresent()) {
            throw new Exception("Student with id " + id + " is not found!");
        }

        return car.get();
    }


    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable long id) {
        carRepository.deleteById(id);
    }


    @PostMapping("/car")
    public ResponseEntity<Object> createNewCar(@RequestBody Car car) {
        Car savedCar = carRepository.save(car);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedCar.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


    @PutMapping("/students/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Car student, @PathVariable long id) {

        Optional<Car> studentOptional = carRepository.findById(id);

        if (!studentOptional.isPresent())
            return ResponseEntity.notFound().build();

        student.setId(id);
        carRepository.save(student);

        return ResponseEntity.noContent().build();
    }
}
