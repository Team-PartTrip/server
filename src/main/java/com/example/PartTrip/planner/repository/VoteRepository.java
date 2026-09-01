package com.example.PartTrip.planner.repository;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM VoteEntity v WHERE v.voteId = :voteId")
    Optional<VoteEntity> findByVoteIdForUpdate(@Param("voteId") Long voteId);

    // 앱 C7 카테고리별 현황
    List<VoteEntity> findByPlanId(Long planId);

    List<VoteEntity> findByPlanIdIn(List<Long> planIds);

    // 플래너 삭제용. 외래키가 없어 순서대로 지워야 한다.
    void deleteByPlanIdIn(List<Long> planIds);

    Optional<VoteEntity> findByPlanIdAndCategory(Long planId, TourPlaceCategory category);

    // 마감 임박 알림용 — 아직 열려 있고 마감이 임박한 투표
    List<VoteEntity> findByStatusAndDeadlineBetween(
            VoteStatus status, LocalDateTime from, LocalDateTime to);
}
