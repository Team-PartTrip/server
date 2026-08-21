package com.example.PartTrip.planner.repository;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    // 앱 C7 카테고리별 현황
    List<VoteEntity> findByPlanId(Long planId);

    Optional<VoteEntity> findByPlanIdAndCategory(Long planId, TourPlaceCategory category);

    // 마감 임박 알림용 — 아직 열려 있고 마감이 임박한 투표
    List<VoteEntity> findByStatusAndDeadlineBetween(
            VoteStatus status, LocalDateTime from, LocalDateTime to);
}
