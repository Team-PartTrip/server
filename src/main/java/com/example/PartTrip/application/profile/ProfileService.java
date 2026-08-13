package com.example.PartTrip.application.profile;

import com.example.PartTrip.application.profile.data.CharacterInfoResponseDto;
import com.example.PartTrip.application.profile.data.ProfileResponseDto;
import com.example.PartTrip.application.profile.data.ProfileUpdateRequestDto;
import com.example.PartTrip.domain.profile.entity.CharacterInfoEntity;
import com.example.PartTrip.domain.signup.entity.UserEntity;
import com.example.PartTrip.domain.profile.repository.CharacterInfoRepository;
import com.example.PartTrip.domain.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final CharacterInfoRepository characterInfoRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String userId) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ProfileResponseDto.from(user);
    }

    @Transactional
    public ProfileResponseDto updateProfile(String userId, ProfileUpdateRequestDto requestDto) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (userProfileRepository.existsByNickNameAndUserIdNot(requestDto.getNickName(), userId)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.setNickName(requestDto.getNickName());
        user.setImgUrl(requestDto.getImgUrl());

        return ProfileResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public CharacterInfoResponseDto getCharacterInfo(String userId) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getCharacterId() == null) {
            throw new IllegalArgumentException("캐릭터를 선택하지 않은 유저입니다.");
        }

        Long characterId;
        try {
            characterId = Long.parseLong(user.getCharacterId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("캐릭터 ID 형식이 올바르지 않습니다.");
        }

        CharacterInfoEntity characterInfo = characterInfoRepository.findByCharacterId(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터 정보를 찾을 수 없습니다."));

        return CharacterInfoResponseDto.from(characterInfo);
    }
}
