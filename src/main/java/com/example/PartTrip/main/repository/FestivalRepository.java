package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {

    List<FestivalEntity> findByCountryName(String countryName);

    // startDate 는 'yyyy-MM-dd' 문자열이므로 'yyyy-MM' 접두사로 해당 월을 찾는다
    List<FestivalEntity> findByCountryNameAndStartDateStartingWithOrderByStartDateAsc(
            String countryName, String yearMonth);
}
