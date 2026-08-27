package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerConfirmResponseDto;
import com.example.PartTrip.planner.service.PlannerConfirmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerConfirmController {

    private final PlannerConfirmService plannerConfirmService;

    @PostMapping("/{plannerId}/confirm")
    public ResponseEntity<PlannerConfirmResponseDto> confirmPlanner(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerConfirmService.confirmPlanner(
                        plannerId,
                        authentication.getName()
                )
        );
    }
}
