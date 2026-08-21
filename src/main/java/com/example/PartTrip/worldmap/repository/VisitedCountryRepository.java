package com.example.PartTrip.worldmap.repository;

import com.example.PartTrip.worldmap.entity.VisitedCountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitedCountryRepository extends JpaRepository<VisitedCountryEntity, Long> {

    // Func-009-01 개인 세계지도 조회
    List<VisitedCountryEntity> findByUserIdOrderByFirstVisitedAtAsc(String userId);

    // Func-009-02 "이미 획득한 국가는 중복 등록하지 않는다"
    Optional<VisitedCountryEntity> findByUserIdAndCountryInfoId(String userId, Long countryInfoId);

    boolean existsByUserIdAndCountryInfoId(String userId, Long countryInfoId);

    // Func-009-04 달성 현황 — 획득한 국가 수
    long countByUserId(String userId);
}
