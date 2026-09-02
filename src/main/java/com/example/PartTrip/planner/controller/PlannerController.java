package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerCreateResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerDetailResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.service.PlannerDeleteService;
import com.example.PartTrip.planner.service.PlannerDetailService;
import com.example.PartTrip.planner.service.PlannerListService;
import com.example.PartTrip.planner.service.PlannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;
    private final PlannerListService plannerListService;
    private final PlannerDetailService plannerDetailService;
    private final PlannerDeleteService plannerDeleteService;

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

    @GetMapping
    public ResponseEntity<List<PlannerListResponseDto>> getMyPlanners(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                plannerListService.getMyPlanners(authentication.getName())
        );
    }

    @GetMapping("/{plannerId}")
    public ResponseEntity<PlannerDetailResponseDto> getPlannerDetail(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerDetailService.getPlannerDetail(plannerId, authentication.getName())
        );
    }

    @DeleteMapping("/{plannerId}")
    public ResponseEntity<Void> deletePlanner(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        plannerDeleteService.deletePlanner(plannerId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
