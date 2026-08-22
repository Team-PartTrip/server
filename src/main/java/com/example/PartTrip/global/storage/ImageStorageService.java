package com.example.PartTrip.global.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

// 업로드 이미지를 로컬 디스크에 저장하고 공개 URL 을 돌려준다.
//
// 하위 디렉토리를 인자로 받아 용도별로 나눈다. (profile, guide-camera ...)
// 저장 결과는 /uploads/{subDirectory}/{uuid}.{ext} 형태이며,
// 이 경로는 WebConfig 의 정적 리소스 핸들러가 서빙한다.
@Service
public class ImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    private final Path rootDirectory;
    private final String publicPathPrefix;

    public ImageStorageService(
            @Value("${part-trip.upload.root-dir:uploads}") String rootDir,
            @Value("${part-trip.upload.public-path-prefix:/uploads}") String publicPathPrefix
    ) {
        this.rootDirectory = Path.of(rootDir);
        this.publicPathPrefix = publicPathPrefix;
    }

    public String store(MultipartFile imageFile, String subDirectory) {
        validate(imageFile);

        try {
            Path targetDirectory = rootDirectory.resolve(subDirectory).normalize();
            Files.createDirectories(targetDirectory);

            String storedFileName = UUID.randomUUID() + getExtension(imageFile.getOriginalFilename());
            imageFile.transferTo(targetDirectory.resolve(storedFileName).normalize());

            return publicPathPrefix + "/" + subDirectory + "/" + storedFileName;
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
