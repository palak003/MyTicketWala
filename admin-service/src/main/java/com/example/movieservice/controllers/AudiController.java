package com.example.movieservice.controllers;

import com.example.movieservice.entities.Audi;
import com.example.movieservice.entities.Cinema;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.repository.AudiRepository;
import com.example.movieservice.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class AudiController {

    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private AudiRepository audiRepository;

    @GetMapping("/cinemas/{cinemaId}/audis")
    public List<Audi> getAllAudisForCinema(@PathVariable Long cinemaId) {
        Optional<Cinema> cinema = cinemaRepository.findById(cinemaId);
        if (cinema.isPresent()) {
            return cinema.get().getAudis();
        } else {
            throw new ResourceNotFoundException("Cinema hall not found", cinemaId);
        }
    }

    @PostMapping("/cinemas/{cinemaId}/audis")
    public ResponseEntity<?> createAudi(@PathVariable Long cinemaId,@RequestBody Audi audi) {
        Optional<Cinema> cinema = cinemaRepository.findById(cinemaId);
        if (cinema.isPresent()) {
            Cinema existingCinema = cinema.get();
            audi.setCinema(existingCinema);
            Audi newAudi = audiRepository.save(audi);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Cinema hall not found", cinemaId);
        }
    }

    @DeleteMapping("/cinemas/{cinemaId}/audis/{audiId}")
    public ResponseEntity<?> deleteAudi(@PathVariable Long cinemaId, @PathVariable Long audiId) {
        Optional<Audi> audi = audiRepository.findByIdAndCinemaId(audiId, cinemaId);
        if (audi.isPresent()) {
            audiRepository.delete(audi.get());
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Audi not found", audiId);
        }
    }


}
