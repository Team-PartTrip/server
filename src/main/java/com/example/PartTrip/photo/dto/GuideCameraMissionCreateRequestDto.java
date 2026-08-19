package com.example.PartTrip.photo.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class GuideCameraMissionCreateRequestDto {

    @NotBlank(message = "장소명은 필수입니다.")
    @Size(max = 100)
    private String targetPlaceName;

    // Google Places의 primaryType 또는 primaryTypeDisplayName 값
    private String placeType;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitude;
}