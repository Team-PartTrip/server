package com.example.PartTrip.global.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void 지금_할_수_없는_요청은_409_와_이유를_돌려준다() {
        ResponseEntity<String> response = handler.handleIllegalStateException(
                new IllegalStateException("여행 종료 후에는 여행 카드를 수정할 수 없습니다."));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("여행 종료 후에는 여행 카드를 수정할 수 없습니다.");
    }
}
