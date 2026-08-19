package com.example.PartTrip.photo.service;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    String store(MultipartFile imageFile);
}
