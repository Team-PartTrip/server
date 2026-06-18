package com.example.PartTrip.photo.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 한국 문화재는 공공데이터포털 API로 대체 예정
@Getter
@NoArgsConstructor
public class WikipediaApiResponseDto {
    private String recognizedName;
    private String content;
}
