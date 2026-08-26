package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.service.PlannerFinalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerFinalController {

    private final PlannerFinalService plannerFinalService;

    @GetMapping("/{plannerId}/confirmed-places")
    public ResponseEntity<PlannerFinalResponseDto> getConfirmedPlaces(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerFinalService.getConfirmedPlaces(
                        plannerId,
                        authentication.getName()
                )
        );
    }
}
