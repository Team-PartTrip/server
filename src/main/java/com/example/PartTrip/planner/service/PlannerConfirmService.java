package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.planner.dto.request.PlannerConfirmRequestDto;
import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerConfirmResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.dto.response.VoteCloseResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerConfirmService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteConfirmService voteConfirmService;
    private final PlannerFinalService plannerFinalService;
    private final TripCardRepository tripCardRepository;
    private final TripCardPlaceRepository tripCardPlaceRepository;
    private final TourPlaceRepository tourPlaceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PlannerConfirmResponseDto confirmPlanner(
            Long plannerId,
            PlannerConfirmRequestDto request,
            String userId
    ) {
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
        List<VoteEntity> votes = voteRepository.findByPlanId(plan.getPlanId());
        if (votes.isEmpty()) {
            throw new IllegalArgumentException("확정할 투표가 없습니다.");
        }

        Map<Long, Long> chosenByVoteId = chosenByVoteId(request, votes);

        for (VoteEntity vote : votes) {
            if (vote.getStatus() == VoteStatus.CONFIRMED) {
                continue;
            }

            VoteCloseResponseDto closed = voteConfirmService.closeVote(
                    plannerId,
                    vote.getVoteId(),
                    userId
            );
            if (closed.getTopOptionIds().isEmpty()) {
                throw new IllegalArgumentException("확정할 후보가 없는 투표가 있습니다.");
            }

            Long chosenOptionId = chosenByVoteId.get(vote.getVoteId());
            // 아무도 투표하지 않은 장바구니는 모든 후보가 0표 동점이다.
            // 고른 것을 함께 보내면 그걸로 확정하고, 없을 때만 막는다.
            if (chosenOptionId == null && Boolean.TRUE.equals(closed.getTied())) {
                // voteId 는 사용자에게 아무 의미가 없다. 어느 투표인지 알려준다.
                throw new IllegalArgumentException(
                        vote.getCategory().getLabel()
                                + " 투표가 동점이에요. 공동 1위 중에서 하나를 먼저 골라주세요.");
            }

            VoteConfirmRequestDto confirmRequest = new VoteConfirmRequestDto();
            confirmRequest.setOptionId(chosenOptionId);
            voteConfirmService.confirmVote(
                    plannerId, vote.getVoteId(), confirmRequest, userId);
        }

        PlannerFinalResponseDto finalResult = plannerFinalService
                .getConfirmedPlaces(plannerId, userId);
        TripCardEntity tripCard = createTripCardsIfAbsent(
                group,
                plan,
                finalResult.getPlaces()
        );

        return PlannerConfirmResponseDto.builder()
                .plannerId(plannerId)
                .confirmedSchedule(finalResult.getPlaces())
                .tripCardId(tripCard.getTripCardId())
                .build();
    }

    /**
     * 요청에 담긴 선택을 투표별로 정리한다.
     *
     * 이 플래너의 투표가 아닌 것을 조용히 버리면, 앱이 엉뚱한 값을 보내도
     * 확정이 그냥 성공해 어디서 틀어졌는지 알 수 없다.
     */
    private Map<Long, Long> chosenByVoteId(
            PlannerConfirmRequestDto request,
            List<VoteEntity> votes
    ) {
        Map<Long, Long> chosen = new HashMap<>();
        if (request == null || request.getSelections() == null) {
            return chosen;
        }

        Set<Long> voteIds = votes.stream()
                .map(VoteEntity::getVoteId)
                .collect(Collectors.toSet());

        for (PlannerConfirmRequestDto.VoteSelection selection : request.getSelections()) {
            // JSON 배열에는 null 이 들어올 수 있다. 그대로 두면 아래에서
            // NullPointerException 이 나 400 이 아니라 500 으로 나간다.
            if (selection == null) {
                throw new IllegalArgumentException("선택 항목이 비어 있습니다.");
            }
            if (selection.getVoteId() == null || selection.getOptionId() == null) {
                throw new IllegalArgumentException("선택에는 voteId 와 optionId 가 모두 필요합니다.");
            }
            if (!voteIds.contains(selection.getVoteId())) {
                throw new IllegalArgumentException(
                        "이 플래너의 투표가 아닙니다. voteId=" + selection.getVoteId());
            }
            // 한 카테고리에서 두 곳을 확정할 수는 없다. 같은 값을 두 번 보낸
            // 것까지 막는다. 한쪽만 허용하면 앱이 무엇을 보냈을 때 통하는지
            // 알 수 없고, 계약이 "투표당 하나" 라는 것도 흐려진다.
            if (chosen.containsKey(selection.getVoteId())) {
                throw new IllegalArgumentException(
                        "한 투표에는 하나만 선택할 수 있습니다. voteId=" + selection.getVoteId());
            }
            chosen.put(selection.getVoteId(), selection.getOptionId());
        }
        return chosen;
    }

    private TripCardEntity createTripCardsIfAbsent(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            List<ConfirmedPlaceResponseDto> places
    ) {
        List<GroupMemberEntity> members = groupMemberRepository
                .findByGroupIdOrderByJoinedAtAsc(group.getGroupId());
        List<String> memberUserIds = members.stream().map(GroupMemberEntity::getUserId).toList();
        Map<String, TripCardEntity> cardsByUserId = tripCardRepository
                .findByPlanIdAndUserIdIn(plan.getPlanId(), memberUserIds).stream()
                .collect(Collectors.toMap(TripCardEntity::getUserId, Function.identity()));

        List<TripCardEntity> newCards = memberUserIds.stream()
                .filter(memberUserId -> !cardsByUserId.containsKey(memberUserId))
                .map(memberUserId -> newTripCard(
                        group, plan, places, memberUserId, members.size()))
                .toList();
        List<TripCardEntity> savedCards = tripCardRepository.saveAll(newCards);
        savedCards.forEach(card -> cardsByUserId.put(card.getUserId(), card));

        Set<Long> tourPlaceIds = places.stream()
                .map(ConfirmedPlaceResponseDto::getTourPlaceId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, TourPlaceEntity> tourPlacesById = tourPlaceRepository.findAllById(tourPlaceIds).stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        List<TripCardPlaceEntity> cardPlaces = savedCards.stream()
                .flatMap(card -> java.util.stream.IntStream.range(0, places.size())
                        .mapToObj(index -> newTripCardPlace(
                                card.getTripCardId(),
                                plan,
                                places.get(index),
                                tourPlacesById.get(places.get(index).getTourPlaceId()),
                                index + 1)))
                .toList();
        tripCardPlaceRepository.saveAll(cardPlaces);
        savedCards.forEach(card -> eventPublisher.publishEvent(
                new TripCardCreatedEvent(card.getTripCardId(), card.getUserId())));

        TripCardEntity ownerCard = cardsByUserId.get(group.getOwnerUserId());
        if (ownerCard == null) throw new IllegalArgumentException("플래너 그룹장 정보를 찾을 수 없습니다.");
        return ownerCard;
    }

    private TripCardEntity newTripCard(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            List<ConfirmedPlaceResponseDto> places,
            String cardOwnerUserId,
            int companionCount
    ) {
        return TripCardEntity.builder()
                .userId(cardOwnerUserId)
                .planId(plan.getPlanId())
                .title(group.getGroupName())
                .countryName(plan.getCountryName())
                .cityName(plan.getCityName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .companionCount(companionCount)
                .placeCount(places.size())
                .photoCount(0)
                // 커버는 사용자가 찍은 사진 중에서 고른다. 아직 사진이 없으니 비워 두고,
                // 사진이 붙을 때 채운다. 관광지 대표 이미지를 대신 넣지 않는다 (팀 결정).
                .coverImageUrl(null)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private TripCardPlaceEntity newTripCardPlace(
            Long tripCardId,
            GroupTravelPlanEntity plan,
            ConfirmedPlaceResponseDto confirmed,
            TourPlaceEntity place,
            int sortOrder
    ) {
        TripCardPlaceEntity cardPlace = new TripCardPlaceEntity();
        cardPlace.setTripCardId(tripCardId);
        cardPlace.setTourPlaceId(confirmed.getTourPlaceId());
        cardPlace.setPlaceName(confirmed.getPlaceName());
        cardPlace.setAddress(confirmed.getAddress());
        cardPlace.setVisitedDate(plan.getStartDate());
        cardPlace.setLatitude(place == null ? null : place.getLatitude());
        cardPlace.setLongitude(place == null ? null : place.getLongitude());
        cardPlace.setSortOrder(sortOrder);
        return cardPlace;
    }
}
