package com.example.PartTrip.location;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class LocationDtos {

    private LocationDtos() {
    }

    public record UpdateRequest(
            @NotNull(message = "위도가 비어 있습니다.")
            @DecimalMin(value = "-90", message = "위도가 올바르지 않습니다.")
            @DecimalMax(value = "90", message = "위도가 올바르지 않습니다.")
            Double latitude,
            @NotNull(message = "경도가 비어 있습니다.")
            @DecimalMin(value = "-180", message = "경도가 올바르지 않습니다.")
            @DecimalMax(value = "180", message = "경도가 올바르지 않습니다.")
            Double longitude
    ) {}

    public record LocationResponse(Double latitude, Double longitude, LocalDateTime recordedAt) {}
}
