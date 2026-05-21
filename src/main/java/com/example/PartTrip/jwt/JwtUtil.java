package com.example.PartTrip.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "parttrip-secret-key-parttrip-secret-key";
    private final long ACCESS_TOKEN_TIME = 1000 * 60 * 60;
    private final long REFRESH_TOKEN_TIME = 1000L * 60 * 60 * 24 * 7;
}
