package com.example.PartTrip.community.controller;

import com.example.PartTrip.community.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/images")
public class ImageController {

    private final ImageStorageService imageStorageService;

    // 게시글/리뷰/일정용 이미지 업로드. 반환된 url을 작성/수정 요청의 images 목록에 담아서 사용
    @PostMapping
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String url = imageStorageService.store(file);
        return Map.of("url", url);
    }
}
