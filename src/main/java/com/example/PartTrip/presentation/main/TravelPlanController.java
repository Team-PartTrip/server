package com.example.PartTrip.presentation.main;

import com.example.PartTrip.application.main.data.DdayResponseDto;
import com.example.PartTrip.application.main.data.TravelPlanRequestDto;
import com.example.PartTrip.application.main.TravelPlanService;
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