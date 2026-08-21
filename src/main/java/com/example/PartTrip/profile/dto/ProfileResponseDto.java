package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.entity.TravelThemeEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private String userId;
    private String nickName;
    private String imgUrl;

    // 여행 타입 (Func-007-01) — 아직 선택하지 않았으면 전부 null
    private Long themeId;
    private String themeName;
    private String themeDescription;

    public static ProfileResponseDto from(UserEntity user) {
        TravelThemeEntity theme = user.getTravelTheme();

        return new ProfileResponseDto(
                user.getUserId(),
                user.getNickName(),
                user.getImgUrl(),
                theme == null ? null : theme.getThemeId(),
                theme == null ? null : theme.getThemeName(),
                theme == null ? null : theme.getDescription()
        );
    }
}
