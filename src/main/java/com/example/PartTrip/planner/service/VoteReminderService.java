package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.planner.dto.response.VoteReminderResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteReminderService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final NotificationWriter notificationWriter;

    @Transactional(readOnly = true)
    public VoteReminderResponseDto remind(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        GroupMemberEntity requester = groupMemberRepository.findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));
        if (requester.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 재촉 알림을 보낼 수 있습니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));
        List<VoteEntity> openVotes = voteRepository.findByPlanId(plan.getPlanId()).stream()
                .filter(vote -> vote.getStatus() == VoteStatus.OPEN)
                .toList();
        if (openVotes.isEmpty()) {
            throw new IllegalArgumentException("진행 중인 투표가 없습니다.");
        }

        List<Long> voteIds = openVotes.stream().map(VoteEntity::getVoteId).toList();
        Map<String, Set<Long>> votedIdsByUser = voteRecordRepository.findByVoteIdIn(voteIds).stream()
                .collect(Collectors.groupingBy(
                        VoteRecordEntity::getUserId,
                        Collectors.mapping(VoteRecordEntity::getVoteId, Collectors.toSet())
                ));
        List<String> targets = groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(plannerId).stream()
                .map(GroupMemberEntity::getUserId)
                .filter(memberId -> !memberId.equals(userId))
                .filter(memberId -> votedIdsByUser.getOrDefault(memberId, Set.of()).size() < voteIds.size())
                .toList();

        notificationWriter.writeAll(
                targets,
                NotificationType.VOTE_REMINDER,
                "아직 참여하지 않은 투표가 있어요",
                (group.getGroupName() == null || group.getGroupName().isBlank()
                        ? "여행 플래너"
                        : group.getGroupName()) + "의 투표를 확인해주세요.",
                "PLANNER",
                plannerId
        );
        return VoteReminderResponseDto.builder()
                .notifiedCount(targets.size())
                .message(targets.isEmpty() ? "모든 멤버가 투표를 완료했습니다." : targets.size() + "명에게 알림을 보냈습니다.")
                .build();
    }
}
