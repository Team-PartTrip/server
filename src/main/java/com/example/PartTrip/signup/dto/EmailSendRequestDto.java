package com.example.PartTrip.signup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSendRequestDto {

    // 인증 보낼 때 받는 이메일
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

}
