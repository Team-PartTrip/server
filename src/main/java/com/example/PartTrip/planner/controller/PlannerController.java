package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.CreatePlannerRequestDto;
import com.example.PartTrip.planner.dto.PlannerCreateResponseDto;
import com.example.PartTrip.planner.service.PlannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;

    @PostMapping
    public ResponseEntity<PlannerCreateResponseDto> createPlanner(
            Authentication authentication,
            @Valid @RequestBody CreatePlannerRequestDto requestDto
    ) {
        PlannerCreateResponseDto response = plannerService.createPlanner(
                requestDto,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
