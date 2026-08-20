package com.example.PartTrip.login.controller;

import com.example.PartTrip.login.dto.GoogleLoginRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    // 앱에서 받은 Google idToken → 검증 후 우리 서비스 JWT 발급
    @PostMapping("/google")
    public TokenResponseDto google(@RequestBody GoogleLoginRequestDto dto) {
        return googleLoginService.loginWithGoogle(dto);
    }
}
