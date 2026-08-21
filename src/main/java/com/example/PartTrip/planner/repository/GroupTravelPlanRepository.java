package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupTravelPlanRepository extends JpaRepository<GroupTravelPlanEntity, Long> {

    List<GroupTravelPlanEntity> findByGroupIdOrderByStartDateDesc(Long groupId);

    Optional<GroupTravelPlanEntity> findByPlanIdAndGroupId(Long planId, Long groupId);
}
