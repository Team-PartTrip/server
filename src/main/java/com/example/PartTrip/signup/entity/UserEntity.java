package com.example.PartTrip.signup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_manage")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "user_mail", nullable = false)
    private String userMail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "signup_division")
    private String signUpDivision;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "my_country")
    private String myCountry;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    // 여행 취향 설문 완료 여부 (최초 로그인 시 설문 화면 노출 여부 판단용)
    @Column(name = "survey_completed", nullable = false, columnDefinition = "boolean default false")
    private boolean surveyCompleted;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "character_id")
    private String characterId;

    @Column(name = "user_level")
    private Integer userLevel;

    @Column(name = "character_point")
    private Integer characterPoint;

    // 여행 타입 (Func-007-01) — 프로필에 "계획형 모험가" 처럼 표시된다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private com.example.PartTrip.profile.entity.TravelThemeEntity travelTheme;
}