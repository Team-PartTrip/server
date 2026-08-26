package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerDetailResponseDto;
import com.example.PartTrip.planner.service.PlannerDetailService;
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
public class PlannerDetailController {

    private final PlannerDetailService plannerDetailService;

    @GetMapping("/{plannerId}")
    public ResponseEntity<PlannerDetailResponseDto> getPlannerDetail(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerDetailService.getPlannerDetail(
                        plannerId,
                        authentication.getName()
                )
        );
    }
}
