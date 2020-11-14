package com.ite5year;

import com.ite5year.models.Car;
import com.ite5year.repositories.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CarRepository carRepository) {
		return args -> {
			carRepository.save(new Car("#2c24BMW", "BMW 2020"));
			carRepository.save(new Car("#3c6eMer", "Mer 2020"));
			carRepository.save(new Car("#cxw24s", "Asana 2020"));
			carRepository.save(new Car("#TOswsdw", "Tows 2022"));


			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Car car : carRepository.findAll()) {
				log.info(car.toString());
			}
			log.info("");

		};
	}

}
