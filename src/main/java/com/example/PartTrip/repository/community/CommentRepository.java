package com.example.PartTrip.repository.community;

import com.example.PartTrip.entity.community.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardIdOrderByCreateDateAsc(Long boardId);
    long countByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}
