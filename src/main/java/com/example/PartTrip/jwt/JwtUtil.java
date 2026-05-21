package com.example.PartTrip.jwt;

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
                .setSubject(userMail)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
