package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.GroupInvitationEntity;
import com.example.PartTrip.planner.enums.InvitationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupInvitationRepository extends JpaRepository<GroupInvitationEntity, Long> {

    Optional<GroupInvitationEntity> findByGroupIdAndInvitedUserId(Long groupId, String invitedUserId);

    List<GroupInvitationEntity> findByGroupIdAndInvitedUserIdIn(Long groupId, List<String> invitedUserIds);

    List<GroupInvitationEntity> findByInvitedUserIdAndStatusOrderByCreatedAtDesc(
            String invitedUserId,
            InvitationStatus status
    );

    List<GroupInvitationEntity> findByGroupIdAndStatusOrderByCreatedAtAsc(
            Long groupId,
            InvitationStatus status
    );

    long countByGroupIdAndStatus(Long groupId, InvitationStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM GroupInvitationEntity i WHERE i.invitationId = :invitationId")
    Optional<GroupInvitationEntity> findByIdForUpdate(@Param("invitationId") Long invitationId);
}
