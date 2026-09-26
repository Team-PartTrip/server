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

    @Column(name = "user_mail", unique = true)
    private String userMail;

    @Column(name = "signup_division")
    private String signUpDivision;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "create_date")
    private LocalDateTime createDate;


    @Column(name = "img_url")
    private String imgUrl;

}
