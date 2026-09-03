package com.example.PartTrip.profile.service;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ImageStorageService imageStorageService;
    private final TripCardRepository tripCardRepository;
    private final TripCardPhotoRepository tripCardPhotoRepository;
    private final VisitedCountryRepository visitedCountryRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String userId) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ProfileResponseDto.from(user);
    }

    // Func-007-01 프로필 통계.
    // 여행 카드·세계지도 쓰기 API 가 아직 없어서 지금은 대부분 0 이 나온다.
    // 값이 없다고 감추지 않고 0 을 그대로 내려준다 — 화면이 "0" 을 그리면 된다.
    @Transactional(readOnly = true)
    public ProfileStatsResponseDto getStats(String userId) {
        return new ProfileStatsResponseDto(
                tripCardRepository.countByUserId(userId),
                visitedCountryRepository.countByUserId(userId),
                tripCardPhotoRepository.countByUserId(userId)
        );
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

    // 프로필 사진 업로드 (Func-007-01)
    // 저장만 하고 URL 을 돌려준다. 실제 반영은 PUT /api/profile 에서 imgUrl 로 넘긴다.
    public String uploadProfileImage(MultipartFile imageFile) {
        return imageStorageService.store(imageFile, "profile");
    }
}
