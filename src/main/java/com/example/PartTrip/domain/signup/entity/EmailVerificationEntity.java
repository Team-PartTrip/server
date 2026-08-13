package com.example.PartTrip.domain.signup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Getter
@Setter
@NoArgsConstructor
public class EmailVerificationEntity {

    @Id
    // 사용자 이메일(PK)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 인증번호
    @Column(name = "code", nullable = false)
    private String code;

    // 인증 완료 여부
    @Column(name = "verified", nullable = false)
    private boolean verified;

    // 인증번호 만료 시간
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

}
