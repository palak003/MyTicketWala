package com.example.notificationsservice.service;

import com.example.movieservice.entities.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
public class SendMailService {


    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics="${spring.kafka.topic.name}",groupId="${spring.kafka.consumer.group-id}")
    public void send(@Payload Booking booking) {
        String text="";
        if(booking.getPaymentStatus())
            text="Congratulations your seat has been confirmed";
        else
            text="An error occurred please try the payment again";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mailersendit@gmail.com");
        message.setTo(booking.getUser().getUsername());
        message.setSubject("Booking confirmation");
        message.setText(text);
        javaMailSender.send(message);
    }
}
