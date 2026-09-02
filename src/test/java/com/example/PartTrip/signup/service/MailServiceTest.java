package com.example.PartTrip.signup.service;

import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
}
