package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.SavePlannerScheduleRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;
import com.example.PartTrip.planner.entity.*;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** 소유자가 정한 카드 순서를 그대로 저장한다. */
@Service
@RequiredArgsConstructor
public class PlannerScheduleEditService {
    private final TravelGroupRepository groups;
    private final GroupMemberRepository members;
    private final GroupTravelPlanRepository plans;
    private final PlannerCityRepository cities;
    private final PlannerScheduleSlotRepository slots;
    private final TourPlaceRepository places;
    private final PlannerDraftService drafts;

    /** 그룹 잠금으로 동시 저장을 직렬화하고 기존 슬롯 식별자를 보존한다. */
    @Transactional
    public PlannerScheduleResponseDto save(Long plannerId, SavePlannerScheduleRequestDto request, String userId) {
        TravelGroupEntity group = groups.findByIdForUpdate(plannerId)
                .orElseThrow(() -> new NotFoundException("플래너가 존재하지 않습니다."));
        if (!Objects.equals(group.getOwnerUserId(), userId)) {
            throw new ForbiddenException("플래너 소유자만 일정을 수정할 수 있습니다.");
        }
        if (group.getStatus() != GroupStatus.PLANNING) {
            throw new IllegalArgumentException("확정 전 일정만 수정할 수 있습니다.");
        }
        GroupTravelPlanEntity plan = latest(plannerId);
        List<PlannerCityEntity> regions = cities.findByPlanIdOrderBySeqAsc(plan.getPlanId());
        List<PlannerScheduleSlotEntity> existing = slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
        Map<Long, PlannerScheduleSlotEntity> byId = existing.stream()
                .collect(Collectors.toMap(PlannerScheduleSlotEntity::getSlotId, Function.identity()));
        Set<Long> placeIds = request.days().stream().flatMap(d -> d.slots().stream())
                .map(SavePlannerScheduleRequestDto.Slot::tourPlaceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, TourPlaceEntity> byPlace = places.findAllById(placeIds).stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        Set<LocalDate> dates = new HashSet<>();
        Set<Long> retained = new HashSet<>();
        List<PlannerScheduleSlotEntity> updated = new ArrayList<>();
        for (var day : request.days()) {
            validateDate(plan, day.date());
            if (!dates.add(day.date())) throw new IllegalArgumentException("날짜가 중복되었습니다.");
            Set<Long> dailyPlaces = new HashSet<>();
            int order = 1;
            for (var item : day.slots()) {
                PlannerScheduleSlotEntity slot;
                if (item.slotId() == null) {
                    slot = new PlannerScheduleSlotEntity();
                    slot.setPlanId(plan.getPlanId());
                } else {
                    slot = byId.get(item.slotId());
                    if (slot == null || !retained.add(item.slotId())) {
                        throw new IllegalArgumentException("다른 일정의 슬롯이거나 중복 슬롯입니다.");
                    }
                }
                if (item.tourPlaceId() != null) {
                    TourPlaceEntity place = byPlace.get(item.tourPlaceId());
                    if (place == null || !inRegion(place, plan, regions, day.date())) {
                        throw new IllegalArgumentException("해당 날짜의 여행 지역에 속한 장소만 선택할 수 있습니다.");
                    }
                    if (!dailyPlaces.add(item.tourPlaceId())) throw new IllegalArgumentException("같은 날 장소가 중복되었습니다.");
                }
                slot.setVisitDate(day.date());
                slot.setSortOrder(order++);
                slot.setTourPlaceId(item.tourPlaceId());
                updated.add(slot);
            }
        }
        slots.deleteAll(existing.stream().filter(s -> !retained.contains(s.getSlotId())).toList());
        slots.saveAllAndFlush(updated);
        plan.setScheduleEdited(true);
        return drafts.getSchedule(plannerId, userId);
    }

    /** 멤버가 날짜별 후보를 조회하며 이미 선택한 장소는 제외한다. */
    @Transactional(readOnly = true)
    public List<PlannerScheduleResponseDto.Place> candidates(Long plannerId, LocalDate date, String query, String userId) {
        if (!members.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new ForbiddenException("해당 플래너의 멤버가 아닙니다.");
        }
        GroupTravelPlanEntity plan = latest(plannerId);
        validateDate(plan, date);
        List<PlannerCityEntity> regions = cities.findByPlanIdOrderBySeqAsc(plan.getPlanId());
        Set<Long> used = slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId()).stream()
                .filter(s -> date.equals(s.getVisitDate())).map(PlannerScheduleSlotEntity::getTourPlaceId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        String q = query.strip().toLowerCase(Locale.ROOT);
        List<TourPlaceEntity> candidates = new ArrayList<>();
        if (regions.isEmpty()) candidates.addAll(places.search(PlannerDraftService.KOREA, plan.getCityName(), null));
        else for (PlannerCityEntity region : regions) {
            if (!date.isBefore(region.getStartDate()) && !date.isAfter(region.getEndDate())) {
                candidates.addAll(places.search(PlannerDraftService.KOREA, region.getCityName(), null));
            }
        }
        Set<Long> seen = new HashSet<>();
        return candidates.stream().filter(p -> !used.contains(p.getTourPlaceId()))
                .filter(p -> p.getPlaceName() != null && p.getPlaceName().toLowerCase(Locale.ROOT).contains(q))
                .filter(p -> seen.add(p.getTourPlaceId())).map(PlannerDraftService::toPlace).toList();
    }

    /** 최신 여행 계획을 찾는다. */
    private GroupTravelPlanEntity latest(Long id) {
        return plans.findFirstByGroupIdOrderByCreatedAtDesc(id)
                .orElseThrow(() -> new NotFoundException("여행 계획이 없습니다."));
    }

    /** 여행 기간 밖의 날짜를 거부한다. */
    private void validateDate(GroupTravelPlanEntity plan, LocalDate date) {
        if (date == null || date.isBefore(plan.getStartDate()) || date.isAfter(plan.getEndDate()))
            throw new IllegalArgumentException("여행 기간 안의 날짜를 선택해주세요.");
    }

    /** 여러 도시 일정에서는 해당 날짜에 체류하는 도시만 허용한다. */
    private boolean inRegion(TourPlaceEntity place, GroupTravelPlanEntity plan, List<PlannerCityEntity> regions, LocalDate date) {
        // 국내 여행만 다루므로 나라는 늘 같다. 도시만 맞춘다 (#162)
        if (regions.isEmpty()) return Objects.equals(place.getCityName(), plan.getCityName());
        return regions.stream().anyMatch(c -> !date.isBefore(c.getStartDate()) && !date.isAfter(c.getEndDate())
                && Objects.equals(place.getCityName(), c.getCityName()));
    }
}
