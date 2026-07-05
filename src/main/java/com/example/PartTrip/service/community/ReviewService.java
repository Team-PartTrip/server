package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.ReviewRequestDto;
import com.example.PartTrip.dto.community.ReviewResponseDto;
import com.example.PartTrip.entity.community.ReviewEntity;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.community.ReviewRepository;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final UserRepository userRepository;

    // 여행지 별점 리뷰 작성
    public ReviewResponseDto createReview(String userId, ReviewRequestDto dto) {

        validate(dto);

        CountryInfoEntity countryInfo = countryInfoRepository.findById(dto.getCountryInfoId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다."));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        ReviewEntity review = new ReviewEntity();
        review.setCountryInfoId(countryInfo.getCountryInfoId());
        review.setUserId(userId);
        review.setTitle(dto.getTitle());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setCreateDate(LocalDateTime.now());

        ReviewEntity saved = reviewRepository.save(review);

        return toDto(saved);
    }

    // 리뷰 단건 조회
    public ReviewResponseDto getReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        return toDto(review);
    }

    // 특정 여행지의 리뷰 목록 (최신순)
    public List<ReviewResponseDto> getReviews(Long countryInfoId) {
        return reviewRepository.findByCountryInfoIdOrderByCreateDateDesc(countryInfoId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 전체 리뷰 목록 (커뮤니티 피드, 최신순)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreateDateDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 리뷰 수정 (본인 리뷰만 가능)
    public ReviewResponseDto updateReview(String userId, Long reviewId, ReviewRequestDto dto) {

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        validate(dto);

        review.setTitle(dto.getTitle());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());

        ReviewEntity saved = reviewRepository.save(review);

        return toDto(saved);
    }

    // 리뷰 삭제 (본인 리뷰만 가능)
    public void deleteReview(String userId, Long reviewId) {

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    // 작성/수정 공통 검증
    private void validate(ReviewRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("별점은 1점에서 5점 사이여야 합니다.");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }
    }

    // Entity -> Dto 변환 (국가정보 + 닉네임 포함)
    private ReviewResponseDto toDto(ReviewEntity review) {

        CountryInfoEntity country = countryInfoRepository.findById(review.getCountryInfoId())
                .orElse(null);

        String nickName = userRepository.findByUserId(review.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        return new ReviewResponseDto(
                review.getReviewId(),
                review.getCountryInfoId(),
                country != null ? country.getCountryName() : null,
                country != null ? country.getCityName() : null,
                review.getUserId(),
                nickName,
                review.getTitle(),
                review.getRating(),
                review.getContent(),
                review.getCreateDate()
        );
    }
}
