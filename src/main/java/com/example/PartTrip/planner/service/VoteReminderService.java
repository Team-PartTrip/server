package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.event.VoteReminderEvent;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteReminderService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public void remind(Long plannerId, String userId) {
        if (!travelGroupRepository.existsById(plannerId)) {
            throw new IllegalArgumentException("플래너가 존재하지 않습니다.");
        }
        GroupMemberEntity member = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));
        if (member.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 재촉 알림을 보낼 수 있습니다.");
        }
        var plan = groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));
        boolean hasOpenVote = voteRepository.findByPlanId(plan.getPlanId()).stream()
                .anyMatch(vote -> vote.getStatus()
                        == com.example.PartTrip.planner.enums.VoteStatus.OPEN);
        if (!hasOpenVote) {
            throw new IllegalArgumentException("진행 중인 투표가 없습니다.");
        }
        eventPublisher.publishEvent(new VoteReminderEvent(plannerId, userId));
    }
}
