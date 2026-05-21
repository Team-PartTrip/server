package com.example.PartTrip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // CorsConfig 객체 가져오기
    private final CorsConfig corsConfig;

    // 생성자를 통해 CorsConfig 주입
    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                // csrf 보안 기능 비활성화
                // JWT 방식에서는 보통 사용하지 않음
                .csrf(csrf -> csrf.disable())

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

                // URL 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // /api/auth 로 시작하는 요청은 모두 허용
                        .requestMatchers("/api/auth/**").permitAll()

                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // Spring 기본 로그인 페이지 비활성화
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable());

        // Security 설정 완료 후 반환
        return http.build();
    }

    @Bean
    // 비밀번호 암호화 객체 Bean 등록
    public PasswordEncoder passwordEncoder() {

        // BCrypt 방식 암호화 사용
        return new BCryptPasswordEncoder();
    }
}