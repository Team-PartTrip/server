package com.example.PartTrip.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequestDto {
    private String travelId;
    private MultipartFile imageFile;
    private String latitude;
    private String longitude;
}
