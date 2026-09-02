package com.example.PartTrip.password.controller;

import com.example.PartTrip.password.dto.PasswordResetRequestDto;
import com.example.PartTrip.password.service.FindPasswordService;
import com.example.PartTrip.signup.dto.EmailSendRequestDto;
import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/password")
public class FindPasswordController {

    private final FindPasswordService findPasswordService;

    // 1단계: 이메일 입력 -> 인증번호 발송
    @PostMapping("/send-code")
    public String sendCode(@Valid @RequestBody EmailSendRequestDto dto) {

        findPasswordService.sendResetCode(dto.getEmail());

        return "인증번호가 전송되었습니다.";
    }

    // 2단계: 이메일 인증번호 확인
    @PostMapping("/verify-code")
    public String verifyCode(@RequestBody EmailVerifyRequestDto dto) {

        findPasswordService.verifyResetCode(dto);

        return "이메일 인증이 완료되었습니다.";
    }

    // 3단계: 새 비밀번호 입력 + 재입력 -> 비밀번호 변경
    @PostMapping("/reset")
    public String resetPassword(@Valid @RequestBody PasswordResetRequestDto dto) {

        findPasswordService.resetPassword(dto);

        return "비밀번호가 변경되었습니다.";
    }
}
