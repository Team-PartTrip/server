package com.example.PartTrip.repository.photo;

import com.example.PartTrip.entity.photo.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    Optional<PhotoEntity> findByPhotoIdAndUserUserId(Long photoId, String userId);
}
