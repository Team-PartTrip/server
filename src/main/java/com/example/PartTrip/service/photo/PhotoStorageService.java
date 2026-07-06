package com.example.PartTrip.service.photo;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    String store(MultipartFile imageFile);
}
