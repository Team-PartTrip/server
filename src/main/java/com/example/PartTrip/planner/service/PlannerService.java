package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerCreateResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerCityWriter plannerCityWriter;
    private final PlannerCategoryCountService plannerCategoryCountService;
    private final PlannerInviteLinkFactory inviteLinkFactory;
    private final PlannerScheduleLockService plannerScheduleLockService;

    @Transactional
    public PlannerCreateResponseDto createPlanner(
            CreatePlannerRequestDto dto,
            String userId
    ) {
        validateRequest(dto);
        plannerScheduleLockService.lockUser(userId);
        validateNoOverlappingPlan(dto, userId);

        int headcount = Boolean.TRUE.equals(dto.getIsSolo())
                ? 1
                : dto.getMemberCount();
        LocalDateTime now = LocalDateTime.now();

        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId(userId);
        group.setGroupName(dto.getTitle());
        group.setHeadcount(headcount);
        group.setInviteCode(createUniqueInviteCode());
        group.setStatus(GroupStatus.PLANNING);
        group.setCreatedAt(now);

        TravelGroupEntity savedGroup = travelGroupRepository.save(group);

        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setGroupId(savedGroup.getGroupId());
        owner.setUserId(userId);
        owner.setRole(GroupRole.OWNER);
        owner.setJoinedAt(now);
        groupMemberRepository.save(owner);

        GroupTravelPlanEntity travelPlan = createTravelPlanIfPresent(dto, savedGroup, now);

        return PlannerCreateResponseDto.builder()
                .plannerId(savedGroup.getGroupId())
                .title(savedGroup.getGroupName())
                .status(savedGroup.getStatus().name())
                .memberCount(savedGroup.getHeadcount())
                .startDate(travelPlan == null ? null : travelPlan.getStartDate())
                .endDate(travelPlan == null ? null : travelPlan.getEndDate())
                .countryName(travelPlan == null ? null : travelPlan.getCountryName())
                .cityName(travelPlan == null ? null : travelPlan.getCityName())
                .inviteLink(inviteLinkFactory.create(savedGroup.getInviteCode()))
                .build();
    }

    private void validateNoOverlappingPlan(CreatePlannerRequestDto dto, String userId) {
        if (dto.getStartDate() == null) {
            return;
        }
        if (groupTravelPlanRepository.existsOverlappingPlanForUser(
                userId, dto.getStartDate(), dto.getEndDate())) {
            throw new IllegalArgumentException("해당 기간에 이미 등록된 여행 계획이 있습니다.");
        }
    }

    private void validateRequest(CreatePlannerRequestDto dto) {
        if (!Boolean.TRUE.equals(dto.getIsSolo()) && dto.getMemberCount() < 2) {
            throw new IllegalArgumentException("함께 여행하는 경우 인원은 2명 이상이어야 합니다.");
        }

        boolean hasAnyTravelPlanValue = dto.getCountryName() != null
                || dto.getCityName() != null
                || dto.getStartDate() != null
                || dto.getEndDate() != null;
        boolean hasAllTravelPlanValues = dto.getCountryName() != null
                && !dto.getCountryName().isBlank()
                && dto.getCityName() != null
                && !dto.getCityName().isBlank()
                && dto.getStartDate() != null
                && dto.getEndDate() != null;

        if (hasAnyTravelPlanValue && !hasAllTravelPlanValues) {
            throw new IllegalArgumentException("여행지와 기간은 모두 입력하거나 모두 생략해야 합니다.");
        }

        if (hasAllTravelPlanValues && dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("여행 종료일은 시작일보다 빠를 수 없습니다.");
        }
    }

    private GroupTravelPlanEntity createTravelPlanIfPresent(
            CreatePlannerRequestDto dto,
            TravelGroupEntity savedGroup,
            LocalDateTime now
    ) {
        if (dto.getCountryName() == null) {
            return null;
        }

        GroupTravelPlanEntity travelPlan = new GroupTravelPlanEntity();
        travelPlan.setGroupId(savedGroup.getGroupId());
        travelPlan.setTravelTitle(dto.getTitle());
        travelPlan.setCountryName(dto.getCountryName().trim());
        travelPlan.setCityName(dto.getCityName().trim());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());
        travelPlan.setCreatedAt(now);

        List<PlannerCityRequestDto> cities = dto.getCities();
        if (cities != null && !cities.isEmpty()) {
            // 첫 도시를 대표로 둔다. 도시 하나만 보던 기존 코드가 이걸 읽는다
            travelPlan.setCountryName(cities.get(0).getCountryName().trim());
            travelPlan.setCityName(cities.get(0).getCityName().trim());
        }

        GroupTravelPlanEntity saved = groupTravelPlanRepository.save(travelPlan);

        plannerCityWriter.replace(saved, cities);
        plannerCategoryCountService.replace(saved, cities, dto.getPlaceCounts());
        return saved;
    }

    private String createUniqueInviteCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String inviteCode = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();

            if (!travelGroupRepository.existsByInviteCode(inviteCode)) {
                return inviteCode;
            }
        }

        throw new IllegalStateException("초대 코드를 생성하지 못했습니다.");
    }
}
