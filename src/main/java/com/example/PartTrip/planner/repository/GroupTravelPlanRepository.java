package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupTravelPlanRepository extends JpaRepository<GroupTravelPlanEntity, Long> {

    List<GroupTravelPlanEntity> findByGroupIdOrderByStartDateDesc(Long groupId);

    // 플래너 삭제용
    void deleteByGroupId(Long groupId);

    Optional<GroupTravelPlanEntity> findByPlanIdAndGroupId(Long planId, Long groupId);

    // 플래너 상세 화면에 보여줄 가장 최근 여행 계획
    Optional<GroupTravelPlanEntity> findFirstByGroupIdOrderByCreatedAtDesc(Long groupId);

    // 여러 그룹의 최신 여행 계획을 목록 조회용으로 한 번에 가져온다
    List<GroupTravelPlanEntity> findByGroupIdInOrderByCreatedAtDesc(List<Long> groupIds);


}
