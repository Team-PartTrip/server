package com.example.PartTrip.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequestDto {
    private String photoId;
    private String travelId;
    private String userId;
    private MultipartFile imageFile;
    private String latitude;
    private String longitude;
    private String commTitle;
    private String commContent;
}
