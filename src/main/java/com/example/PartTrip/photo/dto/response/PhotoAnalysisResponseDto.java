package com.example.PartTrip.photo.dto.response;

import com.example.PartTrip.photo.entity.PhotoAnalysisEntity;
import com.example.PartTrip.photo.enums.PhotoAnalysisAccuracyCategory;
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
    private String source;
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
                .source(photoAnalysis.getSource())
                .photoAnalysisAccuracyCategory(photoAnalysis.getPhotoAnalysisAccuracyCategory())
                .build();
}

