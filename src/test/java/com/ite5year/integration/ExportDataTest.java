package com.ite5year.integration;


import com.ite5year.Application;
import com.ite5year.models.RabbitMessage;
import com.ite5year.services.ApplicationUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import static com.ite5year.utils.GlobalConstants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportDataTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ApplicationUserDetailsServiceImpl userDetailsService;

    private String getRootUrl() {
        return "http://localhost:4000" + BASE_URL;
    }


    @BeforeEach
    void authorizeRequest() {
        System.out.println("authorizeRequest");
        restTemplate.getRestTemplate().getInterceptors().add(new RestTemplateHeaderModifierInterceptor(userDetailsService));
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
        RabbitMessage rabbitMessage = new RabbitMessage();
        rabbitMessage.setContent("This is a dummy content for a rabbit message and only for testing purposes");
        rabbitMessage.setDate("2020-01-08 12:30:00");
        rabbitMessage.setEmail("abdulrahman-falyoun@outlook.com");
    }

}
