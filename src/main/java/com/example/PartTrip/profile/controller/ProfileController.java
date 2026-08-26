package com.example.PartTrip.profile.controller;

import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelThemeResponseDto;
import com.example.PartTrip.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

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


    // 여행 타입 목록 조회 (Func-007-01)
    // 프로필 수정 화면에서 선택지를 그리는 데 사용한다
    @GetMapping("/themes")
    public ResponseEntity<List<TravelThemeResponseDto>> getTravelThemes() {
        return ResponseEntity.ok(profileService.getTravelThemes());
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
