package com.example.PartTrip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

// 프로젝트 전체 예외 처리 클래스
@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException이 발생하면 여기서 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        // 400 Bad Request와 에러 메시지 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    // 업로드 용량 제한 초과 시 여기서 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("업로드 가능한 최대 용량(20MB)을 초과했습니다.");
    }
}