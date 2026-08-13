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
@Table(name = "pending_signup")
@Getter
@Setter
@NoArgsConstructor
public class PendingSignUpEntity {

    @Id
    @Column(name = "user_mail", nullable = false)
    private String userMail;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "signup_division")
    private String signupDivision;

    @Column(name = "my_country")
    private String myCountry;

    // 임시 회원가입 만료 시간(10분)
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;


}
