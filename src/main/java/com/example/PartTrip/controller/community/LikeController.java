package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.LikeRequestDto;
import com.example.PartTrip.dto.community.LikeResponseDto;
import com.example.PartTrip.service.community.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/likes")
public class LikeController {

    private final LikeService likeService;

    // 좋아요 토글 (게시글/리뷰/일정 공통)
    @PostMapping
    public LikeResponseDto toggleLike(Authentication authentication, @RequestBody LikeRequestDto dto) {
        return likeService.toggleLike(authentication.getName(), dto.getTargetType(), dto.getTargetId());
    }
}
