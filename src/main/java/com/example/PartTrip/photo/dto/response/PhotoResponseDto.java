package com.example.PartTrip.photo.dto.response;

import com.example.PartTrip.photo.entity.PhotoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponseDto {
    private String photoId;
    private String travelId;
    private String userId;
    private MultipartFile imageFile;
    private String latitude;
    private String longitude;
    private String commTitle;
    private String commContent;
    private LocalDate photoDate;
    private LocalDateTime createDate;

    public static PhotoResponseDto from(PhotoEntity photo) {
        return PhotoResponseDto.builder()
                .photoId(photo.getPhotoId())
                // .travelId(photo.getTravel().getTravelId())  // TravelEntity 연결 후 활성화
                .userId(photo.getUser().getUserId())
                .imageFile(photo.getImageFile())
                .photoDate(photo.getPhotoDate())
                .latitude(photo.getLatitude())
                .longitude(photo.getLongitude())
                .commTitle(photo.getCommTitle())
                .commContent(photo.getCommContent())
                .createDate(photo.getCreateDate())
                .build();
    }
}