package com.example.PartTrip.signup.service;

import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private EmailVerificationRepository emailVerificationRepository;
    @InjectMocks
    private MailService mailService;

    @Test
    void storesNewVerificationCodeAsUnverifiedWithNormalizedEmail() {
        mailService.sendCode("  090626IHS@DGSW.HS.KR  ");

        ArgumentCaptor<EmailVerificationEntity> captor =
                ArgumentCaptor.forClass(EmailVerificationEntity.class);
        verify(emailVerificationRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("090626ihs@dgsw.hs.kr");
        assertThat(captor.getValue().getCode()).matches("[1-9][0-9]{5}");
        assertThat(captor.getValue().isVerified()).isFalse();
    }

    // 인증만 보고 만료를 안 보면, 한 번 인증한 메일로 언제든 비밀번호를
    // 바꿀 수 있다. 비밀번호 찾기가 이 값 하나로 통과된다.
    @Test
    void 만료된_인증은_인증하지_않은_것으로_본다() {
        EmailVerificationEntity expired = new EmailVerificationEntity();
        expired.setEmail("a@b.com");
        expired.setVerified(true);
        expired.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        when(emailVerificationRepository.findById("a@b.com")).thenReturn(Optional.of(expired));

        assertThat(mailService.isVerified("a@b.com")).isFalse();
    }

    @Test
    void 유효한_인증은_통과한다() {
        EmailVerificationEntity alive = new EmailVerificationEntity();
        alive.setEmail("a@b.com");
        alive.setVerified(true);
        alive.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        when(emailVerificationRepository.findById("a@b.com")).thenReturn(Optional.of(alive));

        assertThat(mailService.isVerified("a@b.com")).isTrue();
    }

    // 코드 발급 5분에 묶여 있으면 4분 50초에 인증한 사람은 10초 안에
    // 비밀번호를 바꿔야 한다. 인증 성공 시점부터 다시 센다.
    @Test
    void 인증에_성공하면_만료를_다시_잡는다() {
        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setEmail("a@b.com");
        entity.setCode("123456");
        entity.setExpiredAt(LocalDateTime.now().plusSeconds(10));
        when(emailVerificationRepository.findById("a@b.com")).thenReturn(Optional.of(entity));

        EmailVerifyRequestDto dto = new EmailVerifyRequestDto();
        dto.setEmail("a@b.com");
        dto.setCode("123456");
        mailService.verifyCode(dto);

        assertThat(entity.isVerified()).isTrue();
        assertThat(entity.getExpiredAt()).isAfter(LocalDateTime.now().plusMinutes(5));
    }
}
