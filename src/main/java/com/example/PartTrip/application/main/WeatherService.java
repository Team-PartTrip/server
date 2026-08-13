package com.example.PartTrip.application.main;

import com.example.PartTrip.application.main.data.WeatherResponseDto;
import com.example.PartTrip.domain.main.entity.CountryInfoEntity;
import com.example.PartTrip.domain.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final CountryInfoRepository countryInfoRepository;

    // OpenWeatherMap: 실제 관측소 기반 데이터라 대기 모델(Open-Meteo)보다 정확도가 높음
    @Value("${openweathermap.api-key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create("https://api.openweathermap.org");

    // 국가명으로 좌표를 찾아 현재 날씨 조회
    public WeatherResponseDto getWeather(String countryName) {

        CountryInfoEntity country = countryInfoRepository.findByCountryName(countryName)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));

        Double latitude = country.getLatitude();
        Double longitude = country.getLongitude();
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("해당 국가의 위치 정보가 없습니다.");
        }

        Map<String, Object> response;
        try {
            response = restClient.get()
                    .uri("/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=metric&lang=kr",
                            latitude, longitude, apiKey)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("날씨 정보를 가져오지 못했습니다.");
        }

        if (response == null || !response.containsKey("main")) {
            throw new IllegalArgumentException("날씨 정보를 가져오지 못했습니다.");
        }

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        Object temperature = main.get("temp");
        Object feelsLike = main.get("feels_like");

        if (temperature == null || feelsLike == null) {
            throw new IllegalArgumentException("날씨 정보를 가져오지 못했습니다.");
        }

        String description = "정보 없음";
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        if (weatherList != null && !weatherList.isEmpty()) {
            Object desc = weatherList.get(0).get("description");
            if (desc != null) {
                description = (String) desc;
            }
        }

        return new WeatherResponseDto(
                ((Number) temperature).doubleValue(),
                ((Number) feelsLike).doubleValue(),
                description
        );
    }
}
