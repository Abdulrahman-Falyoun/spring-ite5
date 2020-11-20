package com.ite5year.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ite5year.models.Employee;

@Service
public class RabbitMQSenderService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${ite5year.rabbitmq.exchange}")
    private String exchange;

    @Value("${ite5year.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Employee company) {
        rabbitTemplate.convertAndSend(exchange, routingkey, company);
        System.out.println("Send msg = " + company);

    }
}