package com.example.PartTrip.profile.service;

import com.example.PartTrip.global.storage.ImageStorageService;
import com.example.PartTrip.profile.dto.ProfileResponseDto;
import com.example.PartTrip.profile.dto.ProfileUpdateRequestDto;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.profile.dto.ProfileStatsResponseDto;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import com.example.PartTrip.tripcard.repository.TripCardPhotoRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
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

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String userId) {
        UserEntity user = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return ProfileResponseDto.from(user);
    }

    // Func-007-01 프로필 통계.
    // 여행 카드 쓰기 API 가 아직 없어서 지금은 대부분 0 이 나온다.
    // 값이 없다고 감추지 않고 0 을 그대로 내려준다 — 화면이 "0" 을 그리면 된다.
    @Transactional(readOnly = true)
    public ProfileStatsResponseDto getStats(String userId) {
        return new ProfileStatsResponseDto(
                tripCardRepository.countByUserId(userId),
                tripCardRepository.countDistinctRegionsByUserId(userId),
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

        String imgUrl = requestDto.getImgUrl();
        if (!isAllowedImage(imgUrl, user.getImgUrl(), userId)) {
            throw new IllegalArgumentException("직접 올린 사진만 프로필로 쓸 수 있어요.");
        }

        user.setNickName(requestDto.getNickName());
        user.setImgUrl(imgUrl);

        return ProfileResponseDto.from(user);
    }

    // 프로필 사진 업로드 (Func-007-01)
    // 저장만 하고 URL 을 돌려준다. 실제 반영은 PUT /api/profile 에서 imgUrl 로 넘긴다.
    // 사용자별 폴더에 둬서, 프로필을 바꿀 때 내가 올린 파일인지 경로로 확인한다
    public String uploadProfileImage(String userId, MultipartFile imageFile) {
        return imageStorageService.store(imageFile, profileDirectory(userId));
    }

    /** 비우기, 지금 값 그대로(예전 경로 포함), 내 폴더의 파일만 받는다 */
    static boolean isAllowedImage(String imgUrl, String current, String userId) {
        if (imgUrl == null || imgUrl.isBlank() || imgUrl.equals(current)) {
            return true;
        }
        return imgUrl.startsWith("/uploads/" + profileDirectory(userId) + "/") && !imgUrl.contains("..");
    }

    private static String profileDirectory(String userId) {
        return "profile/" + userId;
    }
}
