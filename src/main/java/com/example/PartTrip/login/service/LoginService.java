package com.example.PartTrip.login.service;

import com.example.PartTrip.login.dto.LoginRequestDto;
import com.example.PartTrip.login.dto.LogoutRequestDto;
import com.example.PartTrip.login.dto.RefreshRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.entity.RefreshTokenEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인 처리
    public TokenResponseDto login(LoginRequestDto dto) {

        // 1. 사용자가 입력한 아이디로 DB에서 회원 찾기
        UserEntity user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getUserPwd(), user.getUserPwd())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // AccessToken 생성
        String accessToken = jwtUtil.createAccessToken(
                user.getUserId(),
                user.getUserMail()
        );

        // RefreshToken 생성
        String refreshToken = jwtUtil.createRefreshToken(
                user.getUserId(),
                user.getUserMail()
        );

        // 사용자의 Refrsh token이 DB에 있는지 확인
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(new RefreshTokenEntity());

        // Refresh token 정보 저장
        tokenEntity.setUserId(user.getUserId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        tokenEntity.setCreateDate(LocalDateTime.now());

        refreshTokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto refresh(RefreshRequestDto dto) {

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByRefreshToken(dto.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Refresh Token 입니다."));

        if (tokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다. 다시 로그인 해주세요.");
        }

        if (jwtUtil.isExpired(tokenEntity.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다. 다시 로그인 해주세요.");
        }

        String userId = jwtUtil.getUserId(dto.getRefreshToken());
        String userMail = jwtUtil.getUserMail(dto.getRefreshToken());

        String newAccessToken = jwtUtil.createAccessToken(userId, userMail);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        return new TokenResponseDto(newAccessToken, dto.getRefreshToken());
    }

    // 로그아웃
    public String logout(LogoutRequestDto dto) {

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByRefreshToken(dto.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Refresh Token 입니다."));


        // Refresh Token 삭제
        refreshTokenRepository.delete(tokenEntity);

        return "로그아웃 완료";
    }


}
