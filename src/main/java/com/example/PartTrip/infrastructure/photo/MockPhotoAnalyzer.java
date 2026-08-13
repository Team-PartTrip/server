package com.example.PartTrip.infrastructure.photo;

import com.example.PartTrip.domain.photo.service.PhotoAnalyzer;

import com.example.PartTrip.domain.photo.entity.PhotoAnalysisEntity;
import com.example.PartTrip.domain.photo.entity.PhotoEntity;
import com.example.PartTrip.domain.photo.enums.PhotoAnalysisAccuracyCategory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MockPhotoAnalyzer implements PhotoAnalyzer {

    @Override
    public PhotoAnalysisEntity analyze(PhotoEntity photo, MultipartFile imageFile) {
        return PhotoAnalysisEntity.builder()
                .photo(photo)
                .title("분석 준비 중")
                .era("확인 필요")
                .designation("확인 필요")
                .overview("AI API 키가 연결되면 업로드된 사진을 기반으로 해설이 생성됩니다.")
                .background("현재는 외부 API 없이도 해설 카메라 흐름을 테스트할 수 있는 임시 분석 결과입니다.")
                .features("사진 저장, 분석 결과 조회, 기록 저장 흐름을 먼저 검증할 수 있습니다.")
                .currentStatus("API 연동 전")
                .sourceName("PartTrip Mock Analyzer")
                .sourceUrl("https://example.com")
                .photoAnalysisAccuracyCategory(PhotoAnalysisAccuracyCategory.FAILED_TO_IDENTIFY)
                .build();
    }
}
