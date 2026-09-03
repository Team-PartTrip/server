package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.TravelGroupEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TravelGroupRepository extends JpaRepository<TravelGroupEntity, Long> {

    // 초대 코드로 그룹 찾기
    Optional<TravelGroupEntity> findByInviteCode(String inviteCode);

    // 동시에 여러 사용자가 참여하더라도 설정 인원을 넘지 않도록 그룹 행을 잠근다
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM TravelGroupEntity g WHERE g.inviteCode = :inviteCode")
    Optional<TravelGroupEntity> findByInviteCodeForUpdate(
            @Param("inviteCode") String inviteCode
    );

    boolean existsByInviteCode(String inviteCode);

    // 수정 시 소유자까지 함께 확인한다
    Optional<TravelGroupEntity> findByGroupIdAndOwnerUserId(Long groupId, String ownerUserId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM TravelGroupEntity g WHERE g.groupId = :groupId")
    Optional<TravelGroupEntity> findByIdForUpdate(@Param("groupId") Long groupId);
}
