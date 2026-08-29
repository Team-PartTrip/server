package com.example.PartTrip.profile.service;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.profile.dto.TravelThemeResponseDto;
import com.example.PartTrip.profile.entity.TravelThemeEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.profile.repository.TravelThemeRepository;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final TravelThemeRepository travelThemeRepository;
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

    // 프로필 사진 업로드 (Func-007-01)
    // 저장만 하고 URL 을 돌려준다. 실제 반영은 PUT /api/profile 에서 imgUrl 로 넘긴다.
    public String uploadProfileImage(MultipartFile imageFile) {
        return imageStorageService.store(imageFile, "profile");
    }
}
