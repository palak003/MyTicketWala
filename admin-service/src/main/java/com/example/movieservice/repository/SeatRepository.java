package com.example.movieservice.repository;

import com.example.movieservice.entities.Audi;
import com.example.movieservice.entities.Seat;
import com.example.movieservice.entities.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByAudiAndSeatType(Audi audi, SeatType seatType);
}
