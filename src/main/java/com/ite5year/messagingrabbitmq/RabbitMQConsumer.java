package com.ite5year.messagingrabbitmq;

import com.ite5year.models.Car;
import com.ite5year.models.RabbitMessage;
import com.ite5year.services.CarServiceImpl;
import com.ite5year.services.GoogleGmailService;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private CarServiceImpl carService;


    @Autowired
    public void setCarService(CarServiceImpl carService) {
        this.carService = carService;
    }


    @RabbitListener(queues = "ite5year.queue")
    public void receivedMessage(RabbitMessage rabbitMessage)   {
        logger.info("Received Message From RabbitMQ: " + rabbitMessage);

        String sellingDate = rabbitMessage.getDate();
        System.out.println("sellingDate: " + sellingDate);
        if(!sellingDate.equals("")) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            final LocalDateTime dt = LocalDateTime.parse(sellingDate, formatter);
            List<Car> cars = carService.findAllSoldCardByDate(dt);
            System.out.println("Cars: " + cars);
            String content = rabbitMessage.getContent();
            System.out.println("content: " + content);
            try {

                FileWriter writer = new FileWriter("sto1.csv");
                String collect = cars.stream().map(Car::getName).collect(Collectors.joining(","));
                System.out.println(collect);
                writer.write(collect);
                writer.close();



                File file = new File("sto1.csv");
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                String fileName = "cars.csv";
                FileDataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                GoogleGmailService.Send("abd.fl.19999@gmail.com", "A3#33$$F", rabbitMessage.getEmail(), "Report", content, multipart);
                System.out.println("Successfully an email is sent");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}