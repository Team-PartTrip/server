package com.example.PartTrip.service.login;

import com.example.PartTrip.dto.login.LoginRequestDto;
import com.example.PartTrip.dto.login.TokenResponseDto;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.jwt.JwtUtil;
import com.example.PartTrip.repository.login.RefreshTokenRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepositor;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인 처리
    public TokenResponseDto login(LoginRequestDto dto) {

        // 1. 사용자가 입력한 아이디로 DB에서 회원 찾기
        UserEntity user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
    }
}
