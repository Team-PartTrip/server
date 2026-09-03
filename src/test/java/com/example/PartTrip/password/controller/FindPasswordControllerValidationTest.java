package com.example.PartTrip.password.controller;

import com.example.PartTrip.password.dto.PasswordResetRequestDto;
import com.example.PartTrip.signup.dto.EmailSendRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class FindPasswordControllerValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsBlankEmailsInPasswordRequests() {
        EmailSendRequestDto sendRequest = new EmailSendRequestDto();
        sendRequest.setEmail("  ");
        PasswordResetRequestDto resetRequest = new PasswordResetRequestDto();
        resetRequest.setEmail(null);

        assertThat(validator.validate(sendRequest))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("email");
        assertThat(validator.validate(resetRequest))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("email");
    }

    // 비밀번호가 없으면 서비스에서 equals 호출이 NPE 를 내 500 이 됐다.
    // 인증 토큰도 없으면 남의 계정을 바꿀 수 있어 경계에서 막는다.
    @Test
    void 비밀번호와_인증토큰이_없으면_경계에서_막는다() {
        PasswordResetRequestDto request = new PasswordResetRequestDto();
        request.setEmail("a@b.com");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("newPassword", "confirmPassword", "resetToken");
    }

    @Test
    void validatesPasswordRequestBodiesAtControllerBoundary() throws NoSuchMethodException {
        Method sendCode = FindPasswordController.class
                .getDeclaredMethod("sendCode", EmailSendRequestDto.class);
        Method resetPassword = FindPasswordController.class
                .getDeclaredMethod("resetPassword", PasswordResetRequestDto.class);

        assertThat(sendCode.getParameters()[0].isAnnotationPresent(Valid.class)).isTrue();
        assertThat(resetPassword.getParameters()[0].isAnnotationPresent(Valid.class)).isTrue();
    }
}
