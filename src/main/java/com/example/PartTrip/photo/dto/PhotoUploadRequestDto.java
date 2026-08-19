package com.example.PartTrip.photo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequestDto {
    private String travelId;

    @NotNull(message = "이미지 파일은 필수입니다.")
    private MultipartFile imageFile;

    @NotBlank(message = "위도는 필수입니다.")
    private String latitude;

    @NotBlank(message = "경도는 필수입니다.")
    private String longitude;
}
