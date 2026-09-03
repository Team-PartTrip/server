package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryInfoRepository extends JpaRepository<CountryInfoEntity, Long> {

    Optional<CountryInfoEntity> findByCountryName(String countryName);

    Optional<CountryInfoEntity> findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc(
            String countryName
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c FROM CountryInfoEntity c
            WHERE LOWER(c.countryName) = LOWER(:countryName)
            ORDER BY c.countryInfoId ASC
            """)
    List<CountryInfoEntity> findByCountryNameIgnoreCaseForUpdate(
            @Param("countryName") String countryName
    );

    @Query("select count(distinct c.countryName) from CountryInfoEntity c")
    long countDistinctCountries();

    // 검색어가 포함된 국가만 조회
    List<CountryInfoEntity> findTop20ByCountryNameContainingOrderByCountryNameAsc(
            String keyword
    );
    @Query("""
            SELECT c FROM CountryInfoEntity c
            WHERE LOWER(c.countryName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.cityName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY c.countryName ASC, c.cityName ASC
            """)
    List<CountryInfoEntity> searchByCountryOrCity(@Param("keyword") String keyword);
}
