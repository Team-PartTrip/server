package com.example.PartTrip.planner.service;

import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 확정된 일정 조회 (C8).
 *
 * 일정은 확정할 때 멤버마다 만든 여행 카드에 날짜 · 순서와 함께 저장된다.
 * 투표를 없앤 뒤(#161)로는 여기가 확정 일정을 읽을 수 있는 유일한 곳이다.
 */
@Service
@RequiredArgsConstructor
public class PlannerFinalService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final TripCardRepository tripCardRepository;
    private final TripCardPlaceRepository tripCardPlaceRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional(readOnly = true)
    public PlannerFinalResponseDto getConfirmedPlaces(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 최종 장소를 조회할 수 있습니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));

        // 내 카드를 먼저 본다. 내가 카드를 지웠으면 다른 멤버의 카드도 같은 일정이다.
        List<TripCardEntity> cards = tripCardRepository.findByPlanIdIn(List.of(plan.getPlanId()));
        TripCardEntity card = cards.stream()
                .filter(c -> userId.equals(c.getUserId()))
                .findFirst()
                .or(() -> cards.stream().findFirst())
                .orElseThrow(() -> new IllegalArgumentException("아직 일정이 확정되지 않았습니다."));

        List<TripCardPlaceEntity> cardPlaces = tripCardPlaceRepository
                .findByTripCardIdOrderByVisitedDateAscSortOrderAsc(card.getTripCardId());
        Map<Long, TourPlaceEntity> tourPlaces = tourPlaceRepository
                .findAllById(cardPlaces.stream()
                        .map(TripCardPlaceEntity::getTourPlaceId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));

        return PlannerFinalResponseDto.builder()
                .plannerId(group.getGroupId())
                .title(group.getGroupName())
                .regionCode(plan.getRegionCode())
                .regionName(RegionCode.nameOf(plan.getRegionCode()))
                .cityName(plan.getCityName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .status(group.getStatus().name())
                .places(cardPlaces.stream()
                        .map(place -> toConfirmedPlace(place, tourPlaces.get(place.getTourPlaceId())))
                        .toList())
                .build();
    }

    private ConfirmedPlaceResponseDto toConfirmedPlace(
            TripCardPlaceEntity place,
            TourPlaceEntity tourPlace
    ) {
        boolean hasCategory = tourPlace != null && tourPlace.getCategory() != null;
        return ConfirmedPlaceResponseDto.builder()
                .category(hasCategory ? tourPlace.getCategory().name() : null)
                .categoryLabel(hasCategory ? tourPlace.getCategory().getLabel() : null)
                .tourPlaceId(place.getTourPlaceId())
                .placeName(place.getPlaceName())
                .imageUrl(tourPlace == null ? null : tourPlace.getImageUrl())
                .address(place.getAddress())
                .rating(tourPlace == null ? null : tourPlace.getRating())
                .visitedDate(place.getVisitedDate())
                .build();
    }
}
