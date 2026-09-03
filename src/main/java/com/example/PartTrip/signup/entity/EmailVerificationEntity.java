package com.example.PartTrip.signup.entity;

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

    /**
     * 비밀번호 재설정용 일회용 토큰.
     *
     * 인증 상태를 이메일에만 묶어두면, 피해자가 인증을 마친 사이에 이메일만
     * 아는 사람이 재설정을 호출할 수 있다. 인증에 성공한 쪽에만 이 값을
     * 돌려주고 재설정 때 다시 받아서 같은 사람인지 확인한다.
     */
    @Column(name = "reset_token")
    private String resetToken;

}
