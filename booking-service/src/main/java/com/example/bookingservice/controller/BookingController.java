package com.example.bookingservice.controller;

import com.example.bookingservice.kafka.BookingProducer;
import com.example.movieservice.entities.Booking;
import com.example.movieservice.entities.BookingStatus;
import com.example.movieservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    @Autowired
    private  BookingProducer bookingProducer;
    @Autowired
    private BookingRepository bookingRepository;


    public BookingController(BookingProducer bookingProducer) {
        this.bookingProducer = bookingProducer;
    }

    @PostMapping("/reserveSeat")
    public ResponseEntity<String> reserveSeat(@RequestBody Booking booking){
            booking.getSeat().setStatus(BookingStatus.RESERVED);
            bookingRepository.save(booking);
            bookingProducer.sendReservedMessage(booking);
        return new ResponseEntity<>("Booking is being processed..", HttpStatus.OK);
    }

}
