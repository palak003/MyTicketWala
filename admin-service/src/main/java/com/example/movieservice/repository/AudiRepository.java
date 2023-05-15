package com.example.movieservice.repository;

import com.example.movieservice.entities.Audi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AudiRepository extends JpaRepository<Audi,Long> {
    Optional<Audi> findByIdAndCinemaId(Long audiId, Long cinemaId);
}
