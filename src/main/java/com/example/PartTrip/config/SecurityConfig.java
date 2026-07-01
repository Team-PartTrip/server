package com.example.PartTrip.config;

import com.example.PartTrip.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtAuthFilter jwtAuthFilter;

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
                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // CORS 적용
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

                // URL 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // Swagger 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 인증 API 허용
                        .requestMatchers("/api/auth/**").permitAll()

                        // Guide Camera API 전체 허용 (테스트용)
                        .requestMatchers("/api/guide-camera/**").permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // 기본 로그인 비활성화
                .formLogin(form -> form.disable())

                // Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // JWT 필터 등록
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}