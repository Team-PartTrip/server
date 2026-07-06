package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.dto.main.FestivalResponseDto;
import com.example.PartTrip.dto.main.PopulationInfoResponseDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.service.main.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final CountryInfoRepository countryInfoRepository;
    private final MainService mainService;

    @GetMapping
    public String mainPage() {
        return "메인 페이지 접근 성공";
    }

    // 여행지(국가/도시) 목록 조회 - 여행지 선택 화면 등에서 사용
    @GetMapping("/countries")
    public List<CountryInfoResponseDto> getCountries() {
        List<CountryInfoEntity> countries = countryInfoRepository.findAll(Sort.by("countryName"));

        return countries.stream()
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(),
                        c.getCountryName(),
                        c.getCityName(),
                        c.getImageUrl(),
                        c.getSummary()
                ))
                .collect(Collectors.toList());
    }

    // 국가 정보 조회
    @GetMapping("/country-info")
    public CountryInfoResponseDto getCountryInfo(
            @RequestParam String countryName
    ) {
        return mainService.getCountryInfo(countryName);
    }

    // 인구 구성 조회
    @GetMapping("/population-info")
    public List<PopulationInfoResponseDto> getPopulationInfo(
            @RequestParam String countryName
    ) {
        return mainService.getPopulationInfo(countryName);
    }

    // 축제 조회
    @GetMapping("/festivals")
    public List<FestivalResponseDto> getFestivals(
            @RequestParam String countryName
    ) {
        return mainService.getFestivals(countryName);
    }
}