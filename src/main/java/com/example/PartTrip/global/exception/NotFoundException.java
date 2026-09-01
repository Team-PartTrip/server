package com.example.PartTrip.global.exception;

// 없는 자원을 가리킨 요청. GlobalExceptionHandler 가 404 로 바꾼다.
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
