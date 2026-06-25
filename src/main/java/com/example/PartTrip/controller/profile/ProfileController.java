package com.example.PartTrip.controller.profile;

import com.example.PartTrip.dto.profile.ProfileResponseDto;
import com.example.PartTrip.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;


    @GetMapping("/myInfo")
    public ResponseEntity<ProfileResponseDto> getProfile(Authentication authentication){

        String userId = (String) authentication.getPrincipal();
        ProfileResponseDto resDto = profileService.getProfile(userId);

        return ResponseEntity.ok(resDto);
    }

//    @GetMapping("/myInfo")
//    public ProfileResponseDto getProfile(@PathVariable String userId) {
//        return profileService.getProfile(userId);
//    }

//    @PatchMapping("/{userId}")
//    public ProfileResponseDto updateProfile(
//            @PathVariable String userId,
//            @Valid @RequestBody ProfileUpdateRequestDto requestDto
//    ) {
//        return profileService.updateProfile(userId, requestDto);
//    }
}
