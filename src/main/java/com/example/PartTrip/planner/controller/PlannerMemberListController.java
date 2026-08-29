package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.response.PlannerMemberResponseDto;
import com.example.PartTrip.planner.service.PlannerMemberListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerMemberListController {

    private final PlannerMemberListService plannerMemberListService;

    @GetMapping("/{plannerId}/members")
    public ResponseEntity<List<PlannerMemberResponseDto>> getPlannerMembers(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerMemberListService.getPlannerMembers(
                        plannerId,
                        authentication.getName()
                )
        );
    }
}
