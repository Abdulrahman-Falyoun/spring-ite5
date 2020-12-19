package com.ite5year.integration;


import com.ite5year.Application;
import com.ite5year.messagingrabbitmq.RabbitMQConsumer;
import com.ite5year.messagingrabbitmq.RabbitMQSender;
import com.ite5year.models.Car;
import com.ite5year.models.RabbitMessage;
import com.ite5year.services.ApplicationUserDetailsServiceImpl;
import com.ite5year.utils.CsvUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;

import static com.ite5year.utils.GlobalConstants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportDataTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private RabbitMQSender rabbitMQSender;
    private RabbitMQConsumer rabbitMQConsumer;
    @Autowired
    ApplicationUserDetailsServiceImpl userDetailsService;

    private String getRootUrl() {
        return "http://localhost:4000" + BASE_URL;
    }

    public void setRabbitMQSender(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @BeforeEach
    void authorizeRequest() {
        System.out.println("authorizeRequest");
        restTemplate.getRestTemplate().getInterceptors().add(new RestTemplateHeaderModifierInterceptor(userDetailsService));
    }

    @Autowired
    public void setRabbitMQConsumer(RabbitMQConsumer rabbitMQConsumer) {
        this.rabbitMQConsumer = rabbitMQConsumer;
    }

    @Test
    public void testGeneratingReportForSoldCarInAMonth() {
        RabbitMessage rabbitMessage = new RabbitMessage();
        rabbitMessage.setContent("This is a dummy content for a rabbit message and only for testing purposes");
        rabbitMessage.setDate("2020-12-08 12:30:00");
        rabbitMessage.setEmail("abdulrahman-falyoun@outlook.com");
        ResponseEntity<RabbitMessage> postResponse = restTemplate.postForEntity(getRootUrl() + "/cars/report", rabbitMessage, RabbitMessage.class);
        System.out.println(postResponse);
        System.out.println(postResponse.getBody());
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }


    @Test
    public void testGeneratingReportForSoldCarInAMonthFromFile() {
        try {
//            File f = new File("sto1.csv");
//            InputStream inputStream = new FileInputStream(f);
//            List<Car> carList = CsvUtils.read(Car.class, inputStream);
            RabbitMessage rabbitMessage = new RabbitMessage();
            rabbitMessage.setContent("This is a dummy content for a rabbit message in a spring application and only for testing purposes");
            rabbitMessage.setDate("2020-01-08 12:30:00");
            rabbitMessage.setEmail("abdulrahman-falyoun@outlook.com");
            rabbitMessage.setFileName("sto1.csv");
            assertTrue(rabbitMQConsumer.sendToEmail(rabbitMessage));
        } catch (Exception e) {
            System.out.println("ERRRRRORORRORORO: " + e);
        }


    }

}
