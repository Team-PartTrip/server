package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PlannerScheduleSlotRepository
        extends JpaRepository<PlannerScheduleSlotEntity, Long> {

    List<PlannerScheduleSlotEntity> findByPlanIdOrderByVisitDateAscSortOrderAsc(Long planId);

    void deleteByPlanIdIn(Collection<Long> planIds);
}
