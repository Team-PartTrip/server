package com.example.PartTrip.photo.service;


import com.example.PartTrip.photo.dto.GuideCameraImageUploadResponseDto;
import com.example.PartTrip.photo.dto.GuideCameraRecordSaveRequestDto;
import com.example.PartTrip.photo.dto.NearbyPlaceRecommendationResponseDto;
import com.example.PartTrip.photo.dto.PhotoAnalysisResponseDto;
import com.example.PartTrip.photo.dto.PhotoUploadRequestDto;
import com.example.PartTrip.photo.entity.PhotoAnalysisEntity;
import com.example.PartTrip.photo.entity.PhotoEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.photo.repository.PhotoAnalysisRepository;
import com.example.PartTrip.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.PartTrip.photo.dto.GuideCameraMissionCreateRequestDto;
import com.example.PartTrip.photo.dto.GuideCameraMissionResponseDto;
import com.example.PartTrip.photo.entity.GuideCameraMissionEntity;
import com.example.PartTrip.photo.enums.GuideCameraMissionType;
import com.example.PartTrip.photo.repository.GuideCameraMissionRepository;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideCameraService {

    private final PhotoRepository photoRepository;
    private final PhotoAnalysisRepository photoAnalysisRepository;
    private final PhotoStorageService photoStorageService;
    private final PhotoAnalyzer photoAnalyzer;
    private final NearbyPlaceRecommendationService recommendationService;
    private final CurrentUserProvider currentUserProvider;
    private final GuideCameraMissionRepository guideCameraMissionRepository;

    @Transactional
    public GuideCameraImageUploadResponseDto uploadImage(PhotoUploadRequestDto request) {
        UserEntity user = currentUserProvider.getCurrentUser();
        String imageUrl = photoStorageService.store(request.getImageFile());

        PhotoEntity photo = new PhotoEntity();
        photo.setUser(user);
        photo.setImgUrl(imageUrl);
        photo.setLatitude(toBigDecimal(request.getLatitude(), "위도 값이 올바르지 않습니다."));
        photo.setLongitude(toBigDecimal(request.getLongitude(), "경도 값이 올바르지 않습니다."));

        PhotoEntity savedPhoto = photoRepository.save(photo);
        PhotoAnalysisEntity savedAnalysis = photoAnalysisRepository.save(photoAnalyzer.analyze(savedPhoto, request.getImageFile()));

        return GuideCameraImageUploadResponseDto.of(savedPhoto.getPhotoId(), savedAnalysis.getAnalysisId());
    }

    @Transactional(readOnly = true)
    public PhotoAnalysisResponseDto getAnalysisResult(Long imageId) {
        String userId = currentUserProvider.getCurrentUserId();
        PhotoAnalysisEntity analysis = photoAnalysisRepository.findByPhotoPhotoId(imageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지의 분석 결과가 없습니다."));
        return PhotoAnalysisResponseDto.from(analysis);
    }

    @Transactional(readOnly = true)
    public List<NearbyPlaceRecommendationResponseDto> getRecommendations(BigDecimal latitude, BigDecimal longitude) {
        return recommendationService.recommend(latitude, longitude);
    }

    @Transactional
    public PhotoAnalysisResponseDto saveRecord(GuideCameraRecordSaveRequestDto request) {
        String userId = currentUserProvider.getCurrentUserId();
        PhotoEntity photo = photoRepository.findByPhotoIdAndUserUserId(request.getPhotoId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사진입니다."));

        photo.setCommTitle(request.getCommTitle());
        photo.setCommContent(request.getCommContent());
        photo.setPhotoDate(request.getPhotoDate());

        PhotoAnalysisEntity analysis = photoAnalysisRepository.findByPhotoPhotoIdAndPhotoUserUserId(photo.getPhotoId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지의 분석 결과가 없습니다."));
        return PhotoAnalysisResponseDto.from(analysis);
    }

    @Transactional
    public GuideCameraMissionResponseDto createMission(GuideCameraMissionCreateRequestDto request) {
        UserEntity user = currentUserProvider.getCurrentUser();

        GuideCameraMissionType missionType = classifyMissionType(request.getPlaceType());
        MissionTemplate template = pickTemplate(missionType, request.getTargetPlaceName());

        GuideCameraMissionEntity mission = GuideCameraMissionEntity.builder()
                .user(user)
                .missionType(missionType)
                .title(template.title())
                .description(template.description().formatted(request.getTargetPlaceName()))
                .targetPlaceName(request.getTargetPlaceName())
                .placeType(request.getPlaceType())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .completed(false)
                .build();

        return GuideCameraMissionResponseDto.from(guideCameraMissionRepository.save(mission));
    }

    private GuideCameraMissionType classifyMissionType(String placeType) {
        if (placeType == null) {
            return GuideCameraMissionType.DEFAULT;
        }

        String type = placeType.toLowerCase(Locale.ROOT);

        if (type.contains("restaurant") || type.contains("food") || type.contains("meal")) {
            return GuideCameraMissionType.FOOD;
        }

        if (type.contains("cafe") || type.contains("coffee")) {
            return GuideCameraMissionType.CAFE;
        }

        if (type.contains("amusement")
                || type.contains("tourist")
                || type.contains("aquarium")
                || type.contains("zoo")
                || type.contains("theme")) {
            return GuideCameraMissionType.EXPERIENCE;
        }

        if (type.contains("museum")
                || type.contains("temple")
                || type.contains("church")
                || type.contains("landmark")
                || type.contains("historical")
                || type.contains("cultural")) {
            return GuideCameraMissionType.CULTURE;
        }

        if (type.contains("park")
                || type.contains("garden")
                || type.contains("beach")
                || type.contains("mountain")) {
            return GuideCameraMissionType.NATURE;
        }

        if (type.contains("shopping")
                || type.contains("store")
                || type.contains("market")
                || type.contains("mall")) {
            return GuideCameraMissionType.SHOPPING;
        }

        return GuideCameraMissionType.DEFAULT;
    }

    private MissionTemplate pickTemplate(GuideCameraMissionType type, String placeName) {
        List<MissionTemplate> templates = switch (type) {
            case FOOD -> List.of(
                    new MissionTemplate("오늘의 한 입", "%s에서 먹은 음식을 사진으로 남겨보세요."),
                    new MissionTemplate("맛있는 순간 기록", "%s에서 가장 맛있어 보이는 순간을 담아보세요."),
                    new MissionTemplate("먹방 인증 완료", "%s에서 음식 사진을 찍고 여행 기록을 남겨보세요.")
            );
            case CAFE -> List.of(
                    new MissionTemplate("감성 한 잔", "%s에서 음료와 함께 감성 사진을 남겨보세요."),
                    new MissionTemplate("카페 체크인", "%s에서 오늘의 한 컷을 촬영해보세요."),
                    new MissionTemplate("여유로운 순간", "%s에서 가장 편안한 순간을 사진으로 남겨보세요.")
            );
            case EXPERIENCE -> List.of(
                    new MissionTemplate("짜릿한 도전", "%s에서 가장 기억에 남는 체험을 해보세요."),
                    new MissionTemplate("체험 인증 완료", "%s에서 직접 경험한 순간을 기록해보세요."),
                    new MissionTemplate("오늘의 모험", "%s에서 특별한 체험을 사진으로 남겨보세요.")
            );
            case CULTURE -> List.of(
                    new MissionTemplate("이야기 열기", "%s를 촬영하고 AI 해설을 확인해보세요."),
                    new MissionTemplate("역사 한 조각", "%s의 분위기가 잘 담긴 사진을 남겨보세요."),
                    new MissionTemplate("문화유산 탐험", "%s에서 가장 인상 깊은 장면을 촬영해보세요.")
            );
            case NATURE -> List.of(
                    new MissionTemplate("풍경 수집가", "%s의 가장 아름다운 풍경을 담아보세요."),
                    new MissionTemplate("자연 한 컷", "%s에서 마음에 드는 자연 풍경을 촬영해보세요."),
                    new MissionTemplate("힐링 기록", "%s에서 여유로운 순간을 사진으로 남겨보세요.")
            );
            case SHOPPING -> List.of(
                    new MissionTemplate("쇼핑 체크인", "%s에서 마음에 드는 공간을 사진으로 남겨보세요."),
                    new MissionTemplate("오늘의 발견", "%s에서 눈에 띄는 순간을 기록해보세요."),
                    new MissionTemplate("구경 완료", "%s에서 여행 중 발견한 장면을 촬영해보세요.")
            );
            default -> List.of(
                    new MissionTemplate("오늘의 베스트 샷", "%s에서 가장 마음에 드는 순간을 촬영해보세요."),
                    new MissionTemplate("여행 인증하기", "%s에 방문한 순간을 사진으로 남겨보세요."),
                    new MissionTemplate("추억 남기기", "%s에서 기억에 남을 한 장면을 담아보세요.")
            );
        };

        int index = Math.abs(Objects.hash(type, placeName)) % templates.size();
        return templates.get(index);
    }

    private record MissionTemplate(String title, String description) {
    }

    private BigDecimal toBigDecimal(String value, String errorMessage) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
