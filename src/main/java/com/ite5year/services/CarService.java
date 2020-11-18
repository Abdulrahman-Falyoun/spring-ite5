package com.ite5year.services;

import com.ite5year.models.Car;
import com.ite5year.models.GenericSpecification;

import java.util.List;

public interface CarService {
    List<Car> findAllUnSoldCar();

}
