package com.example.PartTrip.service.profile;

import com.example.PartTrip.dto.profile.ProfileResponseDto;
import com.example.PartTrip.entity.profile.UserProfileEntity;
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
        UserProfileEntity user = findUserProfile(userId);
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

    private UserProfileEntity findUserProfile(String userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
