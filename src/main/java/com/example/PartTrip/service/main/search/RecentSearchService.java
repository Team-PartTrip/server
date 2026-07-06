package com.example.PartTrip.service.main.search;

import com.example.PartTrip.dto.main.search.RecentSearchResponseDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.entity.main.search.RecentSearchEntity;
import com.example.PartTrip.repository.main.search.RecentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentSearchService {

    private final RecentSearchRepository recentSearchRepository;
    private final CountryInfoRepository countryInfoRepository;

    // 최근 검색 조회
    public List<RecentSearchResponseDto> getRecentSearch(Long userId){

        return recentSearchRepository.findByUserIdOrderBySearchedAtDesc(userId)
                .stream()
                .map(search -> {

                    CountryInfoEntity country =
                            countryInfoRepository.findById(search.getCountryInfoId())
                                    .orElseThrow();

                    return new RecentSearchResponseDto(
                            search.getRecentSearchId(),
                            country.getCountryName(),
                            country.getCityName(),
                            country.getCurrency(),
                            country.getImageUrl()
                    );

                })
                .toList();

    }


}