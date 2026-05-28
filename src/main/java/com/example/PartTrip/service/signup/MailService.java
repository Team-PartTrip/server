package com.example.PartTrip.service.signup;

import com.example.PartTrip.entity.signup.EmailVerificationEntity;
import com.example.PartTrip.repository.signup.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // 사용자가 이력한 이메일을 Entity에 넣음
        entity.setEmail(email);
        // 인증번호 6자리도 Entity에 넣음
        entity.setCode(code);
        // 사용자가 인증번호를 입력해서 검증된 상태가 아니므로 false로 저장
        entity.setVerified(true);
        // 인증 만료 시간(현재 시간에서 5분 더한 시간)
        entity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        emailVerificationRepository.save(entity);
    }

}
