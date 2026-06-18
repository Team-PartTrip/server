package com.example.PartTrip.photo.dto.response;

import com.example.PartTrip.photo.enums.PhotoAnalysisAccuracyCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoAnalysisResponse {
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
}
