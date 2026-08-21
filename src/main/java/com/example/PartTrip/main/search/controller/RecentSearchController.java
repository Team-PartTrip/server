package com.example.PartTrip.main.search.controller;

import com.example.PartTrip.main.search.dto.RecentSearchRequestDto;
import com.example.PartTrip.main.search.dto.RecentSearchResponseDto;
import com.example.PartTrip.main.search.service.RecentSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/search")
public class RecentSearchController {

    private final RecentSearchService recentSearchService;

    // 최근 검색 조회
    // userId 는 요청에서 받지 않고 인증 토큰에서 꺼낸다
    @GetMapping("/recent")
    public List<RecentSearchResponseDto> getRecentSearch(Authentication authentication) {

        String userId = (String) authentication.getPrincipal();

        return recentSearchService.getRecentSearch(userId);
    }

    // X 버튼 삭제
    @DeleteMapping("/recent/{recentSearchId}")
    public void deleteRecentSearch(
            Authentication authentication,
            @PathVariable Long recentSearchId) {

        String userId = (String) authentication.getPrincipal();

        recentSearchService.deleteRecentSearch(userId, recentSearchId);
    }

    // 최근 검색 저장
    @PostMapping("/recent")
    public void saveRecentSearch(
            Authentication authentication,
            @Valid @RequestBody RecentSearchRequestDto request) {

        String userId = (String) authentication.getPrincipal();

        recentSearchService.saveRecentSearch(userId, request.getCountryInfoId());
    }

}
