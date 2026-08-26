package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerTravelPlanResponseDto;
import com.example.PartTrip.planner.dto.request.SavePlannerTravelPlanRequestDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlannerTravelPlanService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;

    @Transactional
    public PlannerTravelPlanResponseDto saveTravelPlan(
            Long plannerId,
            SavePlannerTravelPlanRequestDto dto,
            String userId
    ) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupMemberEntity membership = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));

        if (membership.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 여행지와 기간을 설정할 수 있습니다.");
        }

        if (group.getStatus() != GroupStatus.PLANNING) {
            throw new IllegalArgumentException("투표 시작 전인 플래너만 여행 정보를 변경할 수 있습니다.");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("여행 종료일은 시작일보다 빠를 수 없습니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseGet(GroupTravelPlanEntity::new);

        if (plan.getPlanId() == null) {
            plan.setGroupId(plannerId);
            plan.setTravelTitle(group.getGroupName());
            plan.setCreatedAt(LocalDateTime.now());
        }

        plan.setCountryName(dto.getCountryName().trim());
        plan.setCityName(dto.getCityName().trim());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());

        GroupTravelPlanEntity savedPlan = groupTravelPlanRepository.save(plan);

        return PlannerTravelPlanResponseDto.builder()
                .plannerId(group.getGroupId())
                .planId(savedPlan.getPlanId())
                .title(savedPlan.getTravelTitle())
                .countryName(savedPlan.getCountryName())
                .cityName(savedPlan.getCityName())
                .startDate(savedPlan.getStartDate())
                .endDate(savedPlan.getEndDate())
                .build();
    }
}
