package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface TripCardRepository extends JpaRepository<TripCardEntity, Long> {

    // Func-003-01 "여행카드들을 시간순으로 조회"
    List<TripCardEntity> findByUserIdOrderByStartDateDesc(String userId);

    // 조회 · 수정 · 삭제 시 소유자까지 함께 확인한다
    Optional<TripCardEntity> findByTripCardIdAndUserId(Long tripCardId, String userId);

    Optional<TripCardEntity> findByPlanIdAndUserId(Long planId, String userId);

    List<TripCardEntity> findByPlanIdAndUserIdIn(Long planId, Collection<String> userIds);

    // Func-007-01 프로필 통계의 "여행" 수
    long countByUserId(String userId);
}
