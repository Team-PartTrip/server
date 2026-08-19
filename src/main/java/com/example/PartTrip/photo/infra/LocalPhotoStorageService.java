package com.example.PartTrip.photo.infra;

import com.example.PartTrip.photo.service.PhotoStorageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalPhotoStorageService implements PhotoStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadDirectory;
    private final String publicPathPrefix;

    public LocalPhotoStorageService(
            @Value("${part-trip.photo.upload-dir:uploads/guide-camera}") String uploadDir,
            @Value("${part-trip.photo.public-path-prefix:/uploads/guide-camera}") String publicPathPrefix
    ) {
        this.uploadDirectory = Path.of(uploadDir);
        this.publicPathPrefix = publicPathPrefix;
    }

    @Override
    public String store(MultipartFile imageFile) {
        validate(imageFile);

        try {
            Files.createDirectories(uploadDirectory);
            String storedFileName = UUID.randomUUID() + getExtension(imageFile.getOriginalFilename());
            Path targetPath = uploadDirectory.resolve(storedFileName).normalize();
            imageFile.transferTo(targetPath);
            return publicPathPrefix + "/" + storedFileName;
        } catch (IOException exception) {
            throw new IllegalArgumentException("이미지 저장에 실패했습니다.");
        }
    }

    private void validate(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일은 필수입니다.");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(imageFile.getContentType())) {
            throw new IllegalArgumentException("jpg, png, webp 이미지만 업로드할 수 있습니다.");
        }
    }

    private String getExtension(String originalFilename) {
        String filename = StringUtils.cleanPath(originalFilename == null ? "" : originalFilename);
        int dotIndex = filename.lastIndexOf(".");
        return dotIndex >= 0 ? filename.substring(dotIndex) : "";
    }
}
