package com.ite5year.messagingrabbitmq;

import com.ite5year.models.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    @RabbitListener(queues = "ite5year.queue")
    public void receivedMessage(RabbitMessage rabbitMessage)   {
        logger.info("Received Message From RabbitMQ: " + rabbitMessage);

    }
}