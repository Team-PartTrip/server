package com.example.PartTrip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    // CORS 설정 제공 객체를 만들어 반환하는 메서드
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 설정 객체
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 프론트 포트
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173"
        ));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // 모든 헤더 허용
        config.setAllowedHeaders(List.of("*"));
        // 쿠키/인증 허용
        config.setAllowCredentials(true);

        // 모든 URL마다 CORS 규칙 적용을 저장하는 객체
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // (경로, 적용할 규칙) : 모든 URL에 config 규칙 허용
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}