package com.example.PartTrip.community.controller;

import com.example.PartTrip.community.dto.CommentRequestDto;
import com.example.PartTrip.community.dto.CommentResponseDto;
import com.example.PartTrip.community.dto.PageResponseDto;
import com.example.PartTrip.community.dto.ShareTripRequestDto;
import com.example.PartTrip.community.dto.TripResponseDto;
import com.example.PartTrip.community.service.CommentService;
import com.example.PartTrip.community.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/shared-trips")
public class SharedTripController {

    private static final String TARGET_TYPE = "TRIP";

    private final TripService tripService;
    private final CommentService commentService;

    // 내 일정을 커뮤니티에 공개 공유
    @PostMapping
    public TripResponseDto shareTrip(Authentication authentication, @RequestBody ShareTripRequestDto dto) {
        return tripService.shareTrip(authentication.getName(), dto.getTripId());
    }

    // 공유된 일정 목록 (커뮤니티 피드, 페이지네이션)
    @GetMapping
    public PageResponseDto<TripResponseDto> listSharedTrips(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return tripService.listSharedTrips(authentication.getName(), page, size);
    }

    // 공유된 일정 상세
    @GetMapping("/{tripId}")
    public TripResponseDto getSharedTripDetail(Authentication authentication, @PathVariable Long tripId) {
        return tripService.getSharedTripDetail(tripId, authentication.getName());
    }

    // 다른 사람 일정 가져오기(복사)
    @PostMapping("/{tripId}/import")
    public TripResponseDto importTrip(Authentication authentication, @PathVariable Long tripId) {
        return tripService.importTrip(authentication.getName(), tripId);
    }

    // 일정 댓글(또는 대댓글) 작성
    @PostMapping("/{tripId}/comments")
    public CommentResponseDto createComment(
            Authentication authentication,
            @PathVariable Long tripId,
            @RequestBody CommentRequestDto dto
    ) {
        return commentService.createComment(authentication.getName(), TARGET_TYPE, tripId, dto);
    }

    // 일정 댓글 목록 조회
    @GetMapping("/{tripId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long tripId) {
        return commentService.getComments(TARGET_TYPE, tripId);
    }
}
