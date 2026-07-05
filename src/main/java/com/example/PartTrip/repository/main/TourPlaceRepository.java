package com.example.PartTrip.repository.main;

import com.example.PartTrip.entity.main.TourPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourPlaceRepository extends JpaRepository<TourPlaceEntity, Long> {

    // 나라별 관광지 조회
    List<TourPlaceEntity> findByCountryName(String countryName);

}