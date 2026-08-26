package com.example.PartTrip.tripcard.controller;

import com.example.PartTrip.tripcard.dto.response.TripCardResponse;
import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/trip-card")
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;


    @GetMapping
    public List<TripCardResponse> getTripCards() {
        return tripCardService.getTripCards();
    }


    @GetMapping("/{tripCardId}")
    public TripCardResponse getTripCard(@PathVariable Long tripCardId) {
        return tripCardService.getTripCard(tripCardId);
    }


}
