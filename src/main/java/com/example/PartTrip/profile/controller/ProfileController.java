package com.example.PartTrip.profile.controller;

import com.example.PartTrip.profile.dto.CharacterInfoResponseDto;
import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelThemeResponseDto;
import com.example.PartTrip.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(resDto);
    }

    // TODO: 캐릭터 미선택 유저 처리 보류
    // 심리테스트 기능 미구현 상태에서 character_id가 null인 유저가 존재할 수 있음
    // 심리테스트 완료 후 character_id가 null이면 강제로 심리테스트 화면으로 이동 처리 필요
    @GetMapping("/character")
    public ResponseEntity<CharacterInfoResponseDto> getCharacterInfo(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        CharacterInfoResponseDto resDto = profileService.getCharacterInfo(userId);
        return ResponseEntity.ok(resDto);
    }

    // 여행 타입 목록 조회 (Func-007-01)
    // 프로필 수정 화면에서 선택지를 그리는 데 사용한다
    @GetMapping("/themes")
    public ResponseEntity<List<TravelThemeResponseDto>> getTravelThemes() {
        return ResponseEntity.ok(profileService.getTravelThemes());
    }
}
