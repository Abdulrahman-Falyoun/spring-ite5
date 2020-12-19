package com.ite5year.services;

import com.ite5year.enums.SearchOperation;
import com.ite5year.models.*;
import com.ite5year.optimisticlock.OptimisticallyLocked;
import com.ite5year.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service(value = "CarServiceImpl")
@Transactional
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    private SharedParametersServiceImpl sharedParametersService;
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarServiceImpl() {
    }

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Autowired
    public void setSharedParametersService(SharedParametersServiceImpl sharedParametersService) {
        this.sharedParametersService = sharedParametersService;
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
            if(carDate != null) {
                Instant instant = carDate.toInstant();
                LocalDateTime ldtCar = LocalDateTime.from(instant.atZone(ZoneId.of("UTC")));
                if(ldtCar.getYear() == date.getYear()) {
                    if(ldtCar.getMonthValue() <= date.getMonthValue() && ldtCar.getMonthValue() > (date.getMonthValue() - 1)) {
                        res.add(car);
                    }
                }
            }
        }
        return res;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> purchaseCar(long carId, PurchaseCarObject purchaseCarObject) {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (!carOptional.isPresent())
            return ResponseEntity.notFound().build();


        System.out.println("Completed...");
        Car car = carOptional.get();
        car.setPayerName(purchaseCarObject.getPayerName());
        car.setDateOfSale(purchaseCarObject.getDateOfSale());
        try {
            SharedParam sharedParam = sharedParametersService.findByKey("profitPercentage");
            if(sharedParam != null) {
                double defaultPrice = Double.parseDouble(sharedParam.getFieldValue());
                double finalPrice;
                if(purchaseCarObject.getPriceOfSale() != 0) {
                    finalPrice = defaultPrice * purchaseCarObject.getPriceOfSale();
                } else {
                    finalPrice = defaultPrice * car.getPrice();
                }
                car.setPriceOfSale(finalPrice);
                carRepository.save(car);
                return ResponseEntity.ok(car);
            }
            else {
                return ResponseEntity.unprocessableEntity().body(car);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(car);
        }
    }

    public CarRepository getCarRepository() {
        return carRepository;
    }

    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
}
