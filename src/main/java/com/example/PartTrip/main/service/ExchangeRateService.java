package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.ExchangeRateResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final CountryInfoRepository countryInfoRepository;

    // Frankfurter: 무료, API 키 불필요, 유럽중앙은행(ECB) 환율 데이터 (일 단위 갱신)
    // 구 도메인(api.frankfurter.app)은 신 도메인으로 301 리다이렉트되는데
    // 기본 설정의 RestClient는 이를 따라가지 않아 신 도메인을 직접 사용
    private final RestClient restClient = RestClient.create("https://api.frankfurter.dev/v1");

    // 국가명으로 통화 코드를 찾아 원화 환율 조회
    public ExchangeRateResponseDto getExchangeRate(String countryName) {

        CountryInfoEntity country = countryInfoRepository.findByCountryName(countryName)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));

        String currencyCode = country.getCurrencyCode();
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new IllegalArgumentException("해당 국가의 통화 코드 정보가 없습니다.");
        }

        // 원화 자체는 환전 대상이 아니므로 1:1로 응답
        if (currencyCode.equals("KRW")) {
            return new ExchangeRateResponseDto("KRW", 1.0, null);
        }

        Map<String, Object> response;
        try {
            response = restClient.get()
                    .uri("/latest?from={from}&to=KRW", currencyCode)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("환율 정보를 가져오지 못했습니다.");
        }

        if (response == null || !response.containsKey("rates")) {
            throw new IllegalArgumentException("환율 정보를 가져오지 못했습니다.");
        }

        Map<String, Object> rates = (Map<String, Object>) response.get("rates");
        Object krwRate = rates.get("KRW");

        if (krwRate == null) {
            throw new IllegalArgumentException("지원하지 않는 통화입니다: " + currencyCode);
        }

        return new ExchangeRateResponseDto(
                currencyCode,
                ((Number) krwRate).doubleValue(),
                (String) response.get("date")
        );
    }
}
