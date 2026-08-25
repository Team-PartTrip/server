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

    // 그룹 멤버인지 확인 — 플래너 API 전반의 접근 권한 검사에 쓴다
    boolean existsByGroupIdAndUserId(Long groupId, String userId);

    long countByGroupId(Long groupId);

    // 플래너 목록에서 그룹별 현재 참여 인원을 한 번에 조회한다
    @Query("""
            SELECT m.groupId, COUNT(m)
            FROM GroupMemberEntity m
            WHERE m.groupId IN :groupIds
            GROUP BY m.groupId
            """)
    List<Object[]> countMembersByGroupIds(@Param("groupIds") Collection<Long> groupIds);
}
