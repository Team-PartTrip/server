package com.example.PartTrip.login.service;

import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.support.NickNameGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock private UserRepository userRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private NickNameGenerator nickNameGenerator;
    @InjectMocks private KakaoLoginService kakaoLoginService;

    private static JsonNode account(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void takesOnlyVerifiedEmail() {
        assertThat(KakaoLoginService.verifiedEmail(account("""
                {"email":"A@Example.com","is_email_valid":true,"is_email_verified":true}""")))
                .isEqualTo("a@example.com");

        // 확인 안 된 이메일은 남의 것일 수 있다
        assertThat(KakaoLoginService.verifiedEmail(account("""
                {"email":"a@example.com","is_email_valid":true,"is_email_verified":false}""")))
                .isNull();

        // 이메일 동의를 안 하면 아예 오지 않는다
        assertThat(KakaoLoginService.verifiedEmail(account("{}"))).isNull();
    }

    @Test
    void reusesAccountFoundByKakaoId() {
        UserEntity existing = new UserEntity();
        existing.setUserId("kakao_1234");
        when(userRepository.findByUserId("kakao_1234")).thenReturn(Optional.of(existing));

        UserEntity found = kakaoLoginService.resolveKakaoUser("1234", "a@example.com", "홍길동");

        assertThat(found).isSameAs(existing);
        verify(userRepository, never()).save(any());
    }

    @Test
    void leavesEmailEmptyWhenAlreadyTaken() {
        stubNewUser();
        when(userRepository.existsByUserMailIgnoreCase("a@example.com")).thenReturn(true);

        UserEntity created = kakaoLoginService.resolveKakaoUser("1234", "a@example.com", "홍길동");

        // 이메일만 보고 남의 계정에 붙이지 않는다. unique 제약도 지킨다
        assertThat(created.getUserMail()).isNull();
        assertThat(created.getUserId()).isEqualTo("kakao_1234");
        assertThat(created.getSignUpDivision()).isEqualTo("KAKAO");
    }

    @Test
    void keepsEmailWhenFree() {
        stubNewUser();
        when(userRepository.existsByUserMailIgnoreCase("a@example.com")).thenReturn(false);

        UserEntity created = kakaoLoginService.resolveKakaoUser("1234", "a@example.com", "홍길동");

        assertThat(created.getUserMail()).isEqualTo("a@example.com");
    }

    @Test
    void fallsBackToRandomNickNameWithoutKakaoNickName() {
        when(userRepository.findByUserId("kakao_1234")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(nickNameGenerator.generate()).thenReturn("여행자1");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(call -> call.getArgument(0));

        UserEntity created = kakaoLoginService.resolveKakaoUser("1234", null, null);

        assertThat(created.getNickName()).isEqualTo("여행자1");
        assertThat(created.getUserMail()).isNull();
    }

    private void stubNewUser() {
        when(userRepository.findByUserId("kakao_1234")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(nickNameGenerator.generateFrom("홍길동")).thenReturn("홍길동7");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(call -> call.getArgument(0));
    }
}
