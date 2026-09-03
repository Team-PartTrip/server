package com.example.PartTrip.global.exception;

// 자원은 있지만 이 사용자가 할 수 없는 요청. GlobalExceptionHandler 가 403 으로 바꾼다.
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
