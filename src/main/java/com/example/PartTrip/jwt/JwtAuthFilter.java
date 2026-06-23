package com.example.PartTrip.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component // 스프링이 자동으로 관리하는 객체 등록
@RequiredArgsConstructor
// OncePerRequestFilter : 요청 1개당 필터 1번 실행
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 현재 요청 URL 가져오기
        String requestURI = request.getRequestURI();

        // 회원가입, 로그인, 이메일 인증, Swagger 등
        // 인증이 필요 없는 API는 필터 검사 없이 통과
        if (
                requestURI.startsWith("/api/auth/")
                        || requestURI.startsWith("/swagger-ui")
                        || requestURI.startsWith("/v3/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나
        // Bearer 토큰 형식이 아니면
        // 다음 필터로 넘김
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 제거 후 실제 토큰만 추출
        String token = authorizationHeader.substring(7);

        try {

            // 토큰이 만료되었는지 확인
            if (jwtUtil.isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Token이 만료되었습니다.");
                return;
            }

            // 토큰 안에서 userId 꺼냄
            String userId = jwtUtil.getUserId(token);

            // Spring Security에 인증 정보 등록
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            // 토큰이 위조되었거나
            // 형식이 잘못되었을 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않은 Access Token입니다.");
            return;
        }

        // 토큰이 정상이라면 다음 필터 또는 컨트롤러로 넘김
        filterChain.doFilter(request, response);

    }
}