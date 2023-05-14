package com.example.movieservice.controllers;

import com.example.movieservice.entities.Price;
import com.example.movieservice.entities.SeatType;
import com.example.movieservice.entities.Show;
import com.example.movieservice.exception.ResourceNotFoundException;
import com.example.movieservice.repository.PriceRepository;
import com.example.movieservice.repository.SeatTypeRepository;
import com.example.movieservice.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class PriceController {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private SeatTypeRepository seatTypeRepository;
    @Autowired
    private PriceRepository priceRepository;

    @PostMapping("/shows/prices")
    public ResponseEntity<Price> createPrice(@RequestBody Price priceRequest) {
        Show show = showRepository.findById(priceRequest.getShow().getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found", priceRequest.getShow().getShowId()));
        SeatType seatType = seatTypeRepository.findById(priceRequest.getSeatType().getSeatTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Seat Type not found", priceRequest.getSeatType().getSeatTypeId()));

        Price price = Price.builder()
                .show(show)
                .seatType(seatType)
                .price(priceRequest.getPrice())
                .build();

        Price savedPrice = priceRepository.save(price);

        return ResponseEntity.ok(savedPrice);
    }

    @GetMapping("/shows/{showId}/seat-types/{seatTypeId}/prices")
    public ResponseEntity<Price> getAllPricesForShowAndSeatType(@PathVariable Long showId, @PathVariable Long seatTypeId) {
        Price prices = priceRepository.findByShowIdAndSeatTypeId(showId, seatTypeId);
        return ResponseEntity.ok(prices);
        }


    @GetMapping("/shows/{showId}/seat-types/{seatTypeId}/prices/{priceId}")
    public ResponseEntity<Price> getPriceForShowAndSeatType(@PathVariable Long showId, @PathVariable Long seatTypeId, @PathVariable Long priceId) {
        Price price = priceRepository.findByIdAndShowIdAndSeatTypeId(priceId, showId, seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Price not found", priceId));

        return ResponseEntity.ok(price);
    }

    @DeleteMapping("/shows/{showId}/seat-types/{seatTypeId}/prices/{priceId}")
    public ResponseEntity<?> deletePriceForShowAndSeatType(@PathVariable Long showId, @PathVariable Long seatTypeId, @PathVariable Long priceId) {
        if (!priceRepository.existsByIdAndShowIdAndSeatTypeId(priceId, showId, seatTypeId)) {
            throw new ResourceNotFoundException("Price not found", priceId);
        }

        priceRepository.deleteById(priceId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/shows/{showId}/seat-types/{seatTypeId}/prices/{priceId}")
    public ResponseEntity<Price> updatePriceForShowAndSeatType(@RequestBody Price priceRequest) {
        Price price = priceRepository.findByIdAndShowIdAndSeatTypeId(priceRequest.getId(), priceRequest.getShow().getShowId(),priceRequest.getSeatType().getSeatTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found", priceRequest.getId()));

        price.setPrice(priceRequest.getPrice());

        Price savedPrice = priceRepository.save(price);

        return ResponseEntity.ok(savedPrice);
    }



}
