package com.example.PartTrip.controller.main.search;

import com.example.PartTrip.dto.main.search.RecentSearchRequestDto;
import com.example.PartTrip.dto.main.search.RecentSearchResponseDto;
import com.example.PartTrip.service.main.search.RecentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/search")
public class RecentSearchController {

    private final RecentSearchService recentSearchService;

    // 최근 검색 조회
    @GetMapping("/recent")
    public List<RecentSearchResponseDto> getRecentSearch(
            @RequestParam Long userId){

        return recentSearchService.getRecentSearch(userId);

    }

    // X 버튼 삭제
    @DeleteMapping("/recent/{recentSearchId}")
    public void deleteRecentSearch(
            @PathVariable Long recentSearchId){

        recentSearchService.deleteRecentSearch(recentSearchId);

    }


    @PostMapping("/recent")
    public void saveRecentSearch(
            @RequestBody RecentSearchRequestDto request
    ){

        recentSearchService.saveRecentSearch(
                request.getUserId(),
                request.getCountryInfoId()
        );

    }

}