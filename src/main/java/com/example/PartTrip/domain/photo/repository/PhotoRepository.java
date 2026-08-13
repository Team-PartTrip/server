package com.example.PartTrip.domain.photo.repository;

import com.example.PartTrip.domain.photo.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    Optional<PhotoEntity> findByPhotoIdAndUserUserId(Long photoId, String userId);
}
