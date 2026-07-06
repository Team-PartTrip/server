package com.example.PartTrip.dto.photo;

import com.example.PartTrip.entity.photo.GuideCameraMissionEntity;
import com.example.PartTrip.enums.photo.GuideCameraMissionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideCameraMissionResponseDto {

    private Long missionId;
    private GuideCameraMissionType missionType;
    private String title;
    private String description;
    private String targetPlaceName;
    private String placeType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean completed;
    private LocalDateTime createDate;

    public static GuideCameraMissionResponseDto from(GuideCameraMissionEntity mission) {
        return GuideCameraMissionResponseDto.builder()
                .missionId(mission.getMissionId())
                .missionType(mission.getMissionType())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .targetPlaceName(mission.getTargetPlaceName())
                .placeType(mission.getPlaceType())
                .latitude(mission.getLatitude())
                .longitude(mission.getLongitude())
                .completed(mission.getCompleted())
                .createDate(mission.getCreateDate())
                .build();
    }
}