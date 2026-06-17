package com.example.PartTrip.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCreateRequestDto {
    private String photoId;
    private String travelId;
    private String userId;
    private String imageUrl;
    private String latitude;
    private String longitude;
    private String commTitle;
    private String commContent;
}
