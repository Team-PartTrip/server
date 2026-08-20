package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.TodayPhraseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodayPhraseRepository extends JpaRepository<TodayPhraseEntity, Long> {

    Optional<TodayPhraseEntity> findByCountryNameAndDayNumber(String countryName, Integer dayNumber);
}