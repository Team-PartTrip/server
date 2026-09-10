package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.PlannerCityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerCityRepository extends JpaRepository<PlannerCityEntity, Long> {

    List<PlannerCityEntity> findByPlanIdOrderBySeqAsc(Long planId);

    void deleteByPlanId(Long planId);
}
