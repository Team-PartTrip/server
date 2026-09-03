package com.example.PartTrip.signup.service;

import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    /** 인증을 마친 뒤 비밀번호를 바꿀 수 있는 시간(분) */
    private static final int VERIFIED_TTL_MINUTES = 10;

    @Transactional
    public void sendCode(String email) {

        String normalizedEmail = normalizeEmail(email);

        // 숫자를 랜덤으로 뽑아 인증 코드 생성 + 인증 코드를 문자열로 변환
        String code = String.valueOf(SECURE_RANDOM.nextInt(900000) + 100000);

        EmailVerificationEntity entity = new EmailVerificationEntity();
        // 사용자가 이력한 이메일을 Entity에 넣음
        entity.setEmail(normalizedEmail);
        // 인증번호 6자리도 Entity에 넣음
        entity.setCode(code);
        // 사용자가 인증번호를 입력해서 검증된 상태가 아니므로 false로 저장
        entity.setVerified(false);
        // 인증 만료 시간(현재 시간에서 5분 더한 시간)
        entity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        emailVerificationRepository.save(entity);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(normalizedEmail);
        message.setSubject("[PartTrip] 이메일 인증번호]");
        message.setText("인증번호는 " + code + " + 입니다. 5분 안에 입력해주세요");

        mailSender.send(message);
    }


    // 이메일로 받은 인증번호를 입력했을 때 실행됨
    @Transactional
    public void verifyCode(EmailVerifyRequestDto dto) {

        EmailVerificationEntity entity = emailVerificationRepository
                .findById(normalizeEmail(dto.getEmail()))
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
        // 인증에 성공하면 만료 시각을 다시 잡는다.
        //
        // 그냥 두면 인증 상태가 코드 발급 5분에 묶인다. 4분 50초에 인증한
        // 사람은 비밀번호를 바꿀 시간이 10초뿐이다. 반대로 만료를 아예 안
        // 보면 인증 기록이 영원히 살아 있어, 한 번 인증한 메일로 언제든
        // 비밀번호를 바꿀 수 있다.
        entity.setExpiredAt(LocalDateTime.now().plusMinutes(VERIFIED_TTL_MINUTES));
        emailVerificationRepository.save(entity);

    }

    /** 인증을 마쳤고 아직 유효한가. 만료된 인증은 안 한 것으로 본다 */
    @Transactional(readOnly = true)
    public boolean isVerified(String email) {
        return emailVerificationRepository.findById(normalizeEmail(email))
                .map(entity -> entity.isVerified()
                        && entity.getExpiredAt() != null
                        && entity.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

}
