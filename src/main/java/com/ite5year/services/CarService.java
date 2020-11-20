package com.ite5year.services;

import com.ite5year.models.Car;
import com.ite5year.models.GenericSpecification;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CarService {
    List<Car> findAllUnSoldCar();
    List<Car> findAllSoldCardByDate(LocalDateTime date);

}
