package com.example.PartTrip.community.repository;

import com.example.PartTrip.community.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {
    List<PostImageEntity> findByTargetTypeAndTargetIdOrderBySortOrderAsc(String targetType, Long targetId);
    void deleteByTargetTypeAndTargetId(String targetType, Long targetId);
}
