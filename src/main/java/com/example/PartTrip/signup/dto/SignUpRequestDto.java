package com.example.PartTrip.signup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    // 아이디: 6~20자, 영문 소문자 + 숫자
    // 특수문자를 허용하지 않는 이유 - #, &, ?, % 가 URL 경로·쿼리에서 잘리거나 인코딩과 충돌함
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(
            regexp = "^[a-z0-9]{6,20}$",
            message = "아이디는 6~20자의 영문 소문자와 숫자만 사용할 수 있습니다."
    )
    private String userId;

    // 비밀번호: 8~64자, 영문 / 숫자 / 특수문자 중 2종 이상 조합
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?:(?=.*[A-Za-z])(?=.*\\d)"
                    + "|(?=.*[A-Za-z])(?=.*[^A-Za-z0-9])"
                    + "|(?=.*\\d)(?=.*[^A-Za-z0-9])).{8,64}$",
            message = "비밀번호는 8~64자이며 영문·숫자·특수문자 중 2종 이상을 조합해야 합니다."
    )
    private String userPwd;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String userMail;

    // 전화번호·국적은 기능명세서에서 빠졌다. 회원가입에서 받지 않는다.
    // user_manage 의 phone_number · my_country 컬럼은 남아 있지만
    // 여기서 채우지 않는다(ddl-auto=update 는 컬럼을 지우지 않는다).

    private String signUpDivision;
}
