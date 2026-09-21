package com.example.PartTrip.guardian.repository;

import com.example.PartTrip.guardian.entity.GuardianLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuardianLinkRepository extends JpaRepository<GuardianLinkEntity, Long> {

    List<GuardianLinkEntity> findByGuardianUserIdOrderByLinkedAtAsc(String guardianUserId);

    List<GuardianLinkEntity> findBySeniorUserIdOrderByLinkedAtAsc(String seniorUserId);

    Optional<GuardianLinkEntity> findBySeniorUserIdAndGuardianUserId(String seniorUserId, String guardianUserId);

    boolean existsBySeniorUserIdAndGuardianUserId(String seniorUserId, String guardianUserId);
}
