package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PlannerScheduleSlotRepository
        extends JpaRepository<PlannerScheduleSlotEntity, Long> {

    List<PlannerScheduleSlotEntity> findByPlanIdOrderByVisitDateAscSortOrderAsc(Long planId);

    // "오늘 일정" 알림 (#143)
    List<PlannerScheduleSlotEntity> findByPlanIdAndVisitDateOrderBySortOrderAsc(
            Long planId, LocalDate visitDate);

    void deleteByPlanIdIn(Collection<Long> planIds);

    @Query("select distinct p.groupId from GroupTravelPlanEntity p, PlannerScheduleSlotEntity s"
            + " where s.planId = p.planId and s.routeStatus = :status")
    List<Long> findPlannerIdsByRouteStatus(@org.springframework.data.repository.query.Param("status") String status);
}
