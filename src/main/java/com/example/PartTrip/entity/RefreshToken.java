package com.example.PartTrip.entity;

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
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt; // refresh token 만료 시간

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate; // refresh token 생성 시간

}
