package com.example.PartTrip.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideCameraImageUploadResponseDto {
    private Long imageId;
    private Long analysisId;

    public static GuideCameraImageUploadResponseDto of(Long imageId, Long analysisId) {
        return GuideCameraImageUploadResponseDto.builder()
                .imageId(imageId)
                .analysisId(analysisId)
                .build();
    }
}
