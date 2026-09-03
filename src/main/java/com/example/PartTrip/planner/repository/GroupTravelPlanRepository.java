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
