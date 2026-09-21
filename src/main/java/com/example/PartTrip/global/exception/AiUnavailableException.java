package com.example.PartTrip.global.exception;

/** AI 가 답을 못 줬다. 사용자가 잘못한 게 아니라서 400 이 아니라 503 이다 */
public class AiUnavailableException extends RuntimeException {

    public AiUnavailableException(String message) {
        super(message);
    }
}
