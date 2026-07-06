package com.example.PartTrip.controller.main.search;

import com.example.PartTrip.dto.main.search.ChangeTravelCountryRequestDto;
import com.example.PartTrip.service.main.TravelPlanService;
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