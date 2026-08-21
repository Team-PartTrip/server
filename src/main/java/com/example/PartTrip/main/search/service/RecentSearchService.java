package com.example.PartTrip.main.search.service;

import com.example.PartTrip.main.search.dto.RecentSearchResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.search.entity.RecentSearchEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.main.search.repository.RecentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {

    private final RecentSearchRepository recentSearchRepository;
    private final CountryInfoRepository countryInfoRepository;

    // 최근 검색 조회
    @Transactional(readOnly = true)
    public List<RecentSearchResponseDto> getRecentSearch(String userId) {

        return recentSearchRepository.findByUserIdOrderBySearchedAtDesc(userId)
                .stream()
                .map(search -> {

                    CountryInfoEntity country = countryInfoRepository
                            .findById(search.getCountryInfoId())
                            .orElseThrow();

                    return new RecentSearchResponseDto(
                            search.getRecentSearchId(),
                            country.getCountryName(),
                            country.getCityName(),
                            country.getImageUrl()
                    );
                })
                .toList();
    }

    // 최근 검색 저장
    @Transactional
    public void saveRecentSearch(String userId, Long countryInfoId) {

        RecentSearchEntity recentSearch = RecentSearchEntity.builder()
                .userId(userId)
                .countryInfoId(countryInfoId)
                .searchedAt(LocalDateTime.now())
                .build();

        recentSearchRepository.save(recentSearch);
    }

    // 최근 검색 삭제 (X 버튼)
    @Transactional
    public void deleteRecentSearch(Long recentSearchId) {

        recentSearchRepository.deleteById(recentSearchId);

    }

}