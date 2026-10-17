package com.example.PartTrip.guardian.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** 시니어가 만든 보호자 초대 코드. 한 번 쓰면 지운다 */
@Entity
@Table(name = "guardian_invite")
@Getter
@NoArgsConstructor
public class GuardianInviteEntity {

    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "senior_user_id", nullable = false)
    private String seniorUserId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public GuardianInviteEntity(String code, String seniorUserId, LocalDateTime expiresAt) {
        this.code = code;
        this.seniorUserId = seniorUserId;
        this.expiresAt = expiresAt;
    }
}
