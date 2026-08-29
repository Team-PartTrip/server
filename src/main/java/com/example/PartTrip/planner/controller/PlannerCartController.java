package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.PlannerCartRequestDto;
import com.example.PartTrip.planner.dto.response.RandomPlaceResponseDto;
import com.example.PartTrip.planner.service.PlannerCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerCartController {

    private final PlannerCartService plannerCartService;

    @PostMapping("/{plannerId}/cart")
    public ResponseEntity<String> addPlaces(
            Authentication authentication,
            @PathVariable Long plannerId,
            @Valid @RequestBody PlannerCartRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                plannerCartService.addPlaces(
                        plannerId,
                        requestDto,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{plannerId}/cart/random")
    public ResponseEntity<RandomPlaceResponseDto> selectRandom(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerCartService.selectRandom(plannerId, authentication.getName())
        );
    }
}
