package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.TripCardPhotoEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TripCardEntryResponse {

    private Long entryId;
    private String imageUrl;
    private LocalDateTime takenAt;
    private Double latitude;
    private Double longitude;

    public static TripCardEntryResponse from(TripCardPhotoEntity photo) {
        return TripCardEntryResponse.builder()
                .entryId(photo.getTripCardPhotoId())
                .imageUrl(photo.getImageUrl())
                .takenAt(photo.getTakenAt())
                .latitude(photo.getLatitude())
                .longitude(photo.getLongitude())
                .build();
    }
}
