package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.CreatePlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerCreateResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerCityRepository plannerCityRepository;
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

        if (cities != null && !cities.isEmpty()) {
            saveCities(saved, cities);
        }
        return saved;
    }

    /**
     * 도시별 기간을 저장한다.
     *
     * 여행 기간을 빈틈 없이 이어 덮어야 한다. 하루라도 비면 AI 가 그날
     * 어느 도시에서 일정을 짤지 알 수 없다.
     */
    private void saveCities(GroupTravelPlanEntity plan, List<PlannerCityRequestDto> cities) {
        List<PlannerCityEntity> rows = new ArrayList<>(cities.size());
        LocalDate expected = plan.getStartDate();

        for (int i = 0; i < cities.size(); i++) {
            PlannerCityRequestDto city = cities.get(i);

            if (city.getEndDate().isBefore(city.getStartDate())) {
                throw new IllegalArgumentException(
                        city.getCityName() + " 의 종료일이 시작일보다 빠릅니다.");
            }
            if (!city.getStartDate().equals(expected)) {
                throw new IllegalArgumentException(
                        "도시별 기간이 이어지지 않습니다. " + expected + " 부터 시작해야 합니다.");
            }

            PlannerCityEntity row = new PlannerCityEntity();
            row.setPlanId(plan.getPlanId());
            row.setSeq(i);
            row.setCountryName(city.getCountryName().trim());
            row.setCityName(city.getCityName().trim());
            row.setStartDate(city.getStartDate());
            row.setEndDate(city.getEndDate());
            rows.add(row);

            expected = city.getEndDate().plusDays(1);
        }

        if (!expected.minusDays(1).equals(plan.getEndDate())) {
            throw new IllegalArgumentException(
                    "도시별 기간이 여행 기간을 다 덮지 않습니다. 마지막 도시는 "
                            + plan.getEndDate() + " 에 끝나야 합니다.");
        }
        plannerCityRepository.saveAll(rows);
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
