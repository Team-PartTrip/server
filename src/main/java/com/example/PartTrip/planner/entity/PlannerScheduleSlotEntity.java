package com.example.PartTrip.planner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "planner_schedule_slot",
        indexes = @Index(name = "idx_schedule_slot_plan", columnList = "plan_id")
)
@Getter
@Setter
@NoArgsConstructor
public class PlannerScheduleSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    /** 그날 안에서의 순서. 1 부터 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "tour_place_id")
    private Long tourPlaceId;

    public PlannerScheduleSlotEntity(Long planId, LocalDate visitDate, int sortOrder, Long tourPlaceId) {
        this.planId = planId;
        this.visitDate = visitDate;
        this.sortOrder = sortOrder;
        this.tourPlaceId = tourPlaceId;
    }
}
