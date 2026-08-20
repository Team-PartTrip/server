package com.example.PartTrip.signup.controller;

import com.example.PartTrip.signup.dto.EmailSendRequestDto;
import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.service.MailService;
import com.example.PartTrip.signup.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
public class AuthMailController {

    private final MailService mailService;
    private final SignUpService signUpService;

    @PostMapping("/send")
    public String sendCode(@RequestBody EmailSendRequestDto dto) {

        mailService.sendCode(dto.getEmail());

        return "인증번호가 전송되었습니다.";

    }

    @PostMapping("/verify")
    public UserEntity verifyCode(@RequestBody EmailVerifyRequestDto dto) {

        mailService.verifyCode(dto);

        return signUpService.completeSignUp(dto.getEmail());

    }

}
