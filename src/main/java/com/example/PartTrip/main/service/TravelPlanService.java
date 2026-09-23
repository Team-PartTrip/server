package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.DdayResponseDto;
import com.example.PartTrip.main.dto.TripPhase;
import com.example.PartTrip.planner.service.PlannerDraftService;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final TravelGroupRepository travelGroupRepository;
    private final PlannerDraftService plannerDraftService;
    private final Clock clock;

    // D-Day 조회
    //
    // 여행지와 기간은 플래너(Func-005-02)에서만 정하므로,
    // 내가 속한 그룹들의 여행 계획 중 '가장 가까운' 것 하나를 보여준다.
    @Transactional(readOnly = true)
    public DdayResponseDto getDday(String userId) {

        List<Long> groupIds = groupMemberRepository.findByUserId(userId)
                .stream()
                .map(GroupMemberEntity::getGroupId)
                .toList();

        // 참여한 플래너가 없으면 예외 대신 '쉬는 중' 상태를 내려줌
        // 앱 첫 진입 사용자가 대부분 이 경로를 탐
        if (groupIds.isEmpty()) {
            return restingDto();
        }

        LocalDate today = LocalDate.now(clock);
        Set<Long> groupsWithLatestPlan = new HashSet<>();

        // 이미 끝난 여행은 제외하고 시작일이 가장 이른 것을 고른다.
        // 여행 중인 계획은 끝나지 않았으므로 자연히 먼저 잡힌다.
        GroupTravelPlanEntity nearest =
                groupTravelPlanRepository
                        .findByGroupIdInOrderByCreatedAtDesc(groupIds)
                        .stream()
                        .filter(plan -> groupsWithLatestPlan.add(plan.getGroupId()))
                        .filter(plan -> !plan.getEndDate().isBefore(today))
                        .min(Comparator.comparing(
                                GroupTravelPlanEntity::getStartDate
                        ))
                        .orElse(null);

        if (nearest == null) {
            return restingDto();
        }

        // 인원은 그룹에 설정된 모집 인원을 따른다
        Integer headcount = travelGroupRepository.findById(nearest.getGroupId())
                .map(TravelGroupEntity::getHeadcount)
                .orElse(null);

        DdayResponseDto response = toDdayResponseDto(
                nearest.getCountryName(),
                nearest.getCityName(),
                nearest.getStartDate(),
                nearest.getEndDate(),
                headcount,
                today
        );
        if (response.getStatus() == TripPhase.DURING) {
            var schedule = plannerDraftService.getSchedule(nearest.getGroupId(), userId);
            response.setTodaySchedule(schedule.days().stream()
                    .filter(day -> today.equals(day.date()))
                    .flatMap(day -> day.slots().stream())
                    .toList());
        }
        return response;
    }

    // 보여줄 여행이 없을 때. 문구는 클라이언트가 status 를 보고 정한다.
    private DdayResponseDto restingDto() {
        return new DdayResponseDto(
                null, null, null, null, null, "쉬는 중", TripPhase.NO_TRIP);
    }

    // 여행 정보 -> DdayResponseDto 변환
    private DdayResponseDto toDdayResponseDto(
            String countryName,
            String cityName,
            LocalDate startDate,
            LocalDate endDate,
            Integer headcount,
            LocalDate today
    ) {


        long days = ChronoUnit.DAYS.between(today, startDate);

        String dday;
        TripPhase status;

        if (days > 0) {
            dday = "D - " + days;
            status = TripPhase.BEFORE;
        } else if (days == 0) {
            dday = "D-Day";
            status = TripPhase.DURING;
        } else if (!today.isAfter(endDate)) {
            dday = "여행 중";
            status = TripPhase.DURING;
        } else {
            dday = "여행 종료";
            status = TripPhase.ENDED;
        }

        return new DdayResponseDto(
                countryName,
                cityName,
                startDate,
                endDate,
                headcount,
                dday,
                status
        );
    }
}
