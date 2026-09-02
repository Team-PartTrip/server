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

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "my_country")
    private String myCountry;

    @Column(name = "create_date")
    private LocalDateTime createDate;


    @Column(name = "img_url")
    private String imgUrl;

    // 여행 타입은 기능에서 뺐다. user.theme_id 컬럼과 travel_theme 표는
    // 남아 있지만 매핑하지 않는다. ddl-auto=update 는 컬럼을 지우지 않는다.
}