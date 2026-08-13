package com.example.PartTrip.presentation.profile;

import com.example.PartTrip.application.profile.data.CharacterInfoResponseDto;
import com.example.PartTrip.application.profile.data.ProfileResponseDto;
import com.example.PartTrip.application.profile.data.ProfileUpdateRequestDto;
import com.example.PartTrip.application.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
