package com.example.movieservice.controllers;

import com.example.movieservice.entities.Seat;
import com.example.movieservice.entities.SeatType;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.repository.SeatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class SeatTypeController {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @PostMapping("/seatTypes")
    public SeatType createSeatType(@RequestBody SeatType seatType) {
        return seatTypeRepository.save(seatType);
    }

    @GetMapping("/seatTypes")
    public List<SeatType> getAllSeatTypes() {
        return seatTypeRepository.findAll();
    }

    @GetMapping("/seatTypes/{seatTypeId}")
    public SeatType getSeatTypeById(@PathVariable Long seatTypeId) {
        return seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType not found", seatTypeId));
    }

    @PutMapping("/seatTypes/{seatTypeId}")
    public SeatType updateSeatType(@PathVariable Long seatTypeId, @RequestBody SeatType seatTypeDetails) {
        SeatType seatType = seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType not found", seatTypeId));

        seatType.setSeatTypeName(seatTypeDetails.getSeatTypeName());

        return seatTypeRepository.save(seatType);
    }

    @DeleteMapping("/seatTypes/{seatTypeId}")
    public ResponseEntity<?> deleteSeatType(@PathVariable Long seatTypeId) {
        SeatType seatType = seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType not found", seatTypeId));

        seatTypeRepository.delete(seatType);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/seatTypes/{seatTypeId}/seats")
    public List<Seat> getSeatsBySeatTypeId(@PathVariable Long seatTypeId) {
        SeatType seatType = seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType not found", seatTypeId));

        return seatType.getSeats();
    }


}
