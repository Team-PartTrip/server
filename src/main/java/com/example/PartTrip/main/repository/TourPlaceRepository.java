package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourPlaceRepository extends JpaRepository<TourPlaceEntity, Long> {

    // 나라별 관광지 조회
    List<TourPlaceEntity> findByCountryName(String countryName);

    // 나라 + 도시 + 카테고리로 조회 (도시·카테고리는 null 이면 조건에서 제외)
    @Query("""
            SELECT p FROM TourPlaceEntity p
            WHERE p.countryName = :countryName
              AND (:cityName IS NULL OR p.cityName = :cityName)
              AND (:category IS NULL OR p.category = :category)
            ORDER BY p.rating DESC NULLS LAST, p.placeName ASC
            """)
    List<TourPlaceEntity> search(String countryName,
                                 String cityName,
                                 TourPlaceCategory category);

}
