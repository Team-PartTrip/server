package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
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
import java.util.List;

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
            if (Boolean.TRUE.equals(closed.getTied())) {
                throw new IllegalArgumentException(
                        "동점 투표가 있습니다. 그룹장이 공동 1위 후보를 먼저 선택해주세요. voteId="
                                + vote.getVoteId());
            }

            VoteConfirmRequestDto request = new VoteConfirmRequestDto();
            voteConfirmService.confirmVote(plannerId, vote.getVoteId(), request, userId);
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

    private TripCardEntity createTripCardsIfAbsent(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            List<ConfirmedPlaceResponseDto> places
    ) {
        List<GroupMemberEntity> members = groupMemberRepository
                .findByGroupIdOrderByJoinedAtAsc(group.getGroupId());
        TripCardEntity ownerCard = null;
        for (GroupMemberEntity member : members) {
            TripCardEntity card = tripCardRepository
                    .findByPlanIdAndUserId(plan.getPlanId(), member.getUserId())
                    .orElseGet(() -> createTripCard(
                            group,
                            plan,
                            places,
                            member.getUserId()));
            if (member.getUserId().equals(group.getOwnerUserId())) {
                ownerCard = card;
            }
        }
        if (ownerCard == null) {
            throw new IllegalArgumentException("플래너 그룹장 정보를 찾을 수 없습니다.");
        }
        return ownerCard;
    }

    private TripCardEntity createTripCard(
            TravelGroupEntity group,
            GroupTravelPlanEntity plan,
            List<ConfirmedPlaceResponseDto> places,
            String cardOwnerUserId
    ) {
        TripCardEntity card = new TripCardEntity();
        card.setUserId(cardOwnerUserId);
        card.setPlanId(plan.getPlanId());
        card.setTitle(group.getGroupName());
        card.setCountryName(plan.getCountryName());
        card.setCityName(plan.getCityName());
        card.setStartDate(plan.getStartDate());
        card.setEndDate(plan.getEndDate());
        card.setCompanionCount((int) groupMemberRepository.countByGroupId(group.getGroupId()));
        card.setPlaceCount(places.size());
        card.setPhotoCount(0);
        card.setCoverImageUrl(places.stream()
                .map(ConfirmedPlaceResponseDto::getImageUrl)
                .filter(url -> url != null && !url.isBlank())
                .findFirst()
                .orElse(null));
        card.setCreatedAt(LocalDateTime.now());
        TripCardEntity savedCard = tripCardRepository.save(card);

        for (int index = 0; index < places.size(); index++) {
            ConfirmedPlaceResponseDto confirmed = places.get(index);
            TourPlaceEntity place = confirmed.getTourPlaceId() == null
                    ? null
                    : tourPlaceRepository.findById(confirmed.getTourPlaceId()).orElse(null);

            TripCardPlaceEntity cardPlace = new TripCardPlaceEntity();
            cardPlace.setTripCardId(savedCard.getTripCardId());
            cardPlace.setTourPlaceId(confirmed.getTourPlaceId());
            cardPlace.setPlaceName(confirmed.getPlaceName());
            cardPlace.setAddress(confirmed.getAddress());
            cardPlace.setVisitedDate(plan.getStartDate());
            cardPlace.setLatitude(place == null ? null : place.getLatitude());
            cardPlace.setLongitude(place == null ? null : place.getLongitude());
            cardPlace.setSortOrder(index + 1);
            tripCardPlaceRepository.save(cardPlace);
        }

        eventPublisher.publishEvent(
                new TripCardCreatedEvent(savedCard.getTripCardId(), cardOwnerUserId)
        );
        return savedCard;
    }
}
