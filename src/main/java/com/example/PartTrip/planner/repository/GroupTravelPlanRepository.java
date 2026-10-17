package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupTravelPlanRepository extends JpaRepository<GroupTravelPlanEntity, Long> {

    List<GroupTravelPlanEntity> findByGroupIdOrderByStartDateDesc(Long groupId);

    /**
     * 알림 스케줄러가 쓴다 (#143). 내일 떠나는 계획.
     *
     * <p>확정한 그룹만 본다. 일정 초안은 확정 전에 생기므로
     * ({@code PlannerDraftService}) 상태를 보지 않으면 확정 버튼을 누르지도 않은
     * 그룹에 "내일 출발이에요" 가 나간다. DONE 은 이미 끝난 여행이라 뺀다.
     *
     * <p>그룹별로 최신 계획 하나만 본다. 다른 화면이 모두
     * {@code findFirstByGroupIdOrderByCreatedAtDesc} 로 최신 하나만 보는데
     * 여기만 전부 보면, 남아 있는 옛 계획 날짜로 알림이 나간다.
     *
     * <p>{@code MAX(createdAt)} 이 아니라 {@code MAX(planId)} 인 것은 위 메서드의
     * 설명과 같은 이유다 — {@code createdAt} 이 같은 계획이 둘이면 어느 쪽인지
     * 정해지지 않아 둘 다 뽑힌다. {@code planId} 는 IDENTITY 라 시간순으로
     * 증가하면서 같은 값이 없다.
     */
    @Query("""
            SELECT p FROM GroupTravelPlanEntity p
            JOIN TravelGroupEntity g ON g.groupId = p.groupId
            WHERE p.startDate = :startDate
              AND g.status IN (com.example.PartTrip.planner.enums.GroupStatus.CONFIRMED,
                               com.example.PartTrip.planner.enums.GroupStatus.TRAVELING)
              AND p.planId = (SELECT MAX(p2.planId) FROM GroupTravelPlanEntity p2
                               WHERE p2.groupId = p.groupId)
            """)
    List<GroupTravelPlanEntity> findConfirmedStartingOn(@Param("startDate") LocalDate startDate);

    /** 알림 스케줄러가 쓴다 (#143). 오늘이 기간 안인 확정된 계획. 위와 같이 그룹별 최신 하나만. */
    @Query("""
            SELECT p FROM GroupTravelPlanEntity p
            JOIN TravelGroupEntity g ON g.groupId = p.groupId
            WHERE p.startDate <= :date AND p.endDate >= :date
              AND g.status IN (com.example.PartTrip.planner.enums.GroupStatus.CONFIRMED,
                               com.example.PartTrip.planner.enums.GroupStatus.TRAVELING)
              AND p.planId = (SELECT MAX(p2.planId) FROM GroupTravelPlanEntity p2
                               WHERE p2.groupId = p.groupId)
            """)
    List<GroupTravelPlanEntity> findConfirmedCovering(@Param("date") LocalDate date);

    // 플래너 삭제용
    void deleteByGroupId(Long groupId);

    Optional<GroupTravelPlanEntity> findByPlanIdAndGroupId(Long planId, Long groupId);

    @Query("""
            SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
            FROM GroupTravelPlanEntity p
            JOIN GroupMemberEntity m ON m.groupId = p.groupId
            WHERE m.userId = :userId
              AND p.startDate <= :endDate
              AND p.endDate >= :startDate
            """)
    boolean existsOverlappingPlanForUser(
            @Param("userId") String userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
            FROM GroupTravelPlanEntity p
            JOIN GroupMemberEntity existingMember ON existingMember.groupId = p.groupId
            WHERE existingMember.userId IN (
                  SELECT currentMember.userId
                  FROM GroupMemberEntity currentMember
                  WHERE currentMember.groupId = :groupId
            )
              AND p.groupId <> :groupId
              AND p.startDate <= :endDate
              AND p.endDate >= :startDate
            """)
    boolean existsOverlappingPlanForGroupMembersExcludingGroup(
            @Param("groupId") Long groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 플래너 상세 화면에 보여줄 가장 최근 여행 계획.
     *
     * createdAt 이 같은 계획이 있으면 이름만으로는 어느 것이 먼저인지 정해지지
     * 않는다. 그러면 화면마다 다른 계획을 골라 목록·상세·D-Day 가 어긋난다.
     * planId 로 한 번 더 정렬해 어디서나 같은 계획을 보게 한다.
     */
    @Query("""
            SELECT p FROM GroupTravelPlanEntity p
             WHERE p.groupId = :groupId
             ORDER BY p.createdAt DESC, p.planId DESC
            LIMIT 1
            """)
    Optional<GroupTravelPlanEntity> findFirstByGroupIdOrderByCreatedAtDesc(
            @Param("groupId") Long groupId);

    // 여러 그룹의 최신 여행 계획을 목록 조회용으로 한 번에 가져온다
    @Query("""
            SELECT p FROM GroupTravelPlanEntity p
             WHERE p.groupId IN :groupIds
             ORDER BY p.createdAt DESC, p.planId DESC
            """)
    List<GroupTravelPlanEntity> findByGroupIdInOrderByCreatedAtDesc(
            @Param("groupIds") List<Long> groupIds);


}
