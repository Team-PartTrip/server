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
                .containsExactly("email");
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
