package com.example.PartTrip.planner.entity;

import com.example.PartTrip.planner.enums.GroupRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 그룹 멤버 (Func-008-01)
// 같은 사용자가 한 그룹에 두 번 들어가지 않도록 (group_id, user_id) 를 유니크로 둔다
@Entity
@Table(
        name = "group_member",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_group_member_group_user",
                columnNames = {"group_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class GroupMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long groupMemberId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private GroupRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}
