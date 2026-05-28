package com.example.PartTrip.controller.signup;

import com.example.PartTrip.dto.signup.EmailSendRequestDto;
import com.example.PartTrip.service.signup.MailService;
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

    @PostMapping("/send")
    public String sendCode(@RequestBody EmailSendRequestDto dto) {

        mailService.sendCode(dto.getEmail());

        return "인증번호가 전송되었습니다.";

    }

}
