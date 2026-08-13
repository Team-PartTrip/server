package com.example.PartTrip.application.photo.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptApiPhotoAnalysisResultDto {
    private String title;
    private String era;
    private String designation;
    private String overview;
    private String background;
    private String features;
    private String currentStatus;
}
