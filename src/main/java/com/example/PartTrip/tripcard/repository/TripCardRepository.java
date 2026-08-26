package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripCardRepository extends JpaRepository<TripCardEntity, Long> {

    // Func-003-01 "여행카드들을 시간순으로 조회"
    List<TripCardEntity> findByUserIdOrderByStartDateDesc(String userId);

    // 조회 · 수정 · 삭제 시 소유자까지 함께 확인한다
    Optional<TripCardEntity> findByTripCardIdAndUserId(Long tripCardId, String userId);

    Optional<TripCardEntity> findByPlanId(Long planId);

    // 카드 id 로 조회
    Optional<TripCardEntity> findByTripCardId(Long tripCardId);

    List<TripCardEntity> findTripCardEntitiesByUserId(String userId);

}
