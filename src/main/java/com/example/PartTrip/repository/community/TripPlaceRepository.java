package com.example.PartTrip.repository.community;

import com.example.PartTrip.entity.community.TripPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripPlaceRepository extends JpaRepository<TripPlaceEntity, Long> {

    // 일정에 속한 장소들을 일차/순서대로 조회
    List<TripPlaceEntity> findByTripIdOrderByDayNumberAscSortOrderAsc(Long tripId);
}
