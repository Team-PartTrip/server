package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.PlannerConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerConfirmResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.service.PlannerConfirmService;
import com.example.PartTrip.planner.service.PlannerFinalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerConfirmController {

    private final PlannerConfirmService plannerConfirmService;
    private final PlannerFinalService plannerFinalService;

    @PostMapping("/{plannerId}/confirm")
    public ResponseEntity<PlannerConfirmResponseDto> confirmPlanner(
            Authentication authentication,
            @PathVariable Long plannerId,
            // 본문 없이 부르던 기존 호출을 그대로 받아준다
            @RequestBody(required = false) PlannerConfirmRequestDto request
    ) {
        return ResponseEntity.ok(
                plannerConfirmService.confirmPlanner(
                        plannerId,
                        request,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{plannerId}/confirmed-places")
    public ResponseEntity<PlannerFinalResponseDto> getConfirmedPlaces(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerFinalService.getConfirmedPlaces(plannerId, authentication.getName())
        );
    }
}
