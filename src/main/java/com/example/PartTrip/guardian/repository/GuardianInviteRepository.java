package com.example.PartTrip.guardian.repository;

import com.example.PartTrip.guardian.entity.GuardianInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface GuardianInviteRepository extends JpaRepository<GuardianInviteEntity, String> {

    /** 같은 코드를 두 사람이 동시에 받아도 한 명만 연결되게 잠근다 */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM GuardianInviteEntity i WHERE i.code = :code")
    Optional<GuardianInviteEntity> findByCodeForUpdate(String code);
}
