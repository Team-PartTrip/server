package com.example.PartTrip.dto.profile;

import com.example.PartTrip.entity.profile.UserProfileEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {

    private String userId;
    private String nickName;
    private String imgUrl;
//    private String characterId;


    public static ProfileResponseDto from(UserEntity user) {
        return new ProfileResponseDto(
                user.getUserId(),
                user.getNickName(),
                user.getImgUrl()
                // 이부분에 캐릭터 아이디 조회 넣어야하는데,
//                user.get캐릭터 아이디()
        );
    }
}
