package com.example.PartTrip.community.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploads/community";

    // 이미지 파일을 로컬 디스크에 저장하고, 앱에서 접근 가능한 URL 경로를 반환
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        try {
            Path dir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(dir);

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            String fileName = UUID.randomUUID() + ext;

            Path target = dir.resolve(fileName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/images/" + fileName;
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 저장에 실패했습니다.");
        }
    }
}
