package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.ReviewRequestDto;
import com.example.PartTrip.dto.community.ReviewResponseDto;
import com.example.PartTrip.service.community.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class ReviewController {

    private final ReviewService reviewService;

    // 여행지 별점 리뷰 작성
    @PostMapping("/reviews")
    public ReviewResponseDto createReview(
            Authentication authentication,
            @RequestBody ReviewRequestDto dto
    ) {
        return reviewService.createReview(authentication.getName(), dto);
    }

    // 리뷰 목록 조회 (countryInfoId 없으면 전체, 있으면 해당 여행지만)
    @GetMapping("/reviews")
    public List<ReviewResponseDto> getReviews(
            @RequestParam(required = false) Long countryInfoId
    ) {
        if (countryInfoId != null) {
            return reviewService.getReviews(countryInfoId);
        }
        return reviewService.getAllReviews();
    }

    // 리뷰 단건 조회
    @GetMapping("/reviews/{reviewId}")
    public ReviewResponseDto getReview(@PathVariable Long reviewId) {
        return reviewService.getReview(reviewId);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ReviewResponseDto updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto dto
    ) {
        return reviewService.updateReview(authentication.getName(), reviewId, dto);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public String deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(authentication.getName(), reviewId);
        return "리뷰가 삭제되었습니다.";
    }
}
