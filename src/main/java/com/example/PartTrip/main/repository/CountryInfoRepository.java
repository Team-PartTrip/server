package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryInfoRepository extends JpaRepository<CountryInfoEntity, Long> {

    Optional<CountryInfoEntity> findByCountryName(String countryName);

    // 검색어가 포함된 국가만 조회
    List<CountryInfoEntity> findTop20ByCountryNameContainingOrderByCountryNameAsc(
            String keyword
    );
}