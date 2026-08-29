package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.service.PlannerListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerListController {

    private final PlannerListService plannerListService;

    @GetMapping
    public ResponseEntity<List<PlannerListResponseDto>> getMyPlanners(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                plannerListService.getMyPlanners(authentication.getName())
        );
    }
}
