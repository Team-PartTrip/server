package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerCityResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerDetailResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerCityRepository plannerCityRepository;
    private final PlannerInviteLinkFactory inviteLinkFactory;

    @Transactional(readOnly = true)
    public PlannerDetailResponseDto getPlannerDetail(
            Long plannerId,
            String userId
    ) {
        GroupMemberEntity membership = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 플래너를 조회할 권한이 없습니다."
                ));

        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElse(null);

        long joinedMemberCount = groupMemberRepository.countByGroupId(plannerId);
        List<PlannerCityResponseDto> cities = cities(plan);

        return PlannerDetailResponseDto.builder()
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
                .inviteLink(inviteLinkFactory.create(group.getInviteCode()))
                .cities(cities)
                .build();
    }

    /**
     * 도시 목록. planner_city 가 비어 있으면 계획의 도시 하나로 만들어 준다.
     *
     * 다중 도시가 생기기 전에 만들어진 플래너에는 planner_city 가 없다.
     * 앱이 목록만 보고 그리도록 여기서 한 줄짜리로 맞춰 준다.
     */
    private List<PlannerCityResponseDto> cities(GroupTravelPlanEntity plan) {
        if (plan == null) {
            return List.of();
        }
        List<PlannerCityResponseDto> saved = plannerCityRepository
                .findByPlanIdOrderBySeqAsc(plan.getPlanId())
                .stream()
                .map(c -> new PlannerCityResponseDto(
                        c.getCountryName(), c.getCityName(), c.getStartDate(), c.getEndDate()))
                .toList();
        if (!saved.isEmpty()) {
            return saved;
        }
        return List.of(new PlannerCityResponseDto(
                plan.getCountryName(), plan.getCityName(),
                plan.getStartDate(), plan.getEndDate()));
    }
}
