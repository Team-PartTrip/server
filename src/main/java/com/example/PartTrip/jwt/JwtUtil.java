package com.example.PartTrip.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "parttrip-secret-key-parttrip-secret-key";
    private final long ACCESS_TOKEN_TIME = 1000 * 60 * 60;
    private final long REFRESH_TOKEN_TIME = 1000L * 60 * 60 * 24 * 7;

    public String createAccessToken(Long userId, String userMail) {
        return Jwts.builder()
                // 토큰의 대표 사용자 정보를 userMail로 설정
                .setSubject(userMail)
                // 토큰을 보고 식별이 가능하게 식별값을 넣음
                .claim("userId", userId)
                // 토큰 생성 시간
                .setIssuedAt(new Date())
                // 토큰 만료 시간
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                // 토큰 검사(HS256 방식을 사용)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 문자열로 압축
                .compact();
    }

    public String createRefreshToken(Long userId, String userMail) {
        return Jwts.builder()
                .setSubject(userMail)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims getClaims(String token) {
        // 토큰을 해석하는 객체 생성
        return Jwts.parser()
                // 토큰을 검증하기 위한 시크릿 키
                .setSigningKey(SECRET_KEY)
                // 토큰 검사
                .parseClaimsJws(token)
                .getBody();
    }
}
