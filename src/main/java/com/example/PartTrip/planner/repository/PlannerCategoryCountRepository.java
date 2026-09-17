package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.PlannerCategoryCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerCategoryCountRepository
        extends JpaRepository<PlannerCategoryCountEntity, Long> {

    List<PlannerCategoryCountEntity> findByPlanIdOrderBySeqAsc(Long planId);

    void deleteByPlanId(Long planId);
}
