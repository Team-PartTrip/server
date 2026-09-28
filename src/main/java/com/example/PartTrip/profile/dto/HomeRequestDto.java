package com.example.PartTrip.profile.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeRequestDto {

    @NotBlank(message = "집 이름을 입력해주세요.")
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String address;

    @NotNull(message = "위치를 골라주세요.")
    @DecimalMin("-90") @DecimalMax("90")
    private Double latitude;

    @NotNull(message = "위치를 골라주세요.")
    @DecimalMin("-180") @DecimalMax("180")
    private Double longitude;
}
