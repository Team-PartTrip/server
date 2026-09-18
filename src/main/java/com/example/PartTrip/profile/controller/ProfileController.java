package com.example.PartTrip.profile.controller;

import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.service.ProfileService;
import com.example.PartTrip.profile.service.TravelPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final TravelPreferenceService travelPreferenceService;

    @GetMapping("/myInfo")
    public ResponseEntity<ProfileResponseDto> getProfile(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.getProfile(userId);
        return ResponseEntity.ok(resDto);
    }

    // 여행 통계 (Func-007-01) — 마이 탭 상단의 여행 · 국가 · 기록 3칸
    @GetMapping("/stats")
    public ResponseEntity<ProfileStatsResponseDto> getStats(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.getStats(userId));
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(resDto);
    }

    // Func-007-02 여행 편의 설정 조회
    @GetMapping("/travel-preferences")
    public ResponseEntity<TravelPreferenceResponseDto> getTravelPreferences(
            Authentication authentication
    ) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(travelPreferenceService.getPreference(userId));
    }

    // Func-007-02 여행 편의 설정 수정
    @PutMapping("/travel-preferences")
    public ResponseEntity<TravelPreferenceResponseDto> updateTravelPreferences(
            Authentication authentication,
            @Valid @RequestBody TravelPreferenceRequestDto request
    ) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(travelPreferenceService.updatePreference(userId, request));
    }


    // 프로필 사진 업로드 (Func-007-01)
    // 업로드된 이미지의 공개 URL 을 문자열로 반환한다.
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(profileService.uploadProfileImage(file));
    }
}
