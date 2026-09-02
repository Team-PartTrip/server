package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {

    List<GroupMemberEntity> findByGroupIdOrderByJoinedAtAsc(Long groupId);

    // 내가 속한 그룹 목록 (앱 C1 계획 목록)
    List<GroupMemberEntity> findByUserId(String userId);

    Optional<GroupMemberEntity> findByGroupIdAndUserId(Long groupId, String userId);

    List<GroupMemberEntity> findByGroupIdAndUserIdIn(Long groupId, Collection<String> userIds);

    // 그룹 멤버인지 확인 — 플래너 API 전반의 접근 권한 검사에 쓴다
    boolean existsByGroupIdAndUserId(Long groupId, String userId);

    long countByGroupId(Long groupId);

    // 플래너 삭제용
    void deleteByGroupId(Long groupId);

    // 플래너 목록에서 그룹별 현재 참여 인원을 한 번에 조회한다
    @Query("""
            SELECT m.groupId, COUNT(m)
            FROM GroupMemberEntity m
            WHERE m.groupId IN :groupIds
            GROUP BY m.groupId
            """)
    List<Object[]> countMembersByGroupIds(@Param("groupIds") Collection<Long> groupIds);

    /**
     * 플래너 목록 한 번에 받기.
     *
     * 예전에는 멤버십 → 그룹 → 최신 계획 → 참여 인원을 각각 물어서 왕복이
     * 네 번이었다. Supabase 가 도쿄에 있어 왕복 한 번이 60~100ms 라,
     * 목록 하나에 300ms 가 걸렸다. 한 번에 받는다.
     *
     * 그룹에 계획이 여러 개면 행도 여러 개 나온다. 최신순으로 정렬해두고
     * 서비스에서 그룹별 첫 행만 쓴다.
     */
    @Query("""
            SELECT m, g, p,
                   (SELECT COUNT(m2) FROM GroupMemberEntity m2 WHERE m2.groupId = m.groupId)
            FROM GroupMemberEntity m
            JOIN TravelGroupEntity g ON g.groupId = m.groupId
            LEFT JOIN GroupTravelPlanEntity p ON p.groupId = m.groupId
            WHERE m.userId = :userId
            ORDER BY g.createdAt DESC, p.createdAt DESC
            """)
    List<Object[]> findMyPlannerRows(@Param("userId") String userId);
}
