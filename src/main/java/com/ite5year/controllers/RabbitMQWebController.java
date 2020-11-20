package com.ite5year.controllers;

import com.ite5year.models.RabbitMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ite5year.models.Employee;
import com.ite5year.services.RabbitMQSenderService;

@RestController
@RequestMapping(value = "/ite5year-rabbitmq/")
public class RabbitMQWebController {

    RabbitMQSenderService rabbitMQSender;

    @Autowired
    public void setRabbitMQSender(RabbitMQSenderService rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @PostMapping
    public String produce(@RequestBody RabbitMessage rabbitMessage) {
        rabbitMQSender.send(rabbitMessage);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }
    @GetMapping(value = "/producer")
    public String producer(@RequestParam("empName") String empName,@RequestParam("empId") String empId) {

        Employee emp=new Employee();
        emp.setEmpId(empId);
        emp.setEmpName(empName);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}