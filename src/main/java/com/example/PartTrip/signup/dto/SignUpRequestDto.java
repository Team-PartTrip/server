package com.example.PartTrip.signup.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    private String userId;
    private String userPwd;
    private String userMail;
    private String signUpDivision;
    private String myCountry;
}