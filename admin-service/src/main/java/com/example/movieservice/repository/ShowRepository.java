package com.example.movieservice.repository;

import com.example.movieservice.entities.Audi;
import com.example.movieservice.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovieId(Long movieId);

    List<Show> findByAudiId(Long audiId);

    boolean existsByAudiAndStartTimeAndEndTime(Audi audi, LocalDateTime startTime, LocalDateTime endTime);
}
