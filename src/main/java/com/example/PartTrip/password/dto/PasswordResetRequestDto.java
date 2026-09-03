package com.example.PartTrip.password.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {

    // 비밀번호를 변경할 이메일
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    // 새 비밀번호
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;

    // 새 비밀번호 재입력
    @NotBlank(message = "새 비밀번호를 다시 입력해주세요.")
    private String confirmPassword;

    // 인증을 마칠 때 받은 일회용 토큰
    @NotBlank(message = "이메일 인증을 먼저 완료해주세요.")
    private String resetToken;

}
