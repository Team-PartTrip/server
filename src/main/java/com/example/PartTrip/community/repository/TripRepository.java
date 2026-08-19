package com.example.PartTrip.community.repository;

import com.example.PartTrip.community.entity.TripEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<TripEntity, Long> {

    // 내가 만든 일정 목록 (최신순)
    List<TripEntity> findByUserIdOrderByCreateDateDesc(String userId);

    // 공개(공유)된 일정 목록 (최신순, 페이지네이션)
    Page<TripEntity> findByIsPublicTrueOrderByCreateDateDesc(Pageable pageable);
}
