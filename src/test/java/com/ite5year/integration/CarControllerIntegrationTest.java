package com.ite5year.integration;


import com.ite5year.Application;
import com.ite5year.authentication.handlers.JwtUtils;
import com.ite5year.models.ApplicationUser;
import com.ite5year.models.Car;
import com.ite5year.repositories.ApplicationUserRepository;
import com.ite5year.services.ApplicationUserDetailsServiceImpl;
import com.ite5year.services.AuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;


import java.util.Optional;

import static com.ite5year.utils.GlobalConstants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ApplicationUserDetailsServiceImpl userDetailsService;

    @LocalServerPort
    private int port;
    @Autowired
    ApplicationUserRepository applicationUserRepository;


    private String getRootUrl() {
        return "http://localhost:4000" + BASE_URL;
    }


    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().getInterceptors().add(new RestTemplateHeaderModifierInterceptor(userDetailsService));
    }

    @BeforeAll
    static void beforeAll() {
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllCars() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/cars",
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }

    @Test
    public void testGetCarById() {
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/1", Car.class);
        System.out.println(car.getName());
        assertNotNull(car);
    }

    @Test
    public void testCreateCar() {
        Car car = new Car();
        car.setName("Mercedes");
        car.setPrice(2000);
        car.setSeatsNumber(5);
        car.setPayerName("admin");
        ResponseEntity<Car> postResponse = restTemplate.postForEntity(getRootUrl() + "/cars", car, Car.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateCar() {
        int id = 1;
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        car.setPayerName("admin1");
        car.setSeatsNumber(6);
        restTemplate.put(getRootUrl() + "/cars/" + id, car);
        Car updatedCar = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        assertNotNull(updatedCar);
    }

    @Test
    public void testDeleteCar() {
        int id = 2;
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        assertNotNull(car);
        restTemplate.delete(getRootUrl() + "/cars/" + id);
        try {
            car = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
