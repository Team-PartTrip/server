package com.example.PartTrip.community.repository;

import com.example.PartTrip.community.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByTargetTypeAndTargetIdAndUserId(String targetType, Long targetId, String userId);
    long countByTargetTypeAndTargetId(String targetType, Long targetId);
    void deleteByTargetTypeAndTargetId(String targetType, Long targetId);
}
