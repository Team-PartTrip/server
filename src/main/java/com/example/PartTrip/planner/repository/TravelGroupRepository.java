package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.TravelGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelGroupRepository extends JpaRepository<TravelGroupEntity, Long> {

    // 초대 코드로 그룹 찾기
    Optional<TravelGroupEntity> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    // 수정 시 소유자까지 함께 확인한다
    Optional<TravelGroupEntity> findByGroupIdAndOwnerUserId(Long groupId, String ownerUserId);
}
