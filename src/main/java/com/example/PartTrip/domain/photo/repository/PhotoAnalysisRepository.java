package com.example.PartTrip.domain.photo.repository;

import com.example.PartTrip.domain.photo.entity.PhotoAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoAnalysisRepository extends JpaRepository<PhotoAnalysisEntity, Long> {

    Optional<PhotoAnalysisEntity> findByPhotoPhotoIdAndPhotoUserUserId(Long photoId, String userId);
    Optional<PhotoAnalysisEntity> findByPhotoPhotoId(Long photoId);

}
