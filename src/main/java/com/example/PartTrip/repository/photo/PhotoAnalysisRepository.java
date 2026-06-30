package com.example.PartTrip.repository.photo;

import com.example.PartTrip.entity.photo.PhotoAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoAnalysisRepository extends JpaRepository<PhotoAnalysisEntity, Long> {
    Optional<PhotoAnalysisEntity> findByPhotoPhotoIdAndPhotoUserUserId(Long photoId, String userId);
}
