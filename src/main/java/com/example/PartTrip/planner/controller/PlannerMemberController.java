package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerJoinResponseDto;
import com.example.PartTrip.planner.service.PlannerMemberService;
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
public class PlannerMemberController {

    private final PlannerMemberService plannerMemberService;

    @PostMapping("/join")
    public ResponseEntity<PlannerJoinResponseDto> joinPlanner(
            Authentication authentication,
            @Valid @RequestBody JoinPlannerRequestDto requestDto
    ) {
        PlannerJoinResponseDto response = plannerMemberService.joinPlanner(
                requestDto,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
