package com.example.movieservice.controllers;

import com.example.movieservice.entities.Cinema;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class CinemaController {

    @Autowired
    private CinemaRepository cinemaRepository;
    @PostMapping("/cinemas")
    public Cinema createCinema(@RequestBody Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    @GetMapping("/cinemas")
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @GetMapping("/cinemas/{id}")
    public Cinema getCinemaById(@PathVariable(value = "cinemaId") Long cinemaId) {
        return cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema does not exist", cinemaId));
    }

    @PutMapping("/cinemas/{id}")
    public Cinema updateCinema(@PathVariable(value = "cinemaId") Long cinemaId, @RequestBody Cinema cinemaDetails) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema does not exist", cinemaId));

        cinema.setCinemaHallName(cinemaDetails.getCinemaHallName());

        return cinemaRepository.save(cinema);
    }

}
