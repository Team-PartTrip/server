package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerConfirmResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.notification.event.RegionVisitedEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerConfirmService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerFinalService plannerFinalService;
    private final TripCardRepository tripCardRepository;
    private final TripCardPlaceRepository tripCardPlaceRepository;
    private final PlannerScheduleService plannerScheduleService;
    private final PlannerScheduleSlotRepository plannerScheduleSlotRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 일정을 확정하고 멤버마다 여행 카드를 만든다.
     *
     * AI 초안(#158)이 있으면 그 카드 중 장소를 정한 칸을 그대로 쓴다. 초안 없이
     * 만든 예전 플래너는 일정 생성기가 도시마다 추천 장소로 채운다.
     */
    @Transactional
    public PlannerConfirmResponseDto confirmPlanner(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        GroupMemberEntity member = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));
        if (member.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 일정을 확정할 수 있습니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));

        TripCardEntity tripCard = createTripCardsIfAbsent(group, plan, userId);
        group.setStatus(GroupStatus.CONFIRMED);

        return PlannerConfirmResponseDto.builder()
                .plannerId(plannerId)
                .confirmedSchedule(plannerFinalService
                        .getConfirmedPlaces(plannerId, userId).getPlaces())
                .tripCardId(tripCard.getTripCardId())
                .build();
    }

    /** 아직 여행 카드가 없는 그룹 멤버에게 확정 일정을 기반으로 카드를 생성한다. */
    private TripCardEntity createTripCardsIfAbsent(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            String userId
    ) {
        var slots = plannerScheduleSlotRepository
                .findByPlanIdOrderByVisitDateAscSortOrderAsc(plan.getPlanId());
        List<PlannerScheduleService.ScheduledPlace> schedule = slots.isEmpty()
                ? plannerScheduleService.buildSchedule(plan, List.of(), userId)
                : plannerScheduleService.fromSlots(slots);
        int uniquePlaceCount = Math.toIntExact(schedule.stream()
                .map(item -> item.place().getTourPlaceId())
                .filter(id -> id != null)
                .distinct()
                .count());
        List<GroupMemberEntity> members = groupMemberRepository
                .findByGroupIdOrderByJoinedAtAsc(group.getGroupId());
        List<String> memberUserIds = members.stream().map(GroupMemberEntity::getUserId).toList();
        Map<String, TripCardEntity> cardsByUserId = tripCardRepository
                .findByPlanIdAndUserIdIn(plan.getPlanId(), memberUserIds).stream()
                .collect(Collectors.toMap(TripCardEntity::getUserId, Function.identity()));

        if (schedule.isEmpty() && cardsByUserId.isEmpty()) {
            throw new IllegalArgumentException("이 도시에는 아직 일정에 넣을 장소가 없습니다.");
        }

        List<TripCardEntity> newCards = memberUserIds.stream()
                .filter(memberUserId -> !cardsByUserId.containsKey(memberUserId))
                .map(memberUserId -> newTripCard(
                        group, plan, uniquePlaceCount, memberUserId, members.size()))
                .toList();
        List<TripCardEntity> savedCards = tripCardRepository.saveAll(newCards);
        savedCards.forEach(card -> cardsByUserId.put(card.getUserId(), card));

        List<TripCardPlaceEntity> cardPlaces = savedCards.stream()
                .flatMap(card -> schedule.stream()
                        .map(scheduled -> newTripCardPlace(
                                card.getTripCardId(), scheduled.place(),
                                scheduled.tourPlace(),
                                scheduled.date(), scheduled.sortOrder())))
                .toList();
        tripCardPlaceRepository.saveAll(cardPlaces);
        savedCards.forEach(card -> {
            eventPublisher.publishEvent(
                    new TripCardCreatedEvent(card.getTripCardId(), card.getUserId()));
            // 방금 저장한 카드까지 세서 1이면 이 시·도는 처음이다.
            // 지도에 새 구역이 칠해지는 순간이라 알림을 준다 (#162).
            if (tripCardRepository.countByUserIdAndRegionCode(
                    card.getUserId(), card.getRegionCode()) == 1) {
                eventPublisher.publishEvent(
                        new RegionVisitedEvent(card.getRegionCode(), card.getUserId()));
            }
        });

        TripCardEntity ownerCard = cardsByUserId.get(group.getOwnerUserId());
        if (ownerCard == null) throw new IllegalArgumentException("플래너 그룹장 정보를 찾을 수 없습니다.");
        return ownerCard;
    }

    /** 그룹 여행 정보를 멤버 한 명의 여행 카드로 변환한다. */
    private TripCardEntity newTripCard(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            int placeCount,
            String cardOwnerUserId,
            int companionCount
    ) {
        return TripCardEntity.builder()
                .userId(cardOwnerUserId)
                .planId(plan.getPlanId())
                .title(group.getGroupName())
                .regionCode(plan.getRegionCode())
                .cityName(plan.getCityName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .companionCount(companionCount)
                .placeCount(placeCount)
                .photoCount(0)
                // 커버는 사용자가 찍은 사진 중에서 고른다. 아직 사진이 없으니 비워 두고,
                // 사진이 붙을 때 채운다. 관광지 대표 이미지를 대신 넣지 않는다 (팀 결정).
                .coverImageUrl(null)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /** 일정 장소를 여행 카드에 저장할 장소 엔티티로 변환한다. */
    private TripCardPlaceEntity newTripCardPlace(
            Long tripCardId,
            ConfirmedPlaceResponseDto confirmed,
            TourPlaceEntity place,
            LocalDate visitedDate,
            int sortOrder
    ) {
        TripCardPlaceEntity cardPlace = new TripCardPlaceEntity();
        cardPlace.setTripCardId(tripCardId);
        cardPlace.setTourPlaceId(confirmed.getTourPlaceId());
        cardPlace.setPlaceName(confirmed.getPlaceName());
        cardPlace.setAddress(confirmed.getAddress());
        cardPlace.setVisitedDate(visitedDate);
        cardPlace.setLatitude(place == null ? null : place.getLatitude());
        cardPlace.setLongitude(place == null ? null : place.getLongitude());
        cardPlace.setSortOrder(sortOrder);
        return cardPlace;
    }

}
