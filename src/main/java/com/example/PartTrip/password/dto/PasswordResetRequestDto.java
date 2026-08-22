package com.example.PartTrip.password.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {

    // 비밀번호를 변경할 이메일
    private String email;

    // 새 비밀번호
    private String newPassword;

    // 새 비밀번호 재입력
    private String confirmPassword;

}
