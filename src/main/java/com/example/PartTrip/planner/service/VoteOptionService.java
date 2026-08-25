package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.CreateVoteOptionRequestDto;
import com.example.PartTrip.planner.dto.VoteOptionResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteOptionService {

    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional
    public VoteOptionResponseDto addOption(
            Long plannerId,
            Long voteId,
            CreateVoteOptionRequestDto dto,
            String userId
    ) {
        requireMember(plannerId, userId);
        VoteEntity vote = requireVoteOfPlanner(plannerId, voteId);
        validateVoteIsOpen(vote);

        Long tourPlaceId = dto.getTourPlaceId();
        String placeName;

        if (tourPlaceId != null) {
            TourPlaceEntity place = tourPlaceRepository.findById(tourPlaceId)
                    .orElseThrow(() -> new IllegalArgumentException("관광지가 존재하지 않습니다."));

            if (place.getCategory() != null && place.getCategory() != vote.getCategory()) {
                throw new IllegalArgumentException("투표 카테고리와 관광지 카테고리가 일치하지 않습니다.");
            }

            if (voteOptionRepository.existsByVoteIdAndTourPlaceId(voteId, tourPlaceId)) {
                throw new IllegalArgumentException("이미 등록된 관광지 후보입니다.");
            }
            placeName = place.getPlaceName();
        } else {
            placeName = normalizePlaceName(dto.getPlaceName());
            if (voteOptionRepository.existsByVoteIdAndPlaceNameIgnoreCase(voteId, placeName)) {
                throw new IllegalArgumentException("이미 등록된 장소 후보입니다.");
            }
        }

        VoteOptionEntity option = new VoteOptionEntity();
        option.setVoteId(voteId);
        option.setTourPlaceId(tourPlaceId);
        option.setPlaceName(placeName);
        option.setAddedByUserId(userId);
        option.setCreatedAt(LocalDateTime.now());

        return toResponse(voteOptionRepository.save(option));
    }

    @Transactional
    public void deleteOption(
            Long plannerId,
            Long voteId,
            Long optionId,
            String userId
    ) {
        GroupMemberEntity membership = requireMember(plannerId, userId);
        VoteEntity vote = requireVoteOfPlanner(plannerId, voteId);
        validateVoteIsOpen(vote);

        VoteOptionEntity option = voteOptionRepository
                .findByOptionIdAndVoteId(optionId, voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표 후보가 존재하지 않습니다."));

        boolean isOwner = membership.getRole() == GroupRole.OWNER;
        boolean isAddedByMe = userId.equals(option.getAddedByUserId());
        if (!isOwner && !isAddedByMe) {
            throw new IllegalArgumentException("후보를 등록한 사용자 또는 그룹장만 삭제할 수 있습니다.");
        }

        if (voteRecordRepository.existsByOptionId(optionId)) {
            throw new IllegalArgumentException("이미 투표를 받은 후보는 삭제할 수 없습니다.");
        }

        voteOptionRepository.delete(option);
    }

    private GroupMemberEntity requireMember(Long plannerId, String userId) {
        return groupMemberRepository.findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 플래너의 멤버만 후보를 관리할 수 있습니다."
                ));
    }

    private VoteEntity requireVoteOfPlanner(Long plannerId, Long voteId) {
        VoteEntity vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표가 존재하지 않습니다."));

        groupTravelPlanRepository.findByPlanIdAndGroupId(vote.getPlanId(), plannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 투표가 아닙니다."));
        return vote;
    }

    private void validateVoteIsOpen(VoteEntity vote) {
        if (vote.getStatus() != VoteStatus.OPEN) {
            throw new IllegalArgumentException("진행 중인 투표에서만 후보를 관리할 수 있습니다.");
        }

        if (vote.getDeadline() != null && !vote.getDeadline().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("마감된 투표입니다.");
        }
    }

    private String normalizePlaceName(String placeName) {
        if (placeName == null || placeName.isBlank()) {
            throw new IllegalArgumentException("관광지 또는 장소 이름을 입력해주세요.");
        }
        return placeName.trim();
    }

    private VoteOptionResponseDto toResponse(VoteOptionEntity option) {
        return VoteOptionResponseDto.builder()
                .optionId(option.getOptionId())
                .voteId(option.getVoteId())
                .tourPlaceId(option.getTourPlaceId())
                .placeName(option.getPlaceName())
                .addedByUserId(option.getAddedByUserId())
                .createdAt(option.getCreatedAt())
                .build();
    }
}
