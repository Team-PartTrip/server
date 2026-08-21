package com.example.PartTrip.global.security;

import com.example.PartTrip.global.config.CorsConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // CorsConfig 객체 가져오기
    private final CorsConfig corsConfig;

    // JwtAuthFilter 객체 가져오기
    private final JwtAuthFilter jwtAuthFilter;

    // 생성자를 통해 객체 주입
    public SecurityConfig(
            CorsConfig corsConfig,
            JwtAuthFilter jwtAuthFilter
    ) {
        this.corsConfig = corsConfig;
        this.jwtAuthFilter = jwtAuthFilter;
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

                        // Swagger 접속 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 회원가입 허용
                        .requestMatchers("/api/auth/signup").permitAll()

                        // 로그인 허용
                        .requestMatchers("/api/auth/login").permitAll()

                        // 이메일 인증 관련 API 허용
                        .requestMatchers("/api/auth/**").permitAll()

                        // 업로드된 게시글 이미지는 인증 없이 조회 가능해야 함(앱 Image 컴포넌트가 토큰을 안 붙임)
                        .requestMatchers("/images/**").permitAll()

                        // 나머지 요청은 로그인 필요
                        .anyRequest().authenticated()
                )

                // Spring 기본 로그인 페이지 비활성화
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // JwtAuthFilter를 UsernamePasswordAuthenticationFilter보다 먼저 실행
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

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