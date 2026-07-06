package com.example.PartTrip.dto.photo;

import com.example.PartTrip.entity.photo.PhotoAnalysisEntity;
import com.example.PartTrip.enums.photo.PhotoAnalysisAccuracyCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoAnalysisResponseDto {
    private Long analysisId;
    private Long photoId;
    private String title;
    private String era;
    private String designation;
    private String overview;
    private String background;
    private String features;
    private String current_status;
    private String sourceName;
    private String sourceUrl;
    private PhotoAnalysisAccuracyCategory photoAnalysisAccuracyCategory;

    public static PhotoAnalysisResponseDto from(PhotoAnalysisEntity photoAnalysis) {
        return PhotoAnalysisResponseDto.builder()
                .analysisId(photoAnalysis.getAnalysisId())
                .photoId(photoAnalysis.getPhoto().getPhotoId())
                .title(photoAnalysis.getTitle())
                .era(photoAnalysis.getEra())
                .designation(photoAnalysis.getDesignation())
                .overview(photoAnalysis.getOverview())
                .background(photoAnalysis.getBackground())
                .features(photoAnalysis.getFeatures())
                .current_status(photoAnalysis.getCurrentStatus())
                .sourceName(photoAnalysis.getSourceName())
                .sourceUrl(photoAnalysis.getSourceUrl())
                .photoAnalysisAccuracyCategory(photoAnalysis.getPhotoAnalysisAccuracyCategory())
                .build();
    }
}

