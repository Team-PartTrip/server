package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.SavePlannerScheduleRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.service.PlannerDraftService;
import com.example.PartTrip.planner.service.PlannerScheduleEditService;
import com.example.PartTrip.planner.service.PlannerScheduleRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/** AI 초안의 카드 편집과 추가 후보 검색. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners/{plannerId}/schedule")
public class PlannerScheduleController {
    private final PlannerScheduleEditService service;
    private final PlannerScheduleRouteService routeService;
    private final PlannerDraftService draftService;

    /** 배열 순서대로 전체 일정을 저장한다. */
    @PutMapping
    public PlannerScheduleResponseDto save(@PathVariable Long plannerId,
            @Valid @RequestBody SavePlannerScheduleRequestDto request, Authentication authentication) {
        String userId = authentication.getName();
        service.save(plannerId, request, userId);
        routeService.recalculate(plannerId);
        return draftService.getSchedule(plannerId, userId);
    }

    /** 그 날짜의 지역에서 아직 일정에 없는 장소를 검색한다. */
    @GetMapping("/candidates")
    public List<PlannerScheduleResponseDto.Place> candidates(@PathVariable Long plannerId,
            @RequestParam LocalDate date, @RequestParam(defaultValue = "") String q,
            Authentication authentication) {
        return service.candidates(plannerId, date, q, authentication.getName());
    }
}
