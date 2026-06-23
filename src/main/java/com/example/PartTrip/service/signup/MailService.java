package com.example.PartTrip.service.signup;

import com.example.PartTrip.dto.signup.EmailVerifyRequestDto;
import com.example.PartTrip.entity.signup.EmailVerificationEntity;
import com.example.PartTrip.repository.signup.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
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
        entity.setVerified(false);
        // 인증 만료 시간(현재 시간에서 5분 더한 시간)
        entity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        emailVerificationRepository.save(entity);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("[PartTrip] 이메일 인증번호]");
        message.setText("인증번호는 " + code + " + 입니다. 5분 안에 입력해주세요");

        mailSender.send(message);
    }


    // 이메일로 받은 인증번호를 입력했을 때 실행됨
    public void verifyCode(EmailVerifyRequestDto dto) {

        EmailVerificationEntity entity = emailVerificationRepository.findById(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("인증번호를 먼저 요청해주세요."));

        // DB에 저장된 만료 시간을 가져오고 현재 시간을 가져와서 비교
        if (entity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }

        // 사용자가 입력한 인증 코드와 DB에 저장된 인증 코드가 다르다면 예외 출력
        if (!entity.getCode().equals(dto.getCode())) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        entity.setVerified(true);
        emailVerificationRepository.save(entity);

    }

    public boolean isVerified(String email) {
        // DB에서 해당 이메일 인증 정보를 찾음
        return emailVerificationRepository.findById(email)
                // 찾으면 그 Entity의 verified값을 꺼냄
                .map(EmailVerificationEntity::isVerified)
                .orElse(false);
    }

}
