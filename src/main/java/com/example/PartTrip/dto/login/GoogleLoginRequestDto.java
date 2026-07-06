package com.example.PartTrip.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequestDto {

    // 앱(네이티브)에서 보내는 Google idToken
    private String idToken;

    // 웹에서 보내는 Google auth code
    private String code;
}
