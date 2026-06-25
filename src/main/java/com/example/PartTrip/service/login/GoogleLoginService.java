package com.example.PartTrip.service.login;

import com.example.PartTrip.dto.login.GoogleLoginRequestDto;
import com.example.PartTrip.dto.login.TokenResponseDto;
import com.example.PartTrip.entity.login.RefreshTokenEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.jwt.JwtUtil;
import com.example.PartTrip.repository.login.RefreshTokenRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // application.properties 의 google.client-id (= 웹 클라이언트 ID)
    @Value("${google.client-id}")
    private String googleClientId;

    // 구글 로그인 처리: idToken 검증 → (없으면 가입) → JWT 발급
    public TokenResponseDto loginWithGoogle(GoogleLoginRequestDto dto) {

        // 1. Google idToken 검증
        GoogleIdToken.Payload payload = verify(dto.getIdToken());

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (email == null) {
            throw new IllegalArgumentException("유효하지 않은 Google 토큰입니다.");
        }

        // 2. 이메일로 회원 조회, 없으면 구글 계정으로 자동 가입
        UserEntity user = userRepository.findByUserMail(email)
                .orElseGet(() -> createGoogleUser(email, name));

        // 3. 토큰 발급 (이메일 로그인과 동일)
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUserMail());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getUserMail());

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(new RefreshTokenEntity());
        tokenEntity.setUserId(user.getUserId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        tokenEntity.setCreateDate(LocalDateTime.now());
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    // Google 서버 공개키로 idToken 서명/만료/audience 검증
    private GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("유효하지 않은 Google 토큰입니다.");
            }
            return idToken.getPayload();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Google 토큰 검증에 실패했습니다.");
        }
    }

    // 구글 신규 회원 생성
    private UserEntity createGoogleUser(String email, String name) {
        UserEntity user = new UserEntity();
        user.setUserId(email);                 // PK 로 이메일 사용
        user.setUserMail(email);
        // 구글 로그인은 비밀번호를 쓰지 않지만 컬럼이 NOT NULL 이라 임의값 저장
        user.setUserPwd(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setNickName(name != null ? name : email.split("@")[0]);
        user.setSignUpDivision("GOOGLE");
        user.setMyCountry("KR");
        user.setCreateDate(LocalDateTime.now());
        return userRepository.save(user);
    }
}
