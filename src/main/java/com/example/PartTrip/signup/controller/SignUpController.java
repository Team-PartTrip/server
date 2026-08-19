package com.example.PartTrip.signup.controller;

import com.example.PartTrip.signup.dto.SignUpRequestDto;
import com.example.PartTrip.signup.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {

    private final SignUpService signUpService;

    // 임시 저장 + 이메일 인증번호 전송을 함
    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequestDto dto) {

        // 회원가입 정보 임시 저장 + 인증번호 전송
        signUpService.startSignUp(dto);

        return "인증번호가 전송되었습니다.";
    }
}