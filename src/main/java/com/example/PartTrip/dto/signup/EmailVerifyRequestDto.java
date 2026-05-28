package com.example.PartTrip.dto.signup;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequestDto {

    private String email;
    private String code;

}
