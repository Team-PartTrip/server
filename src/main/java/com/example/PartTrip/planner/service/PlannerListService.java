package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerListResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlannerListService {

    private final GroupMemberRepository groupMemberRepository;

    @Transactional(readOnly = true)
    public List<PlannerListResponseDto> getMyPlanners(String userId) {
        // 쿼리가 그룹 생성순 → 계획 최신순으로 정렬해 온다.
        // 그룹마다 첫 행이 최신 계획이라, 뒤에 오는 옛 계획은 버린다.
        Map<Long, PlannerListResponseDto> byGroupId = new LinkedHashMap<>();

        for (Object[] row : groupMemberRepository.findMyPlannerRows(userId)) {
            GroupMemberEntity membership = (GroupMemberEntity) row[0];
            TravelGroupEntity group = (TravelGroupEntity) row[1];
            GroupTravelPlanEntity plan = (GroupTravelPlanEntity) row[2];
            long joinedMemberCount = (Long) row[3];

            byGroupId.computeIfAbsent(
                    group.getGroupId(),
                    id -> toResponse(membership, group, plan, joinedMemberCount));
        }
        return List.copyOf(byGroupId.values());
    }

    private PlannerListResponseDto toResponse(
            GroupMemberEntity membership,
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            long joinedMemberCount
    ) {
        return PlannerListResponseDto.builder()
                .plannerId(group.getGroupId())
                .title(group.getGroupName())
                .countryName(plan == null ? null : plan.getCountryName())
                .cityName(plan == null ? null : plan.getCityName())
                .startDate(plan == null ? null : plan.getStartDate())
                .endDate(plan == null ? null : plan.getEndDate())
                .status(group.getStatus().name())
                .role(membership.getRole().name())
                .memberCount(group.getHeadcount())
                .joinedMemberCount(joinedMemberCount)
                .build();
    }
}
