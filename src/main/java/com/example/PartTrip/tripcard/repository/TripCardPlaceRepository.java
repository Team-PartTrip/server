package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripCardPlaceRepository extends JpaRepository<TripCardPlaceEntity, Long> {

    // D10 타임라인 — 날짜 → 순서
    List<TripCardPlaceEntity> findByTripCardIdOrderByVisitedDateAscSortOrderAsc(Long tripCardId);

    void deleteByTripCardId(Long tripCardId);
}
