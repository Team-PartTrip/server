package com.example.PartTrip.domain.photo.service;

import com.example.PartTrip.domain.photo.entity.PhotoAnalysisEntity;
import com.example.PartTrip.domain.photo.entity.PhotoEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoAnalyzer {
    PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile);
}
