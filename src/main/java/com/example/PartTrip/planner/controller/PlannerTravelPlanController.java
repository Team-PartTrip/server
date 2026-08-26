package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerTravelPlanResponseDto;
import com.example.PartTrip.planner.dto.request.SavePlannerTravelPlanRequestDto;
import com.example.PartTrip.planner.service.PlannerTravelPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerTravelPlanController {

    private final PlannerTravelPlanService plannerTravelPlanService;

    @PutMapping("/{plannerId}/travel-plan")
    public ResponseEntity<PlannerTravelPlanResponseDto> saveTravelPlan(
            Authentication authentication,
            @PathVariable Long plannerId,
            @Valid @RequestBody SavePlannerTravelPlanRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                plannerTravelPlanService.saveTravelPlan(
                        plannerId,
                        requestDto,
                        authentication.getName()
                )
        );
    }
}
