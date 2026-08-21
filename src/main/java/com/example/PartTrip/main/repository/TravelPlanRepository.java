package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.TravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelPlanRepository extends JpaRepository<TravelPlanEntity, Long> {

    // 사용자 아이디로 여행 일정 조회
    Optional<TravelPlanEntity> findByUserId(String userId);

    Optional<TravelPlanEntity> findByTravelPlanId(Long travelPlanId);

    // 수정 시 소유자까지 함께 확인한다 (다른 사용자의 여행 계획을 바꿀 수 없도록)
    Optional<TravelPlanEntity> findByTravelPlanIdAndUserId(Long travelPlanId, String userId);
}