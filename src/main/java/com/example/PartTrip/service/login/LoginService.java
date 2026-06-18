package com.example.PartTrip.service.login;

import com.example.PartTrip.dto.login.LoginRequestDto;
import com.example.PartTrip.dto.login.RefreshRequestDto;
import com.example.PartTrip.dto.login.TokenResponseDto;
import com.example.PartTrip.entity.login.RefreshTokenEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.jwt.JwtUtil;
import com.example.PartTrip.repository.login.RefreshTokenRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepositor;
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
        RefreshTokenEntity tokenEntity = refreshTokenRepositor.findByUserId(user.getUserId())
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

        return new TokenResponseDto(newAccessToken, dto.getRefreshToken());
    }
}
