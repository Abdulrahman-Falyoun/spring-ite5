package com.ite5year.services;

import com.ite5year.enums.SearchOperation;
import com.ite5year.models.Car;
import com.ite5year.models.GenericSpecification;
import com.ite5year.models.SearchCriteria;
import com.ite5year.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarServiceImpl() {
    }



    @Override
    public List<Car> findAllUnSoldCar() {
        GenericSpecification<Car> genericSpecification = new GenericSpecification<Car>();
        genericSpecification.add(new SearchCriteria("priceOfSale", 0, SearchOperation.GREATER_THAN));
        return carRepository.findAll(genericSpecification);
    }

    @Override
    public List<Car> findAllSoldCardByDate(LocalDateTime date) {
        List<Car> cars = carRepository.findAll();

        List<Car> res = new ArrayList<>();
        for(Car car: cars) {
            Date carDate = car.getDateOfSale();
            LocalDateTime ldtCar = LocalDateTime.from(carDate.toInstant());
            if(ldtCar.getYear() == date.getYear()) {
                if(ldtCar.getMonthValue() <= date.getMonthValue() && ldtCar.getMonthValue() > (date.getMonthValue() - 1)) {
                    res.add(car);
                }
            }
        }
        return res;
    }

    public CarRepository getCarRepository() {
        return carRepository;
    }

    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
}
