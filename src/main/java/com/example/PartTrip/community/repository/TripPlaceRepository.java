package com.example.PartTrip.community.repository;

import com.example.PartTrip.community.entity.TripPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripPlaceRepository extends JpaRepository<TripPlaceEntity, Long> {

    // 일정에 속한 장소들을 일차/순서대로 조회
    List<TripPlaceEntity> findByTripIdOrderByDayNumberAscSortOrderAsc(Long tripId);

    // 일정 수정 시 기존 장소를 모두 삭제하기 위함
    void deleteByTripId(Long tripId);
}
