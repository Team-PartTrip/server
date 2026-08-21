package com.example.PartTrip.photo.repository;

import com.example.PartTrip.photo.entity.PhotoCommentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoCommentHistoryRepository
        extends JpaRepository<PhotoCommentHistoryEntity, Long> {

    // 앱 D5 "수정 이력" — 최초 작성부터 순서대로
    List<PhotoCommentHistoryEntity> findByPhotoIdOrderByRevisionAsc(Long photoId);

    long countByPhotoId(Long photoId);
}
