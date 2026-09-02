package com.example.PartTrip.login.service;

import com.example.PartTrip.login.dto.GoogleLoginRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.entity.RefreshTokenEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.PartTrip.signup.support.NickNameGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final NickNameGenerator nickNameGenerator;

    // 웹/앱이 공통으로 쓰는 웹 클라이언트 ID
    @Value("${google.client-id}")
    private String googleClientId;

    // 웹 auth code 교환용 client secret
    @Value("${google.client-secret:}")
    private String googleClientSecret;

    private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";

    // 구글 로그인: (앱) idToken 검증 또는 (웹) auth code 교환 → (없으면 가입) → JWT 발급
    @Transactional
    public TokenResponseDto loginWithGoogle(GoogleLoginRequestDto dto) {

        GoogleIdToken.Payload payload;
        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            // 웹: auth code 를 토큰으로 교환
            payload = exchangeCode(dto.getCode());
        } else {
            // 앱: idToken 직접 검증
            payload = verify(dto.getIdToken());
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (email == null) {
            throw new IllegalArgumentException("유효하지 않은 Google 토큰입니다.");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        // 이메일로 회원 조회, 없으면 구글 계정으로 자동 가입
        UserEntity user = resolveGoogleUser(normalizedEmail, name);

        // 토큰 발급 (이메일 로그인과 동일)
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getUserMail());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getUserMail());

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(new RefreshTokenEntity());
        tokenEntity.setUserId(user.getUserId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiredAt(LocalDateTime.now().plusDays(7));
        tokenEntity.setPreviousToken(null);
        tokenEntity.setPreviousValidUntil(null);
        tokenEntity.setCreateDate(LocalDateTime.now());
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    UserEntity resolveGoogleUser(String normalizedEmail, String name) {
        List<UserEntity> users = userRepository
                .findAllByUserMailIgnoreCaseOrderByUserIdAsc(normalizedEmail);
        if (users.size() > 1) {
            throw new IllegalArgumentException("중복된 이메일 계정으로 Google 로그인할 수 없습니다.");
        }
        return users.isEmpty()
                ? createGoogleUser(normalizedEmail, name)
                : users.get(0);
    }

    // [웹] auth code 를 Google 토큰 엔드포인트에서 교환하여 idToken 획득
    private GoogleIdToken.Payload exchangeCode(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    TOKEN_ENDPOINT,
                    googleClientId,
                    googleClientSecret,
                    code,
                    "postmessage") // 웹 popup(auth-code) 흐름의 redirect_uri
                    .execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            if (idToken == null) {
                throw new IllegalArgumentException("유효하지 않은 Google 토큰입니다.");
            }
            return idToken.getPayload();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Google 인증에 실패했습니다.");
        }
    }

    // [앱] idToken 서명/만료/audience 검증
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
        // 구글 이름을 우선 쓰되, 이미 쓰이고 있으면 랜덤 접미사를 붙인다
        user.setNickName(nickNameGenerator.generateFrom(
                name != null ? name : email.split("@")[0]));
        user.setSignUpDivision("GOOGLE");
        user.setMyCountry("KR");
        user.setCreateDate(LocalDateTime.now());
        return userRepository.save(user);
    }
}
