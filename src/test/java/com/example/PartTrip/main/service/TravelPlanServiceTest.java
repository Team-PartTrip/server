package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.DdayResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelPlanServiceTest {

    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private TravelGroupRepository travelGroupRepository;
    @InjectMocks private TravelPlanService travelPlanService;

    @Test
    void 그룹별_최신_계획만_Dday_후보로_사용한다() {
        LocalDate today = LocalDate.now();
        GroupMemberEntity firstMembership = membership(1L);
        GroupMemberEntity secondMembership = membership(2L);
        GroupTravelPlanEntity latestFinished = plan(
                1L, "최신 종료 계획", today.minusDays(5), today.minusDays(1), 3);
        GroupTravelPlanEntity otherGroup = plan(
                2L, "선택할 계획", today.plusDays(5), today.plusDays(10), 2);
        GroupTravelPlanEntity olderUpcoming = plan(
                1L, "무시할 예전 계획", today.plusDays(1), today.plusDays(2), 1);
        TravelGroupEntity group = new TravelGroupEntity();
        group.setHeadcount(4);

        when(groupMemberRepository.findByUserId("user"))
                .thenReturn(List.of(firstMembership, secondMembership));
        when(groupTravelPlanRepository.findByGroupIdInOrderByCreatedAtDesc(List.of(1L, 2L)))
                .thenReturn(List.of(latestFinished, otherGroup, olderUpcoming));
        when(travelGroupRepository.findById(2L)).thenReturn(Optional.of(group));

        DdayResponseDto result = travelPlanService.getDday("user");

        assertThat(result.getCityName()).isEqualTo("선택할 계획");
        assertThat(result.getHeadcount()).isEqualTo(4);
    }

    private GroupMemberEntity membership(Long groupId) {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(groupId);
        return member;
    }

    private GroupTravelPlanEntity plan(
            Long groupId,
            String cityName,
            LocalDate startDate,
            LocalDate endDate,
            int age
    ) {
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setGroupId(groupId);
        plan.setCountryName("나라");
        plan.setCityName(cityName);
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        plan.setCreatedAt(LocalDateTime.now().minusHours(age));
        return plan;
    }
}
