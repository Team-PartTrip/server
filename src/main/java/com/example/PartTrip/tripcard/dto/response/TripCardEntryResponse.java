package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.MetadataSource;
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
    private String placeName;
    // 앱이 "이 사진은 아직 고칠 수 있다" 를 판단하는 값. EXIF 면 잠긴 값이다.
    private MetadataSource locationSource;
    private MetadataSource takenAtSource;

    public static TripCardEntryResponse from(TripCardPhotoEntity photo) {
        return TripCardEntryResponse.builder()
                .entryId(photo.getTripCardPhotoId())
                .imageUrl(photo.getImageUrl())
                .takenAt(photo.getTakenAt())
                .latitude(photo.getLatitude())
                .longitude(photo.getLongitude())
                .placeName(photo.getPlaceName())
                .locationSource(photo.getLocationSource())
                .takenAtSource(photo.getTakenAtSource())
                .build();
    }
}
