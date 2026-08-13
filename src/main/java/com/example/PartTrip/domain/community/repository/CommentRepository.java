package com.example.PartTrip.domain.community.repository;

import com.example.PartTrip.domain.community.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByTargetTypeAndTargetIdOrderByCreateDateAsc(
            String targetType, Long targetId);
    long countByTargetTypeAndTargetId(String targetType, Long targetId);
    void deleteByTargetTypeAndTargetId(String targetType, Long targetId);
    void deleteByParentCommentId(Long parentCommentId);
}
