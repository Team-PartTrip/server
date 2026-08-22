package com.example.PartTrip.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 업로드된 이미지를 /uploads/** 로 정적 서빙한다.
//
// 프로필 사진(uploads/profile)과 해설 카메라 사진(uploads/guide-camera)이
// 모두 이 경로로 나간다. 앱의 Image 컴포넌트는 토큰을 붙이지 않으므로
// SecurityConfig 에서 /uploads/** 를 공개 경로로 둔다.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String rootDir;

    public WebConfig(@Value("${part-trip.upload.root-dir:uploads}") String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + rootDir + "/");
    }
}
