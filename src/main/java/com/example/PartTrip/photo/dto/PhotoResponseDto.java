package com.example.PartTrip.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponseDto {
    private Long photoId;
    private Long travelId;
    private Long userId;
    private String latitude;
    private String longitude;
    private String commTitle;
    private String commContent;
    private LocalDateTime createDate;
}
