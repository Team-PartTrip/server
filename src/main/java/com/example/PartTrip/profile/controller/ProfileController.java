package com.example.PartTrip.profile.controller;

import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.service.AccountDeleteService;
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
    private final AccountDeleteService accountDeleteService;

    /** 로그인 사용자의 프로필 정보를 조회한다. */
    @GetMapping("/myInfo")
    public ResponseEntity<ProfileResponseDto> getProfile(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.getProfile(userId);
        return ResponseEntity.ok(resDto);
    }

    /** Func-007-01 마이 탭의 여행, 국가, 기록 통계를 조회한다. */
    @GetMapping("/stats")
    public ResponseEntity<ProfileStatsResponseDto> getStats(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.getStats(userId));
    }

    /** 로그인 사용자의 프로필 정보를 수정한다. */
    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(resDto);
    }

    /** 회원 탈퇴. 계정과 사용자가 남긴 데이터를 지운다. */
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        accountDeleteService.deleteAccount((String) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    /** Func-007-02 로그인 사용자의 여행 편의 설정을 조회한다. */
    @GetMapping("/travel-preferences")
    public ResponseEntity<TravelPreferenceResponseDto> getTravelPreferences(
            Authentication authentication
    ) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(travelPreferenceService.getPreference(userId));
    }

    /** Func-007-02 로그인 사용자의 여행 편의 설정을 수정한다. */
    @PutMapping("/travel-preferences")
    public ResponseEntity<TravelPreferenceResponseDto> updateTravelPreferences(
            Authentication authentication,
            @Valid @RequestBody TravelPreferenceRequestDto request
    ) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(travelPreferenceService.updatePreference(userId, request));
    }


    /** Func-007-01 프로필 사진을 업로드하고 공개 URL을 반환한다. */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.uploadProfileImage(userId, file));
    }
}
