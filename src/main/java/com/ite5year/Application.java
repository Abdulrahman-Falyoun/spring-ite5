package com.ite5year;

import com.ite5year.models.SharedParameters;
import com.ite5year.repositories.SharedParametersRepository;
import com.ite5year.services.AuthenticationService;
import com.ite5year.services.CarServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	 public Map<String, Object> sharedParametersMap(SharedParametersRepository sharedParametersRepository) {
		HashMap<String, Object> res = new HashMap<>();
		List<SharedParameters> params = sharedParametersRepository.findAll();
		for (SharedParameters aParam : params) {
			res.put(aParam.getFieldKey(), aParam.getFieldValue());
		}
		 return res;
	 }

	@Bean
	public CarServiceImpl carService() {
		return new CarServiceImpl();
	}

	@Bean
	public AuthenticationService authenticationService() {
		return new AuthenticationService();
	};

}
