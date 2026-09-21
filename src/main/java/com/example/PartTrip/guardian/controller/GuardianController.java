package com.example.PartTrip.guardian.controller;

import com.example.PartTrip.guardian.dto.GuardianDtos.AcceptRequest;
import com.example.PartTrip.guardian.dto.GuardianDtos.InviteResponse;
import com.example.PartTrip.guardian.dto.GuardianDtos.LinkResponse;
import com.example.PartTrip.guardian.service.GuardianService;
import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

/** 보호자 (#159). 보기만 한다 — 수정 API 는 두지 않는다 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guardians")
public class GuardianController {

    private final GuardianService guardianService;

    /** 시니어: 보호자 초대 코드 만들기 */
    @PostMapping("/invite")
    public InviteResponse invite(Authentication authentication) {
        return guardianService.createInvite(authentication.getName());
    }

    /** 보호자: 코드로 연결 */
    @PostMapping("/accept")
    public LinkResponse accept(Authentication authentication, @Valid @RequestBody AcceptRequest request) {
        return guardianService.accept(request.code(), authentication.getName());
    }

    /** 보호자: 내가 보호하는 시니어 */
    @GetMapping("/seniors")
    public List<LinkResponse> seniors(Authentication authentication) {
        return guardianService.getSeniors(authentication.getName());
    }

    /** 시니어: 나를 보호하는 사람 */
    @GetMapping("/me")
    public List<LinkResponse> guardians(Authentication authentication) {
        return guardianService.getGuardians(authentication.getName());
    }

    /** 시니어 · 보호자: 연결 끊기 */
    @DeleteMapping("/{linkId}")
    public ResponseEntity<Void> unlink(Authentication authentication, @PathVariable Long linkId) {
        guardianService.unlink(linkId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /** 보호자: 시니어의 플래너 목록 */
    @GetMapping("/seniors/{seniorUserId}/planners")
    public List<PlannerListResponseDto> seniorPlanners(
            Authentication authentication, @PathVariable String seniorUserId) {
        return guardianService.getSeniorPlanners(seniorUserId, authentication.getName());
    }

    /** 보호자: 시니어의 일정 카드 */
    @GetMapping("/seniors/{seniorUserId}/planners/{plannerId}/schedule")
    public PlannerScheduleResponseDto seniorSchedule(
            Authentication authentication,
            @PathVariable String seniorUserId,
            @PathVariable Long plannerId) {
        return guardianService.getSeniorSchedule(seniorUserId, plannerId, authentication.getName());
    }
}
