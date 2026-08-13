package com.example.PartTrip.infrastructure.security;

// JWT 안에 들어있는 데이터를 꺼낼 수 있는 객체
import io.jsonwebtoken.Claims;

// JWT 생성 및 해석에 사용하는 클래스
import io.jsonwebtoken.Jwts;

// JWT 암호화 방식(HS256)을 사용하기 위한 클래스
import io.jsonwebtoken.SignatureAlgorithm;

// JWT Key 생성용 클래스
import io.jsonwebtoken.security.Keys;

// 스프링이 관리하는 객체(Bean)로 등록
import org.springframework.stereotype.Component;

// 문자열을 바이트로 변환
import java.nio.charset.StandardCharsets;

// JWT 암호화 Key 객체
import java.security.Key;

// 날짜와 시간을 사용하기 위한 클래스
import java.util.Date;

// JwtUtil 객체를 스프링이 자동 생성
@Component
public class JwtUtil {

    // JWT를 암호화하고 검증할 때 사용하는 비밀키
    // 서버만 알고 있어야 함
    private final String SECRET_KEY =
            "parttrip-secret-key-parttrip-secret-key-1234567890";

    // SECRET_KEY를 JWT에서 사용할 Key 객체로 변환
    private final Key key =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );

    // Access Token 유효 시간
    // 1000ms = 1초
    // 60초 = 1분
    // 60분 = 1시간
    private final long ACCESS_TOKEN_TIME = 1000 * 60 * 60;

    // Refresh Token 유효 시간
    // 7일
    private final long REFRESH_TOKEN_TIME = 1000L * 60 * 60 * 24 * 7;

    // Access Token 생성
    public String createAccessToken(String userId, String userMail) {

        // JWT 생성 시작
        return Jwts.builder()

                // 토큰의 대표 사용자 정보
                // 보통 이메일을 넣음
                .setSubject(userMail)

                // 추가 정보 저장
                // userId를 JWT 안에 넣음
                .claim("userId", userId)

                // 토큰 생성 시간
                .setIssuedAt(new Date())

                // 토큰 만료 시간
                // 현재 시간 + 1시간
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + ACCESS_TOKEN_TIME
                        )
                )

                // HS256 방식으로 암호화
                // key를 이용해 서명
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )

                // JWT 문자열 생성
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(String userId, String userMail) {

        return Jwts.builder()

                // 이메일 저장
                .setSubject(userMail)

                // 로그인 아이디 저장
                .claim("userId", userId)

                // 생성 시간
                .setIssuedAt(new Date())

                // 만료 시간
                // 현재 시간 + 7일
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + REFRESH_TOKEN_TIME
                        )
                )

                // 암호화
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )

                // 문자열 생성
                .compact();
    }

    // JWT 안의 데이터 가져오기
    public Claims getClaims(String token) {

        // JWT를 해석
        return Jwts.parser()

                // key로 검증
                .setSigningKey(key)

                // JWT 분석
                .parseClaimsJws(token)

                // 안에 들어있는 데이터 반환
                .getBody();
    }

    // JWT 안의 userId 가져오기
    public String getUserId(String token) {

        // Claims에서 userId 꺼내기
        return getClaims(token)
                .get("userId", String.class);
    }

    // JWT 안의 이메일 가져오기
    public String getUserMail(String token) {

        // subject에 저장된 이메일 반환
        return getClaims(token)
                .getSubject();
    }

    // 토큰이 만료되었는지 확인
    public boolean isExpired(String token) {

        // 토큰 만료시간이 현재시간보다 이전이면 true
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }
}