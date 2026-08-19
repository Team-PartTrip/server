package com.example.PartTrip.community.service;

import com.example.PartTrip.community.dto.PageResponseDto;
import com.example.PartTrip.community.dto.ReviewRequestDto;
import com.example.PartTrip.community.dto.ReviewResponseDto;
import com.example.PartTrip.community.entity.PostImageEntity;
import com.example.PartTrip.community.entity.ReviewEntity;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.community.repository.CommentRepository;
import com.example.PartTrip.community.repository.LikeRepository;
import com.example.PartTrip.community.repository.PostImageRepository;
import com.example.PartTrip.community.repository.ReviewRepository;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private static final String TARGET_TYPE = "REVIEW";

    private final ReviewRepository reviewRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;

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

        saveImages(saved.getReviewId(), dto.getImages());

        return toDto(saved, userId);
    }

    // 리뷰 단건 조회
    public ReviewResponseDto getReview(Long reviewId, String currentUserId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        return toDto(review, currentUserId);
    }

    // 특정 여행지의 리뷰 목록 (최신순, 페이지네이션)
    public PageResponseDto<ReviewResponseDto> getReviews(Long countryInfoId, String currentUserId, int page, int size) {
        Page<ReviewEntity> result =
                reviewRepository.findByCountryInfoIdOrderByCreateDateDesc(countryInfoId, PageRequest.of(page, size));
        return toPageDto(result, currentUserId);
    }

    // 전체 리뷰 목록 (커뮤니티 피드, 최신순, 페이지네이션)
    public PageResponseDto<ReviewResponseDto> getAllReviews(String currentUserId, int page, int size) {
        Page<ReviewEntity> result = reviewRepository.findAllByOrderByCreateDateDesc(PageRequest.of(page, size));
        return toPageDto(result, currentUserId);
    }

    // 내가 쓴 리뷰 목록
    public PageResponseDto<ReviewResponseDto> getMyReviews(String userId, int page, int size) {
        Page<ReviewEntity> result = reviewRepository.findByUserIdOrderByCreateDateDesc(userId, PageRequest.of(page, size));
        return toPageDto(result, userId);
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

        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, reviewId);
        saveImages(reviewId, dto.getImages());

        return toDto(saved, userId);
    }

    // 리뷰 삭제 (본인 리뷰만 가능)
    public void deleteReview(String userId, Long reviewId) {

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, reviewId);
        likeRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, reviewId);
        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, reviewId);
        reviewRepository.delete(review);
    }

    private void saveImages(Long reviewId, List<String> images) {
        if (images == null) return;
        int order = 0;
        for (String url : images) {
            if (url == null || url.isBlank()) continue;
            PostImageEntity image = new PostImageEntity();
            image.setTargetType(TARGET_TYPE);
            image.setTargetId(reviewId);
            image.setImageUrl(url);
            image.setSortOrder(order++);
            postImageRepository.save(image);
        }
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

    private PageResponseDto<ReviewResponseDto> toPageDto(Page<ReviewEntity> page, String currentUserId) {
        List<ReviewResponseDto> content = page.getContent().stream()
                .map(r -> toDto(r, currentUserId))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }

    // Entity -> Dto 변환 (국가정보 + 닉네임 + 이미지 + 좋아요 + 댓글수 포함)
    private ReviewResponseDto toDto(ReviewEntity review, String currentUserId) {

        CountryInfoEntity country = countryInfoRepository.findById(review.getCountryInfoId())
                .orElse(null);

        String nickName = userRepository.findByUserId(review.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        long likeCount = likeRepository.countByTargetTypeAndTargetId(TARGET_TYPE, review.getReviewId());
        boolean liked = currentUserId != null && likeRepository
                .findByTargetTypeAndTargetIdAndUserId(TARGET_TYPE, review.getReviewId(), currentUserId)
                .isPresent();
        long commentCount = commentRepository.countByTargetTypeAndTargetId(TARGET_TYPE, review.getReviewId());

        List<String> images = postImageRepository
                .findByTargetTypeAndTargetIdOrderBySortOrderAsc(TARGET_TYPE, review.getReviewId())
                .stream()
                .map(PostImageEntity::getImageUrl)
                .collect(Collectors.toList());

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
                images,
                likeCount,
                liked,
                commentCount,
                review.getCreateDate()
        );
    }
}
