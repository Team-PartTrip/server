package com.example.PartTrip.domain.login.entity;

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

    // Refresh Token 생성 시간
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}