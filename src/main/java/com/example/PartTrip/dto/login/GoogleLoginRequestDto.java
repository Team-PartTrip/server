package com.example.PartTrip.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequestDto {

    // 앱에서 Google 로그인 후 받은 idToken
    private String idToken;
}
