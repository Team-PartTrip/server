package com.example.PartTrip.tripcard.repository;

import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripCardPhotoRepository extends JpaRepository<TripCardPhotoEntity, Long> {

    List<TripCardPhotoEntity> findByTripCardIdOrderByTakenAtAsc(Long tripCardId);

    List<TripCardPhotoEntity> findByTripCardPlaceIdOrderBySortOrderAsc(Long tripCardPlaceId);

    long countByTripCardId(Long tripCardId);

    void deleteByTripCardId(Long tripCardId);
}
