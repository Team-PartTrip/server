package com.example.PartTrip.login.service;

import com.example.PartTrip.global.security.JwtUtil;
import com.example.PartTrip.login.dto.KakaoLoginRequestDto;
import com.example.PartTrip.login.dto.TokenResponseDto;
import com.example.PartTrip.login.entity.RefreshTokenEntity;
import com.example.PartTrip.login.repository.RefreshTokenRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import com.example.PartTrip.signup.support.NickNameGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_URL = "https://kapi.kakao.com/v2/user/me";

    /** 로그인 화면에서 사람이 기다리는 요청이다. 오래 붙잡지 않는다 */
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);

    /** 카카오 계정의 user_id 접두어. 구글은 이메일을 PK 로 쓰지만 카카오는 이메일이 없을 수 있다 */
    private static final String USER_ID_PREFIX = "kakao_";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final NickNameGenerator nickNameGenerator;

    // 앱 로그인(액세스 토큰)에는 필요 없고 웹의 code 교환에만 쓴다. 기본값을
    // 비워 둬서, 키를 아직 안 넣은 사람 서버도 뜨게 한다
    @Value("${kakao.rest-api-key:}")
    private String restApiKey;

    // 카카오 개발자 콘솔에서 "보안" 을 켰을 때만 필요하다
    @Value("${kakao.client-secret:}")
    private String clientSecret;

    private final RestClient restClient = RestClient.builder()
            .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(
                    ClientHttpRequestFactorySettings.defaults()
                            .withConnectTimeout(CONNECT_TIMEOUT)
                            .withReadTimeout(READ_TIMEOUT)))
            .build();

    /** 카카오 로그인: (앱) 액세스 토큰 또는 (웹) auth code → (없으면 가입) → 우리 JWT 발급 */
    @Transactional
    public TokenResponseDto loginWithKakao(KakaoLoginRequestDto dto) {

        String kakaoAccessToken = (dto.getCode() != null && !dto.getCode().isBlank())
                ? exchangeCode(dto.getCode(), dto.getRedirectUri())
                : dto.getAccessToken();

        if (kakaoAccessToken == null || kakaoAccessToken.isBlank()) {
            throw new IllegalArgumentException("카카오 토큰이 필요합니다.");
        }

        JsonNode me = fetchMe(kakaoAccessToken);

        String kakaoId = me.path("id").asText(null);
        if (kakaoId == null || kakaoId.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 카카오 토큰입니다.");
        }

        JsonNode account = me.path("kakao_account");
        UserEntity user = resolveKakaoUser(
                kakaoId,
                verifiedEmail(account),
                account.path("profile").path("nickname").asText(null));

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

    /**
     * 카카오 계정을 우리 회원과 맞춘다.
     *
     * 카카오 아이디로 찾는다. 이메일로 찾지 않는다. 이메일은 사용자가 동의를
     * 안 하면 아예 오지 않고, 카카오에서 바꿀 수도 있다. 그걸 계정 식별에
     * 쓰면 동의를 뺀 사람이 매번 새 계정이 된다.
     */
    UserEntity resolveKakaoUser(String kakaoId, String email, String nickname) {
        String userId = USER_ID_PREFIX + kakaoId;
        return userRepository.findByUserId(userId)
                .orElseGet(() -> createKakaoUser(userId, email, nickname));
    }

    private UserEntity createKakaoUser(String userId, String email, String nickname) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        // 같은 이메일을 쓰는 계정이 이미 있으면 비워 둔다. user_mail 은 unique 라
        // 그대로 넣으면 저장이 깨지고, 이메일만 보고 남의 계정에 붙이는 것도 안 된다.
        // 두 계정을 합칠지는 기획에서 정할 일이다.
        user.setUserMail(email != null && !userRepository.existsByUserMailIgnoreCase(email)
                ? email
                : null);
        // 카카오 로그인은 비밀번호를 쓰지 않지만 컬럼이 NOT NULL 이라 임의값 저장
        user.setUserPwd(passwordEncoder.encode(UUID.randomUUID().toString()));
        // 카카오 닉네임을 우선 쓰되, 이미 쓰이고 있으면 랜덤 접미사를 붙인다
        user.setNickName(nickname != null && !nickname.isBlank()
                ? nickNameGenerator.generateFrom(nickname)
                : nickNameGenerator.generate());
        user.setSignUpDivision("KAKAO");
        user.setMyCountry("KR");
        user.setCreateDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * 확인된 이메일만 받는다.
     *
     * 카카오는 확인 안 된 이메일도 내려준다. 그걸 그대로 계정에 붙이면 남의
     * 이메일을 적어 둔 계정이 우리 쪽에서는 그 사람 계정처럼 보인다.
     */
    static String verifiedEmail(JsonNode account) {
        String email = account.path("email").asText(null);
        if (email == null || email.isBlank()) {
            return null;
        }
        if (!account.path("is_email_valid").asBoolean(false)
                || !account.path("is_email_verified").asBoolean(false)) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    /** [웹] auth code 를 카카오 토큰 엔드포인트에서 액세스 토큰으로 바꾼다 */
    private String exchangeCode(String code, String redirectUri) {
        if (restApiKey == null || restApiKey.isBlank()) {
            throw new IllegalArgumentException(
                    "카카오 REST API 키가 설정되지 않았습니다. (kakao.rest-api-key)");
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", restApiKey);
        form.add("code", code);
        if (redirectUri != null && !redirectUri.isBlank()) {
            form.add("redirect_uri", redirectUri);
        }
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        try {
            JsonNode body = restClient.post()
                    .uri(TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(JsonNode.class);

            String token = body == null ? null : body.path("access_token").asText(null);
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("카카오 인증에 실패했습니다.");
            }
            return token;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            // 카카오가 내려준 사유는 우리 로그에만 남긴다. 그대로 앱에 내보내면
            // 키 설정 문제까지 밖으로 새어 나간다.
            log.warn("카카오 code 교환 실패: {}", e.getMessage());
            throw new IllegalArgumentException("카카오 인증에 실패했습니다.");
        }
    }

    /** 액세스 토큰으로 카카오 사용자 정보를 받는다. 이게 토큰 검증도 겸한다 */
    private JsonNode fetchMe(String kakaoAccessToken) {
        try {
            JsonNode body = restClient.get()
                    .uri(USER_URL)
                    .header("Authorization", "Bearer " + kakaoAccessToken)
                    .retrieve()
                    .body(JsonNode.class);

            if (body == null) {
                throw new IllegalArgumentException("카카오 토큰 검증에 실패했습니다.");
            }
            return body;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.warn("카카오 사용자 조회 실패: {}", e.getMessage());
            throw new IllegalArgumentException("카카오 토큰 검증에 실패했습니다.");
        }
    }
}
