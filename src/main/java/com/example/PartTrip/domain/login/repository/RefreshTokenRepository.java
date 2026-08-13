package com.example.PartTrip.domain.login.repository;

import com.example.PartTrip.domain.login.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // user_id로 사용자가 이미 로그인한 토큰이 있는지 확인할 때 사용
    Optional<RefreshTokenEntity> findByUserId(String userId);

    // 토큰 재발급 요청이 왔을 때 DB에 존재하는 토큰 확인할 때 사용
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
