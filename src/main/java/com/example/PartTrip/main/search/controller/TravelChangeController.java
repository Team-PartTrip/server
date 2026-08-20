package com.example.PartTrip.main.search.controller;

import com.example.PartTrip.main.search.dto.ChangeTravelCountryRequestDto;
import com.example.PartTrip.main.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/search")
public class TravelChangeController {

    private final TravelPlanService travelPlanService;

    @PatchMapping("/travel-change")
    public void changeTravelCountry(
            @RequestBody ChangeTravelCountryRequestDto request
    ){

        travelPlanService.changeTravelCountry(
                request.getTravelPlanId(),
                request.getCountryInfoId()
        );

    }

}