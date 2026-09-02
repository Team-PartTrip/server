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

    /**
     * 그룹의 '최신 계획'에 달린 투표를 한 번에 가져온다.
     *
     * 예전에는 최신 계획을 먼저 찾고(왕복 1) 그 planId 로 투표를 물었다(왕복 2).
     * Supabase 가 도쿄에 있어 왕복 한 번이 60~100ms 라 그냥 합친다.
     */
    @Query("""
            SELECT v FROM VoteEntity v
             WHERE v.planId = (
                   SELECT MAX(p.planId) FROM GroupTravelPlanEntity p
                    WHERE p.groupId = :plannerId
                      AND p.createdAt = (
                          SELECT MAX(p2.createdAt) FROM GroupTravelPlanEntity p2
                           WHERE p2.groupId = :plannerId)
             )
             ORDER BY v.createdAt ASC
            """)
    List<VoteEntity> findLatestPlanVotes(@Param("plannerId") Long plannerId);
}
