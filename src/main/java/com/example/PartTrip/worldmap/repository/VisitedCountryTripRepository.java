package com.example.PartTrip.worldmap.repository;

import com.example.PartTrip.worldmap.entity.VisitedCountryTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VisitedCountryTripRepository extends JpaRepository<VisitedCountryTripEntity, Long> {

    Optional<VisitedCountryTripEntity> findByTripCardId(Long tripCardId);

    List<VisitedCountryTripEntity> findByVisitedCountryIdInOrderByStartDateAsc(
            Collection<Long> visitedCountryIds
    );

    List<VisitedCountryTripEntity> findByVisitedCountryIdOrderByStartDateAsc(Long visitedCountryId);
}
