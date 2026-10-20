package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerMemberResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.InvitationStatus;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerMemberListServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupInvitationRepository groupInvitationRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private PlannerMemberListService service;

    @Test
    void 멤버의_프로필_사진_주소를_함께_준다() {
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "owner")).thenReturn(true);
        when(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(1L))
                .thenReturn(List.of(member("owner", GroupRole.OWNER), member("friend", GroupRole.MEMBER)));
        when(groupInvitationRepository.findByGroupIdAndStatusOrderByCreatedAtAsc(1L, InvitationStatus.PENDING))
                .thenReturn(List.of());
        when(userRepository.findAllById(anyList()))
                .thenReturn(List.of(user("owner", "/uploads/owner.jpg"), user("friend", null)));

        List<PlannerMemberResponseDto> members = service.getPlannerMembers(1L, "owner");

        assertThat(members).extracting(PlannerMemberResponseDto::getImgUrl)
                .containsExactly("/uploads/owner.jpg", null);
    }

    private static GroupMemberEntity member(String userId, GroupRole role) {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(1L);
        member.setUserId(userId);
        member.setRole(role);
        member.setJoinedAt(LocalDateTime.of(2026, 10, 1, 9, 0));
        return member;
    }

    private static UserEntity user(String userId, String imgUrl) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setNickName(userId);
        user.setImgUrl(imgUrl);
        return user;
    }
}
