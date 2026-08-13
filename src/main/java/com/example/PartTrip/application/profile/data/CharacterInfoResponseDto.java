package com.example.PartTrip.application.profile.data;

import com.example.PartTrip.domain.profile.entity.CharacterInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CharacterInfoResponseDto {

    private String characterName;
    private String characterType;
    private String characterDescription;
    private String imgUrl;

    public static CharacterInfoResponseDto from(CharacterInfoEntity entity) {
        return new CharacterInfoResponseDto(
                entity.getCharacterName(),
                entity.getCharacterType(),
                entity.getCharacterDescription(),
                entity.getImgUrl()
        );
    }
}
