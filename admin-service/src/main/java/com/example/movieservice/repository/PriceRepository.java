package com.example.movieservice.repository;

import com.example.movieservice.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price,Long> {
    Price findByShowIdAndSeatTypeId(Long showId, Long seatTypeId);

    Optional<Price> findByIdAndShowIdAndSeatTypeId(Long priceId, Long showId, Long seatTypeId);

    boolean existsByIdAndShowIdAndSeatTypeId(Long priceId, Long showId, Long seatTypeId);
}
