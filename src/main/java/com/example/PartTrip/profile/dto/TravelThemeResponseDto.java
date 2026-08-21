package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.entity.TravelThemeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelThemeResponseDto {

    private Long themeId;
    private String themeCode;
    private String themeName;
    private String description;
    private String imageUrl;

    public static TravelThemeResponseDto from(TravelThemeEntity theme) {
        return new TravelThemeResponseDto(
                theme.getThemeId(),
                theme.getThemeCode(),
                theme.getThemeName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }
}
