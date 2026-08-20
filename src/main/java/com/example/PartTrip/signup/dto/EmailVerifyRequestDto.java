package com.example.PartTrip.signup.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequestDto {

    private String email;
    private String code;

}
