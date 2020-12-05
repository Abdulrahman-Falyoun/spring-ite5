package com.ite5year;

import com.ite5year.models.SharedParam;
import com.ite5year.repositories.SharedParametersRepository;
import com.ite5year.services.AuthenticationService;
import com.ite5year.services.CarServiceImpl;
import com.ite5year.services.SharedParametersServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	 public Map<String, Object> sharedParametersMap(SharedParametersRepository sharedParametersRepository) {
		HashMap<String, Object> res = new HashMap<>();
		List<SharedParam> params = sharedParametersRepository.findAll();
		for (SharedParam aParam : params) {
			res.put(aParam.getFieldKey(), aParam.getFieldValue());
		}
		 return res;
	 }

	 @Bean
	SharedParametersServiceImpl sharedParametersService() {
		return new SharedParametersServiceImpl();
	 }
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}
	@Bean
	RedisTemplate<String, SharedParam> redisTemplate() {
		RedisTemplate<String, SharedParam> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
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
