package com.example.PartTrip.service.photo;

import com.example.PartTrip.dto.photo.GuideCameraImageUploadResponseDto;
import com.example.PartTrip.dto.photo.GuideCameraRecordSaveRequestDto;
import com.example.PartTrip.dto.photo.NearbyPlaceRecommendationResponseDto;
import com.example.PartTrip.dto.photo.PhotoAnalysisResponseDto;
import com.example.PartTrip.dto.photo.PhotoUploadRequestDto;
import com.example.PartTrip.entity.photo.PhotoAnalysisEntity;
import com.example.PartTrip.entity.photo.PhotoEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.photo.PhotoAnalysisRepository;
import com.example.PartTrip.repository.photo.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        PhotoAnalysisEntity analysis = photoAnalysisRepository.findByPhotoPhotoIdAndPhotoUserUserId(imageId, userId)
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

    private BigDecimal toBigDecimal(String value, String errorMessage) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
