package com.example.PartTrip.photo.service;

import com.example.PartTrip.photo.entity.PhotoAnalysisEntity;
import com.example.PartTrip.photo.entity.PhotoEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoAnalyzer {
    PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile);
}
