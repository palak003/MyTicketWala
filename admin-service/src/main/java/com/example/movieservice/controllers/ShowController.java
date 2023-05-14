package com.example.movieservice.controllers;

import com.example.movieservice.entities.Audi;
import com.example.movieservice.entities.Movie;
import com.example.movieservice.entities.Show;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.exception.ShowConflictException;
import com.example.movieservice.repository.AudiRepository;
import com.example.movieservice.repository.MovieRepository;
import com.example.movieservice.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private AudiRepository audiRepository;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies/{movieId}/shows")
    public List<Show> getShowsForMovie(@PathVariable Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    @GetMapping("/audis/{audiId}/shows")
    public List<Show> getShowsForAudi(@PathVariable Long audiId) {
        return showRepository.findByAudiId(audiId);
    }

    @PostMapping("/shows")
    public ResponseEntity<?> createShow(@RequestBody Show request) {
        if (showRepository.existsByAudiAndStartTimeAndEndTime(request.getAudi(), request.getStartTime(), request.getEndTime())) {
            throw new ShowConflictException("There is already a show with the same start and end time in the same audi.");
        }
        Show show = Show.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .audi(request.getAudi())
                .movie(request.getMovie())
                .bookings(new ArrayList<>())
                .build();

        return ResponseEntity.ok(show);
    }


    @PutMapping("/shows/{showId}")
    public ResponseEntity<?> updateShow(@PathVariable Long showId, @RequestBody Show showDTO) {
        Optional<Show> optionalShow = showRepository.findById(showId);
        if (optionalShow.isPresent()) {
            Show show = optionalShow.get();
            show.setStartTime(showDTO.getStartTime());
            show.setEndTime(showDTO.getEndTime());
            Audi audi = audiRepository.findById(showDTO.getAudi().getAudiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Audi not found", showDTO.getAudi().getAudiId()));
            show.setAudi(audi);
            Movie movie = movieRepository.findById(showDTO.getMovie().getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found", showDTO.getMovie().getMovieId()));
            show.setMovie(movie);
            showRepository.save(show);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Show not found", showId);
        }
    }


    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<?> deleteShow(@PathVariable Long showId) {
        Optional<Show> optionalShow = showRepository.findById(showId);
        if (optionalShow.isPresent()) {
            showRepository.delete(optionalShow.get());
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Show not found", showId);
        }
    }


}
