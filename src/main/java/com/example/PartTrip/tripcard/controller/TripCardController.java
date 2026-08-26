package com.example.PartTrip.tripcard.controller;

import com.example.PartTrip.tripcard.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/trip-card")
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;


}
