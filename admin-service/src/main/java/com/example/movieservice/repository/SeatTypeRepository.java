package com.example.movieservice.repository;

import com.example.movieservice.entities.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatTypeRepository extends JpaRepository<SeatType,Long> {
}
