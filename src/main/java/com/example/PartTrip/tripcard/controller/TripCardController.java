package com.example.PartTrip.tripcard.controller;

import com.example.PartTrip.tripcard.dto.response.TripCardDetailResponse;
import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/trip-cards")
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;


    @GetMapping
    public List<TripCardResponse> getTripCards() {
        return tripCardService.getTripCards();
    }


    @GetMapping("/{tripCardId}")
    public TripCardDetailResponse getTripCard(@PathVariable Long tripCardId) {
        return tripCardService.getTripCard(tripCardId);
    }


    @DeleteMapping
    public String deleteTripCard(@RequestBody Set<Long> tripCardIds) {
        return tripCardService.deleteTripCard(tripCardIds);
    }


}
