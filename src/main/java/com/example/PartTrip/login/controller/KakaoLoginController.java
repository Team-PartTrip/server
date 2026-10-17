package com.example.PartTrip.login.controller;

import com.example.PartTrip.login.dto.KakaoLoginRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    // 앱에서 받은 카카오 액세스 토큰(또는 웹의 auth code) → 우리 서비스 JWT 발급
    @PostMapping("/kakao")
    public TokenResponseDto kakao(@RequestBody KakaoLoginRequestDto dto) {
        return kakaoLoginService.loginWithKakao(dto);
    }
}
