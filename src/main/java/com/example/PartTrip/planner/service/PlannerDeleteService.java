package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 플래너 삭제 (API-005-12).
 *
 * 이 DB 에는 travel_group 을 가리키는 외래키가 없다. 엔티티가 id 를 값으로만
 * 들고 있어서 연쇄 삭제가 걸리지 않는다. 그래서 자식부터 순서대로 지운다.
 * 그룹만 지우면 투표·멤버·초대가 전부 고아로 남는다.
 */
@Service
@RequiredArgsConstructor
public class PlannerDeleteService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final TripCardRepository tripCardRepository;

    @Transactional
    public void deletePlanner(Long plannerId, String userId) {
        if (!travelGroupRepository.existsById(plannerId)) {
            throw new NotFoundException("플래너가 존재하지 않습니다.");
        }
        // 남의 플래너를 지우려는 것도 "권한 없음" 이다. 404 로 답하면
        // 그 id 의 플래너가 있는지 없는지를 알려주는 셈이 된다.
        GroupMemberEntity member = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new ForbiddenException("해당 플래너의 멤버가 아닙니다."));
        if (member.getRole() != GroupRole.OWNER) {
            throw new ForbiddenException("플래너 그룹장만 플래너를 삭제할 수 있습니다.");
        }

        List<Long> planIds = groupTravelPlanRepository
                .findByGroupIdOrderByStartDateDesc(plannerId)
                .stream()
                .map(GroupTravelPlanEntity::getPlanId)
                .toList();

        if (!planIds.isEmpty()) {
            // 확정된 플래너는 여행 카드를 만든다. 카드는 사용자의 기록이라
            // 함께 지우지 않고 계획과의 연결만 끊는다.
            // 카드를 지우려면 여행 카드 삭제(API-003-05)를 따로 쓴다.
            List<TripCardEntity> cards = tripCardRepository.findByPlanIdIn(planIds);
            cards.forEach(card -> card.setPlanId(null));

            List<Long> voteIds = voteRepository.findByPlanIdIn(planIds).stream()
                    .map(vote -> vote.getVoteId())
                    .toList();
            if (!voteIds.isEmpty()) {
                voteRecordRepository.deleteByVoteIdIn(voteIds);
                voteOptionRepository.deleteByVoteIdIn(voteIds);
                voteRepository.deleteByPlanIdIn(planIds);
            }
            groupTravelPlanRepository.deleteByGroupId(plannerId);
        }

        groupInvitationRepository.deleteByGroupId(plannerId);
        groupMemberRepository.deleteByGroupId(plannerId);
        travelGroupRepository.deleteById(plannerId);
    }
}
