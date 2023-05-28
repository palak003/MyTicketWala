package com.example.bookingservice.kafka;

import com.example.movieservice.entities.Booking;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer {

    private static final Logger LOGGER= LoggerFactory.getLogger(BookingProducer.class);
    private NewTopic topic;

    private KafkaTemplate<String, Booking> kafkaTemplate;

    public BookingProducer(NewTopic topic, KafkaTemplate<String, Booking> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReservedMessage(Booking booking) {
        LOGGER.info(String.format("Seat reserved=?%s",booking.getSeat().toString()));
        Message<Booking> message= MessageBuilder.withPayload(booking).setHeader(KafkaHeaders.TOPIC,topic.name()).build();
        kafkaTemplate.send(message);
    }

    
}

