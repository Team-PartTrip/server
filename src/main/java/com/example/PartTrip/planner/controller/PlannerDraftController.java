package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerBlockResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.enums.PlannerBlockType;
import com.example.PartTrip.planner.service.PlannerDraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/** 블록 지침 → AI 일정 초안 (#158) */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerDraftController {

    private final PlannerDraftService plannerDraftService;

    /** 블록 목록. 앱 · 웹이 이걸로 블록 화면을 그린다 */
    @GetMapping("/blocks")
    public List<PlannerBlockResponseDto> getBlocks() {
        return Arrays.stream(PlannerBlockType.values())
                .map(PlannerBlockResponseDto::from)
                .toList();
    }

    /** 플래너를 만들고 AI 초안을 채운다. 초대는 그 다음이다 */
    @PostMapping("/generate")
    public ResponseEntity<PlannerScheduleResponseDto> generate(
            Authentication authentication,
            @Valid @RequestBody GeneratePlannerRequestDto request
    ) {
        return ResponseEntity.ok(plannerDraftService.generate(request, authentication.getName()));
    }

    @GetMapping("/{plannerId}/schedule")
    public ResponseEntity<PlannerScheduleResponseDto> getSchedule(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                plannerDraftService.getSchedule(plannerId, authentication.getName()));
    }
}
