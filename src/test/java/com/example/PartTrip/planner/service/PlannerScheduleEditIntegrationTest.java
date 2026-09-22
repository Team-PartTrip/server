package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.planner.dto.request.SavePlannerScheduleRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** 실제 JPA 저장 후 영속성 컨텍스트를 비워 저장 결과를 재조회한다. */
@DataJpaTest
@Import(PlannerScheduleEditService.class)
class PlannerScheduleEditIntegrationTest {
    @Autowired PlannerScheduleEditService service;
    @Autowired TravelGroupRepository groups;
    @Autowired GroupTravelPlanRepository plans;
    @Autowired PlannerScheduleSlotRepository slots;
    @Autowired EntityManager entityManager;
    @MockitoBean PlannerDraftService drafts;

    private Long groupId;
    private Long planId;
    private final LocalDate date = LocalDate.of(2026, 10, 10);

    @BeforeEach
    void setUp() {
        var group = new TravelGroupEntity();
        group.setOwnerUserId("owner");
        group.setHeadcount(2);
        group.setStatus(GroupStatus.PLANNING);
        group.setCreatedAt(LocalDateTime.of(2026, 9, 22, 12, 0));
        groupId = groups.saveAndFlush(group).getGroupId();
        var plan = new GroupTravelPlanEntity();
        plan.setGroupId(groupId);
        plan.setCountryName("대한민국");
        plan.setCityName("강릉시");
        plan.setStartDate(date);
        plan.setEndDate(date.plusDays(1));
        plan.setCreatedAt(group.getCreatedAt());
        planId = plans.saveAndFlush(plan).getPlanId();
    }

    /** 이동한 카드의 ID, 요청 순서, 빈 카드 및 생략한 카드 삭제가 저장된다. */
    @Test
    void reorderMoveAddAndDeleteSurviveReload() {
        var first = slots.saveAndFlush(new PlannerScheduleSlotEntity(planId, date, 1, null));
        var second = slots.saveAndFlush(new PlannerScheduleSlotEntity(planId, date, 2, null));
        var removed = slots.saveAndFlush(new PlannerScheduleSlotEntity(planId, date, 3, null));
        service.save(groupId, new SavePlannerScheduleRequestDto(List.of(
                new SavePlannerScheduleRequestDto.Day(date.plusDays(1), List.of(
                        new SavePlannerScheduleRequestDto.Slot(second.getSlotId(), null),
                        new SavePlannerScheduleRequestDto.Slot(first.getSlotId(), null),
                        new SavePlannerScheduleRequestDto.Slot(null, null))))), "owner");
        entityManager.flush();
        entityManager.clear();

        var stored = slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(planId);
        assertThat(stored).hasSize(3);
        assertThat(stored).extracting(PlannerScheduleSlotEntity::getSortOrder).containsExactly(1, 2, 3);
        assertThat(stored).extracting(PlannerScheduleSlotEntity::getVisitDate).containsOnly(date.plusDays(1));
        assertThat(stored).extracting(PlannerScheduleSlotEntity::getTourPlaceId).containsOnlyNulls();
        assertThat(stored.get(0).getSlotId()).isEqualTo(second.getSlotId());
        assertThat(stored.get(1).getSlotId()).isEqualTo(first.getSlotId());
        assertThat(slots.findById(removed.getSlotId())).isEmpty();
        assertThat(plans.findById(planId).orElseThrow().getScheduleEdited()).isTrue();
    }

    /** 빈 일정 저장도 삭제와 편집 여부가 실제 DB에 남는다. */
    @Test
    void deletingAllSlotsPersistsEditedFlag() {
        slots.saveAndFlush(new PlannerScheduleSlotEntity(planId, date, 1, null));
        service.save(groupId, new SavePlannerScheduleRequestDto(List.of()), "owner");
        entityManager.flush();
        entityManager.clear();
        assertThat(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(planId)).isEmpty();
        assertThat(plans.findById(planId).orElseThrow().getScheduleEdited()).isTrue();
    }

    /** 리더가 아닌 사용자는 JPA 서비스를 통해서도 수정할 수 없다. */
    @Test
    void nonOwnerCannotSave() {
        assertThatThrownBy(() -> service.save(groupId,
                new SavePlannerScheduleRequestDto(List.of()), "member"))
                .isInstanceOf(ForbiddenException.class);
    }
}
