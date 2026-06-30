package com.example.PartTrip.service.photo;

import com.example.PartTrip.dto.photo.NearbyPlaceRecommendationResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NearbyPlaceRecommendationService {

    public List<NearbyPlaceRecommendationResponseDto> recommend(BigDecimal latitude, BigDecimal longitude) {
        return List.of(
                NearbyPlaceRecommendationResponseDto.builder()
                        .name("주변 명소 추천 준비 중")
                        .description("관광지 또는 지도 API 키가 연결되면 현재 위치 기반 추천 결과로 교체됩니다.")
                        .latitude(latitude)
                        .longitude(longitude)
                        .distanceMeters(0)
                        .sourceName("PartTrip")
                        .sourceUrl("https://example.com")
                        .build()
        );
    }
}
