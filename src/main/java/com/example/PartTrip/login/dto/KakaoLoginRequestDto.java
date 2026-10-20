package com.example.PartTrip.login.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginRequestDto {

    private String accessToken;

    private String code;

    private String redirectUri;
}
