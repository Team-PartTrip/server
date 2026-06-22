package com.example.PartTrip.dto.profile;

import com.example.PartTrip.entity.profile.UserProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private String userId;
    private String nickName;
    private String imgUrl;
    private String characterId;


    public static ProfileResponseDto from(UserProfileEntity user) {
        return new ProfileResponseDto(
                user.getUserId(),
                user.getNickName(),
                user.getImgUrl(),
                user.getCharacterId()
        );
    }
}
