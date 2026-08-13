package com.example.PartTrip.domain.main.repository;

import com.example.PartTrip.domain.main.entity.FoodInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodInfoRepository extends JpaRepository<FoodInfoEntity, Long> {

    // 나라별 대표 음식 조회
    List<FoodInfoEntity> findByCountryName(String countryName);
}