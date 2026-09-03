package com.example.PartTrip.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
public class RefreshTokenEntity {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    // 로그인한 사용자 아이디
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    // 실제 Refresh Token 문자열
    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;

    // Refresh Token 만료 시간
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    // 갱신 직전에 쓰던 토큰. 응답이 유실되면 앱은 옛 토큰을 그대로 들고 있어
    // 다시 보내는데, 그때 쫓아내지 않으려고 짧은 동안만 함께 받아준다.
    @Column(name = "previous_token", length = 500)
    private String previousToken;

    // previousToken 을 언제까지 받아줄지
    @Column(name = "previous_valid_until")
    private LocalDateTime previousValidUntil;

    // Refresh Token 생성 시간
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}