package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {

    /**
     * 비밀번호 재설정용 — 행을 잠그고 읽는다.
     *
     * 토큰을 확인하고 지우는 사이에 다른 요청이 같은 토큰으로 들어오면
     * 둘 다 통과한다. 한 번 쓴 토큰은 한 번만 통하게 잠근다.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EmailVerificationEntity e WHERE e.email = :email")
    Optional<EmailVerificationEntity> findByEmailForUpdate(String email);
}