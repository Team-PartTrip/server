package com.example.PartTrip.main.search.controller;

import com.example.PartTrip.main.search.dto.ChangeTravelCountryRequestDto;
import com.example.PartTrip.main.service.TravelPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/search")
public class TravelChangeController {

    private final TravelPlanService travelPlanService;

    @PatchMapping("/travel-change")
    public void changeTravelCountry(
            Authentication authentication,
            @Valid @RequestBody ChangeTravelCountryRequestDto request
    ){
        String userId = (String) authentication.getPrincipal();

        travelPlanService.changeTravelCountry(
                userId,
                request.getTravelPlanId(),
                request.getCountryInfoId()
        );
    }

}
