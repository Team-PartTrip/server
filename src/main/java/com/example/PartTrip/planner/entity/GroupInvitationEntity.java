package com.example.PartTrip.planner.entity;

import com.example.PartTrip.planner.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "group_invitation",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_group_invitation_group_user",
                columnNames = {"group_id", "invited_user_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class GroupInvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long invitationId;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "invited_user_id", nullable = false)
    private String invitedUserId;

    @Column(name = "invited_by_user_id", nullable = false)
    private String invitedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvitationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
}
