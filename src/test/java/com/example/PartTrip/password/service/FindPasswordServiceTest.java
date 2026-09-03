package com.example.PartTrip.password.service;

import com.example.PartTrip.password.dto.PasswordResetRequestDto;
import com.example.PartTrip.signup.dto.EmailVerifyRequestDto;
import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.EmailVerificationRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// 인증 상태를 이메일에만 묶어두면, 피해자가 인증을 마친 사이에 이메일만 아는
// 사람도 비밀번호를 바꿀 수 있다. 인증한 쪽에만 준 토큰을 다시 확인한다.
@ExtendWith(MockitoExtension.class)
class FindPasswordServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private MailService mailService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailVerificationRepository emailVerificationRepository;
    @InjectMocks private FindPasswordService findPasswordService;

    @Test
    void 토큰이_다르면_비밀번호를_바꾸지_않는다() {
        given("진짜-토큰");

        assertThatThrownBy(() -> findPasswordService.resetPassword(request("훔친-토큰")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 인증");

        verify(userRepository, never()).save(any());
    }

    @Test
    void 인증만_하고_토큰이_없으면_바꾸지_않는다() {
        given(null);

        assertThatThrownBy(() -> findPasswordService.resetPassword(request("아무거나")))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void 토큰이_같으면_바꾼다() {
        given("진짜-토큰");
        when(passwordEncoder.encode("new1234!")).thenReturn("encoded");

        findPasswordService.resetPassword(request("진짜-토큰"));

        verify(userRepository).save(any(UserEntity.class));
        verify(emailVerificationRepository).deleteById("a@b.com");
    }

    // 원문을 그대로 저장하면 DB 를 읽는 사람이 곧바로 쓸 수 있다
    @Test
    void 토큰은_해시로_저장한다() {
        EmailVerificationEntity verification = new EmailVerificationEntity();
        verification.setEmail("a@b.com");
        when(emailVerificationRepository.findById("a@b.com"))
                .thenReturn(Optional.of(verification));
        EmailVerifyRequestDto dto = new EmailVerifyRequestDto();
        dto.setEmail("a@b.com");
        dto.setCode("123456");

        String issued = findPasswordService.verifyResetCode(dto);

        assertThat(verification.getResetToken())
                .isNotEqualTo(issued)
                .isEqualTo(sha256(issued));
    }

    private void given(String storedToken) {
        when(mailService.isVerified("a@b.com")).thenReturn(true);
        EmailVerificationEntity verification = new EmailVerificationEntity();
        verification.setEmail("a@b.com");
        verification.setVerified(true);
        verification.setResetToken(storedToken == null ? null : sha256(storedToken));
        when(emailVerificationRepository.findByEmailForUpdate("a@b.com"))
                .thenReturn(Optional.of(verification));
        UserEntity user = new UserEntity();
        user.setUserId("someone");
        user.setUserMail("a@b.com");
        lenient().when(userRepository.findAllByUserMailIgnoreCaseOrderByUserIdAsc("a@b.com"))
                .thenReturn(List.of(user));
    }

    // 서비스가 해시로 저장하므로 fixture 도 같은 방식으로 만든다
    private static String sha256(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private PasswordResetRequestDto request(String token) {
        PasswordResetRequestDto dto = new PasswordResetRequestDto();
        dto.setEmail("a@b.com");
        dto.setNewPassword("new1234!");
        dto.setConfirmPassword("new1234!");
        dto.setResetToken(token);
        return dto;
    }
}
