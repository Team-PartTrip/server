package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.PlannerListResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerListService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;

    @Transactional(readOnly = true)
    public List<PlannerListResponseDto> getMyPlanners(String userId) {
        List<GroupMemberEntity> memberships = groupMemberRepository.findByUserId(userId);
        if (memberships.isEmpty()) {
            return List.of();
        }

        List<Long> groupIds = memberships.stream()
                .map(GroupMemberEntity::getGroupId)
                .toList();

        Map<Long, TravelGroupEntity> groupsById = travelGroupRepository
                .findAllById(groupIds)
                .stream()
                .collect(Collectors.toMap(
                        TravelGroupEntity::getGroupId,
                        Function.identity()
                ));

        Map<Long, GroupTravelPlanEntity> plansByGroupId = new HashMap<>();
        groupTravelPlanRepository.findByGroupIdInOrderByCreatedAtDesc(groupIds)
                .forEach(plan -> plansByGroupId.putIfAbsent(plan.getGroupId(), plan));

        Map<Long, Long> joinedCountByGroupId = groupMemberRepository
                .countMembersByGroupIds(groupIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        return memberships.stream()
                .filter(membership -> groupsById.containsKey(membership.getGroupId()))
                .sorted(Comparator.comparing(
                        membership -> groupsById.get(membership.getGroupId()).getCreatedAt(),
                        Comparator.reverseOrder()
                ))
                .map(membership -> toResponse(
                        membership,
                        groupsById.get(membership.getGroupId()),
                        plansByGroupId.get(membership.getGroupId()),
                        joinedCountByGroupId.getOrDefault(membership.getGroupId(), 0L)
                ))
                .toList();
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
