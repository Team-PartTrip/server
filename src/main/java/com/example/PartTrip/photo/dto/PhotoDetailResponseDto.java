package com.example.PartTrip.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 응답 결과 바로 전달할 수 있도록 만들었긴 한데, 삭제될 수도 있음
public class PhotoDetailResponseDto {
    private PhotoResponseDto photo;
    private PhotoAnalysisResponseDto analysis;
}
