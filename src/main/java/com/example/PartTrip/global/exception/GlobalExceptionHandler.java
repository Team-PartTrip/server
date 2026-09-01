package com.example.PartTrip.global.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

// 프로젝트 전체 예외 처리 클래스
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 없는 자원을 가리킨 요청
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // 자원은 있지만 이 사용자가 할 수 없는 요청
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    // 죽은 리프레시 토큰이 유예가 지난 뒤 다시 들어왔다. 세션은 이미 끊었다.
    @ExceptionHandler(RefreshTokenReuseException.class)
    public ResponseEntity<String> handleRefreshTokenReuse(RefreshTokenReuseException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    // 서명이 안 맞거나 형식이 깨진 토큰.
    //
    // JJWT 예외는 RuntimeException 계열이라 여기서 잡지 않으면 500 이 나간다.
    // 앱은 500 을 "잠시 후 되는 오류" 로 보고 토큰을 지우지 않아서, 사용자가
    // 로그인 화면으로도 못 가고 모든 화면이 실패하는 상태에 갇힌다.
    // 위조·손상 토큰은 401 이 맞다.
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("토큰이 유효하지 않습니다. 다시 로그인 해주세요.");
    }

    // IllegalArgumentException이 발생하면 여기서 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        // 400 Bad Request와 에러 메시지 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    // @Valid 검증 실패 시 여기서 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        // 첫 번째 검증 실패 메시지를 그대로 반환
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    // 업로드 용량 제한 초과 시 여기서 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("업로드 가능한 최대 용량(20MB)을 초과했습니다.");
    }
}