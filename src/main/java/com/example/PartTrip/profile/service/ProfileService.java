package com.example.PartTrip.profile.service;

import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelThemeResponseDto;
import com.example.PartTrip.profile.entity.TravelThemeEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.profile.repository.TravelThemeRepository;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final TravelThemeRepository travelThemeRepository;

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

        // themeId 를 보내지 않으면 기존 여행 타입을 그대로 둔다
        if (requestDto.getThemeId() != null) {
            TravelThemeEntity theme = travelThemeRepository.findById(requestDto.getThemeId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 타입입니다."));
            user.setTravelTheme(theme);
        }

        return ProfileResponseDto.from(user);
    }


    // 여행 타입 목록 조회 (프로필 수정 화면에서 선택지로 사용)
    @Transactional(readOnly = true)
    public List<TravelThemeResponseDto> getTravelThemes() {
        return travelThemeRepository.findAllByOrderByThemeIdAsc()
                .stream()
                .map(TravelThemeResponseDto::from)
                .toList();
    }
}
