package com.example.PartTrip.repository.main;

import com.example.PartTrip.entity.main.TodayPhraseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodayPhraseRepository extends JpaRepository<TodayPhraseEntity, Long> {

    Optional<TodayPhraseEntity> findByCountryNameAndDayNumber(String countryName, Integer dayNumber);
}