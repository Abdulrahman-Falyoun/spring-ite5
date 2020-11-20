package com.ite5year.services;

import com.ite5year.models.RabbitMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ite5year.models.Employee;

@Service
public class RabbitMQSenderService {

    private AmqpTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${ite5year.rabbitmq.exchange}")
    private String exchange;

    @Value("${ite5year.rabbitmq.routingkey}")
    private String routingkey;

    public void send(RabbitMessage rabbitMessage) {
        rabbitTemplate.convertAndSend(exchange, routingkey, rabbitMessage);
        System.out.println("Send msg = " + rabbitMessage);

    }
}