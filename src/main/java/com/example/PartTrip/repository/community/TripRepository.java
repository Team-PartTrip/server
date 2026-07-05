package com.example.PartTrip.repository.community;

import com.example.PartTrip.entity.community.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<TripEntity, Long> {

    // 내가 만든 일정 목록 (최신순)
    List<TripEntity> findByUserIdOrderByCreateDateDesc(String userId);

    // 공개(공유)된 일정 목록 (최신순)
    List<TripEntity> findByIsPublicTrueOrderByCreateDateDesc();
}
