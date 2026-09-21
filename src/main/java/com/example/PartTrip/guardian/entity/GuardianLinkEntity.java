package com.example.PartTrip.guardian.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "guardian_link",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_guardian_link_pair",
                columnNames = {"senior_user_id", "guardian_user_id"})
)
@Getter
@NoArgsConstructor
public class GuardianLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "senior_user_id", nullable = false)
    private String seniorUserId;

    @Column(name = "guardian_user_id", nullable = false)
    private String guardianUserId;

    @Column(name = "linked_at", nullable = false)
    private LocalDateTime linkedAt;

    public GuardianLinkEntity(String seniorUserId, String guardianUserId, LocalDateTime linkedAt) {
        this.seniorUserId = seniorUserId;
        this.guardianUserId = guardianUserId;
        this.linkedAt = linkedAt;
    }
}
