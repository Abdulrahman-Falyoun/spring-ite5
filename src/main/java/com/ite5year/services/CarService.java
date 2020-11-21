package com.ite5year.services;

import com.ite5year.models.Car;
import com.ite5year.models.GenericSpecification;
import com.ite5year.models.PurchaseCarObject;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CarService {
    List<Car> findAllUnSoldCar();
    List<Car> findAllSoldCardByDate(LocalDateTime date);
    ResponseEntity<Object> purchaseCar(long carId, PurchaseCarObject purchaseCarObject);

}
