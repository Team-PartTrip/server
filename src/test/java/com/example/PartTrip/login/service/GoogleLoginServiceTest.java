package com.example.PartTrip.login.service;

import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.support.NickNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleLoginServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private NickNameGenerator nickNameGenerator;
    @InjectMocks private GoogleLoginService googleLoginService;

    @Test
    void rejectsDuplicateEmailMatches() {
        String email = "user@example.com";
        when(userRepository.findAllByUserMailIgnoreCaseOrderByUserIdAsc(email))
                .thenReturn(List.of(new UserEntity(), new UserEntity()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> googleLoginService.resolveGoogleUser(email, "Traveler"))
                .withMessage("중복된 이메일 계정으로 Google 로그인할 수 없습니다.");
    }
}
