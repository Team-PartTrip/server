package com.example.PartTrip.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    String store(MultipartFile imageFile);
}
