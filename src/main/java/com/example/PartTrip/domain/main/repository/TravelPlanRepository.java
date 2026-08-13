package com.example.PartTrip.domain.main.repository;

import com.example.PartTrip.domain.main.entity.TravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelPlanRepository extends JpaRepository<TravelPlanEntity, Long> {

    // 사용자 아이디로 여행 일정 조회
    Optional<TravelPlanEntity> findByUserId(String userId);

    Optional<TravelPlanEntity> findByTravelPlanId(Long travelPlanId);
}