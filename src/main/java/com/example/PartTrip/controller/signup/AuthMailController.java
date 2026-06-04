package com.example.PartTrip.controller.signup;

import com.example.PartTrip.dto.signup.EmailSendRequestDto;
import com.example.PartTrip.dto.signup.EmailVerifyRequestDto;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.service.signup.MailService;
import com.example.PartTrip.service.signup.SignUpService;
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
