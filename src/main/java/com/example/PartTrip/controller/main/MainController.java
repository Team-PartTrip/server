package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final CountryInfoRepository countryInfoRepository;

    @GetMapping("/api/main")
    public String mainPage() {
        return "메인 페이지 접근 성공";
    }

    // 여행지(국가/도시) 목록 조회 - 여행지 선택 화면 등에서 사용
    @GetMapping("/api/main/countries")
    public List<CountryInfoResponseDto> getCountries() {
        List<CountryInfoEntity> countries = countryInfoRepository.findAll();

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
}
