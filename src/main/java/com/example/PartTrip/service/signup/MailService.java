package com.example.PartTrip.service.signup;

import com.example.PartTrip.entity.signup.EmailVerificationEntity;
import com.example.PartTrip.repository.signup.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendCode(String email) {

        // 숫자를 랜덤으로 뽑아 인증 코드 생성 + 인증 코드를 문자열로 변환
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        EmailVerificationEntity entity = new EmailVerificationEntity();
    }

}
