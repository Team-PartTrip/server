package com.example.PartTrip.photo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequestDto {
    private String travelId;
    private String imgUrl;
    private String latitude;
    private String longitude;
}
