package com.example.movieservice.controllers;

import com.example.movieservice.entities.Audi;
import com.example.movieservice.entities.Seat;
import com.example.movieservice.entities.SeatType;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.repository.AudiRepository;
import com.example.movieservice.repository.SeatRepository;
import com.example.movieservice.repository.SeatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class SeatController {

    @Autowired
    private AudiRepository audiRepository;
    @Autowired
    private SeatTypeRepository seatTypeRepository;
    @Autowired
    private SeatRepository seatRepository;

    @PostMapping("/audis/{audiId}/seat-types/{seatTypeId}/seats")
    public ResponseEntity<Seat> createSeat(@PathVariable Long audiId, @PathVariable Long seatTypeId, @RequestBody Seat seat) {
        Optional<Audi> audi = audiRepository.findById(audiId);
        Optional<SeatType> seatType = seatTypeRepository.findById(seatTypeId);

        if (audi.isPresent() && seatType.isPresent()) {
            seat.setAudi(audi.get());
            seat.setSeatType(seatType.get());
            Seat savedSeat = seatRepository.save(seat);
            return ResponseEntity.ok(savedSeat);
        } else {
            throw new ResourceNotFoundException("Audi not found",audiId);
        }
    }

    @GetMapping("/audis/{audiId}/seat-types/{seatTypeId}/seats")
    public ResponseEntity<List<Seat>> getSeatsByAudiAndSeatType(@PathVariable Long audiId, @PathVariable Long seatTypeId) {
        Optional<Audi> audi = audiRepository.findById(audiId);
        Optional<SeatType> seatType = seatTypeRepository.findById(seatTypeId);

        if (audi.isPresent() && seatType.isPresent()) {
            List<Seat> seats = seatRepository.findByAudiAndSeatType(audi.get(), seatType.get());
            return ResponseEntity.ok(seats);
        } else {
            throw new ResourceNotFoundException("Audi not found",audiId);
        }
    }

    @GetMapping("/seats/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            return ResponseEntity.ok(seat.get());
        } else {
            throw new ResourceNotFoundException("Seat not found", id);
        }
    }

    @DeleteMapping("/seats/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            seatRepository.delete(seat.get());
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Seat not found", id);
        }
    }

}
