package com.example.PartTrip.entity;

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
public class UserManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_pwd", nullable = false, unique = true)
    private String userPwd;

    @Column(name = "user_mail", nullable = false, unique = true)
    private String userMail;

    @Column(name = "signup_division")
    private String signUpDivision;

    @Column(name = "phn_number")
    private String phnNumber;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "my_country")
    private String myCountry;

    @Column(name = "travel_type")
    private String travelType;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}