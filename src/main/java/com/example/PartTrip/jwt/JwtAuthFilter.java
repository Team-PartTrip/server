package com.example.PartTrip.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

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

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않은 Access Token입니다.");
            return;
        }

        // 토큰이 정상이라면 다음 필터 또는 컨트롤러로 넘김
        filterChain.doFilter(request, response);

    }
}
