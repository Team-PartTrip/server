package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerMemberResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerMemberListService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PlannerMemberResponseDto> getPlannerMembers(
            Long plannerId,
            String userId
    ) {
        if (!travelGroupRepository.existsById(plannerId)) {
            throw new IllegalArgumentException("플래너가 존재하지 않습니다.");
        }

        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 조회할 수 있습니다.");
        }

        List<GroupMemberEntity> members = groupMemberRepository
                .findByGroupIdOrderByJoinedAtAsc(plannerId);

        Map<String, UserEntity> usersById = userRepository
                .findAllById(members.stream().map(GroupMemberEntity::getUserId).toList())
                .stream()
                .collect(Collectors.toMap(UserEntity::getUserId, Function.identity()));

        return members.stream()
                .sorted(Comparator
                        .comparingInt((GroupMemberEntity member) -> roleOrder(member.getRole()))
                        .thenComparing(GroupMemberEntity::getJoinedAt))
                .map(member -> toResponse(member, usersById.get(member.getUserId())))
                .toList();
    }

    private int roleOrder(GroupRole role) {
        return role == GroupRole.OWNER ? 0 : 1;
    }

    private PlannerMemberResponseDto toResponse(
            GroupMemberEntity member,
            UserEntity user
    ) {
        return PlannerMemberResponseDto.builder()
                .userId(member.getUserId())
                .nickName(user == null ? "알 수 없는 사용자" : user.getNickName())
                .role(member.getRole().name())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
