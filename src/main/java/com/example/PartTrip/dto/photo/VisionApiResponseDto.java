package com.example.PartTrip.dto.photo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisionApiResponseDto {
    // 사진 분석 결과 이름
    private String recognizedName;
    // 분석 정확도
    private float confidence;
}
