package com.example.PartTrip.photo.service;


import com.example.PartTrip.photo.dto.GuideCameraImageUploadResponseDto;
import com.example.PartTrip.photo.dto.GuideCameraRecordSaveRequestDto;
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

        // 본인이 올린 사진의 분석 결과만 조회할 수 있음
        PhotoAnalysisEntity analysis = photoAnalysisRepository
                .findByPhotoPhotoIdAndPhotoUserUserId(imageId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지의 분석 결과가 없습니다."));
        return PhotoAnalysisResponseDto.from(analysis);
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
