package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.DdayResponseDto;
import com.example.PartTrip.main.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    // D-Day 조회
    @GetMapping("/dday")
    public DdayResponseDto getDday(Authentication authentication) {

        String userId = authentication.getName();

        return travelPlanService.getDday(userId);
    }
}
