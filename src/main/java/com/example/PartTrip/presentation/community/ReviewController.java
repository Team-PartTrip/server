package com.example.PartTrip.presentation.community;

import com.example.PartTrip.application.community.data.CommentRequestDto;
import com.example.PartTrip.application.community.data.CommentResponseDto;
import com.example.PartTrip.application.community.data.PageResponseDto;
import com.example.PartTrip.application.community.data.ReviewRequestDto;
import com.example.PartTrip.application.community.data.ReviewResponseDto;
import com.example.PartTrip.application.community.CommentService;
import com.example.PartTrip.application.community.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class ReviewController {

    private static final String TARGET_TYPE = "REVIEW";

    private final ReviewService reviewService;
    private final CommentService commentService;

    // 여행지 별점 리뷰 작성
    @PostMapping("/reviews")
    public ReviewResponseDto createReview(
            Authentication authentication,
            @RequestBody ReviewRequestDto dto
    ) {
        return reviewService.createReview(authentication.getName(), dto);
    }

    // 리뷰 목록 조회 (countryInfoId 없으면 전체, 있으면 해당 여행지만, 페이지네이션)
    @GetMapping("/reviews")
    public PageResponseDto<ReviewResponseDto> getReviews(
            Authentication authentication,
            @RequestParam(required = false) Long countryInfoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        if (countryInfoId != null) {
            return reviewService.getReviews(countryInfoId, authentication.getName(), page, size);
        }
        return reviewService.getAllReviews(authentication.getName(), page, size);
    }

    // 내가 쓴 리뷰 목록
    @GetMapping("/reviews/mine")
    public PageResponseDto<ReviewResponseDto> getMyReviews(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reviewService.getMyReviews(authentication.getName(), page, size);
    }

    // 리뷰 단건 조회
    @GetMapping("/reviews/{reviewId}")
    public ReviewResponseDto getReview(Authentication authentication, @PathVariable Long reviewId) {
        return reviewService.getReview(reviewId, authentication.getName());
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

    // 리뷰 댓글(또는 대댓글) 작성
    @PostMapping("/reviews/{reviewId}/comments")
    public CommentResponseDto createComment(
            Authentication authentication,
            @PathVariable Long reviewId,
            @RequestBody CommentRequestDto dto
    ) {
        return commentService.createComment(authentication.getName(), TARGET_TYPE, reviewId, dto);
    }

    // 리뷰 댓글 목록 조회
    @GetMapping("/reviews/{reviewId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long reviewId) {
        return commentService.getComments(TARGET_TYPE, reviewId);
    }
}
