package com.example.PartTrip.service.profile;

import com.example.PartTrip.dto.profile.ProfileResponseDto;
import com.example.PartTrip.entity.profile.UserProfileEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.profile.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String userId) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ProfileResponseDto.from(user);
    }

//
//    @Transactional
//    public ProfileResponseDto updateProfile(String userId, ProfileUpdateRequestDto requestDto) {
//        UserProfileEntity user = findUserProfile(userId);
//
//        if (userProfileRepository.existsByNickNameAndUserIdNot(requestDto.getNickName(), userId)) {
//            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
//        }
//
//        user.updateProfile(requestDto.getNickName(), requestDto.getTravelType());
//        return ProfileResponseDto.from(user);
//    }

    private UserEntity findUserProfile(String userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
