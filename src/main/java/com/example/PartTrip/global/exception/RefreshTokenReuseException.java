package com.example.PartTrip.global.exception;

/**
 * 이미 회전시켜 죽은 리프레시 토큰이 유예 시간이 지난 뒤 다시 들어왔다.
 * 탈취로 보고 세션을 끊는다.
 *
 * 전용 예외로 둔 이유가 있다. 세션을 끊으려면 행을 지운 뒤 예외를 던져야
 * 하는데, 트랜잭션이 예외에 롤백하면 그 삭제가 없던 일이 된다. 이 예외만
 * noRollbackFor 로 빼려면 다른 실패와 구분되는 형이 필요하다.
 */
public class RefreshTokenReuseException extends RuntimeException {

    public RefreshTokenReuseException(String message) {
        super(message);
    }
}
