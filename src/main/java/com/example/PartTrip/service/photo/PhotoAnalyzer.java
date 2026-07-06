package com.example.PartTrip.service.photo;

import com.example.PartTrip.entity.photo.PhotoAnalysisEntity;
import com.example.PartTrip.entity.photo.PhotoEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoAnalyzer {
    PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile);
}
