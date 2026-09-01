package com.example.PartTrip.profile.dto;

import com.example.PartTrip.signup.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private String userId;
    private String nickName;
    private String imgUrl;

    public static ProfileResponseDto from(UserEntity user) {
        return new ProfileResponseDto(
                user.getUserId(),
                user.getNickName(),
                user.getImgUrl()
        );
    }
}
