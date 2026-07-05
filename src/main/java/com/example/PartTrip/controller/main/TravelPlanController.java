package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.DdayResponseDto;
import com.example.PartTrip.dto.main.TravelPlanRequestDto;
import com.example.PartTrip.service.main.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    // 여행 일정 등록 또는 수정
    @PostMapping("/travel-plan")
    public DdayResponseDto saveTravelPlan(
            Authentication authentication,
            @RequestBody TravelPlanRequestDto dto
    ) {
        String userId = authentication.getName();

        return travelPlanService.saveTravelPlan(userId, dto);
    }
    // D-Day 조회
    @GetMapping("/dday")
    public DdayResponseDto getDday(Authentication authentication) {

        String userId = authentication.getName();

        return travelPlanService.getDday(userId);
    }
}