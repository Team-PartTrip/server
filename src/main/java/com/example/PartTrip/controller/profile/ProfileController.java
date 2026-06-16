package com.example.PartTrip.controller.profile;

import com.example.PartTrip.dto.profile.ProfileResponseDto;
import com.example.PartTrip.dto.profile.ProfileUpdateRequestDto;
import com.example.PartTrip.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ProfileResponseDto getProfile(@PathVariable String userId) {
        return profileService.getProfile(userId);
    }

    @PatchMapping("/{userId}")
    public ProfileResponseDto updateProfile(
            @PathVariable String userId,
            @Valid @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        return profileService.updateProfile(userId, requestDto);
    }
}
