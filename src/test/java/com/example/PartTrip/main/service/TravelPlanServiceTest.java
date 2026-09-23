package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.DdayResponseDto;
import com.example.PartTrip.main.dto.TripPhase;
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
        // age 는 createdAt 을 지금에서 몇 시간 뺄지다. 저장소는 createdAt
        // 내림차순으로 주므로 최신일수록 age 가 작아야 한다. 아래 mock 이
        // 돌려주는 순서와 맞춰둔다.
        GroupTravelPlanEntity latestFinished = plan(
                1L, "최신 종료 계획", today.minusDays(5), today.minusDays(1), 1);
        GroupTravelPlanEntity otherGroup = plan(
                2L, "선택할 계획", today.plusDays(5), today.plusDays(10), 2);
        GroupTravelPlanEntity olderUpcoming = plan(
                1L, "무시할 예전 계획", today.plusDays(1), today.plusDays(2), 3);
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

    @Test
    void 여행이_없으면_NO_TRIP_이다() {
        when(groupMemberRepository.findByUserId("user")).thenReturn(List.of());

        DdayResponseDto result = travelPlanService.getDday("user");

        assertThat(result.getStatus()).isEqualTo(TripPhase.NO_TRIP);
        assertThat(result.getStartDate()).isNull();
    }

    @Test
    void 시작_전이면_BEFORE_시작일_당일부터는_DURING_이다() {
        assertThat(phaseOf(3, 7)).isEqualTo(TripPhase.BEFORE);
        assertThat(phaseOf(0, 4)).isEqualTo(TripPhase.DURING);
        assertThat(phaseOf(-2, 2)).isEqualTo(TripPhase.DURING);
        assertThat(phaseOf(-5, 0)).isEqualTo(TripPhase.DURING);
    }

    @Test
    void 문구와_상태는_어긋나지_않는다() {
        assertThat(ddayOf(3, 7)).isEqualTo("D - 3");
        assertThat(ddayOf(0, 4)).isEqualTo("D-Day");
        assertThat(ddayOf(-2, 2)).isEqualTo("여행 중");
    }

    private DdayResponseDto responseFor(int startOffset, int endOffset) {
        LocalDate today = LocalDate.now();
        when(groupMemberRepository.findByUserId("user"))
                .thenReturn(List.of(membership(1L)));
        when(groupTravelPlanRepository.findByGroupIdInOrderByCreatedAtDesc(List.of(1L)))
                .thenReturn(List.of(plan(
                        1L, "오사카",
                        today.plusDays(startOffset),
                        today.plusDays(endOffset),
                        1)));
        when(travelGroupRepository.findById(1L)).thenReturn(Optional.of(new TravelGroupEntity()));

        return travelPlanService.getDday("user");
    }

    private TripPhase phaseOf(int startOffset, int endOffset) {
        return responseFor(startOffset, endOffset).getStatus();
    }

    private String ddayOf(int startOffset, int endOffset) {
        return responseFor(startOffset, endOffset).getDday();
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
        plan.setRegionCode("42");
        plan.setCityName(cityName);
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        plan.setCreatedAt(LocalDateTime.now().minusHours(age));
        return plan;
    }
}
