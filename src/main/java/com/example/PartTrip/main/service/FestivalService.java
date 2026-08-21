package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.FestivalResponseDto;
import com.example.PartTrip.main.entity.FestivalEntity;
import com.example.PartTrip.main.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    // 축제 조회
    // year, month 를 주지 않으면 조회 시점의 연·월을 기준으로 한다
    public List<FestivalResponseDto> getFestivals(String countryName,
                                                 Integer year,
                                                 Integer month) {

        LocalDate today = LocalDate.now();

        int targetYear = (year == null) ? today.getYear() : year;
        int targetMonth = (month == null) ? today.getMonthValue() : month;

        if (targetMonth < 1 || targetMonth > 12) {
            throw new IllegalArgumentException("월은 1 에서 12 사이의 값이어야 합니다.");
        }

        if (targetYear < 1900 || targetYear > 2100) {
            throw new IllegalArgumentException("연도는 1900 에서 2100 사이의 값이어야 합니다.");
        }

        // 'yyyy-MM' 형태로 만들어 startDate 접두사와 비교
        String yearMonth = String.format("%04d-%02d", targetYear, targetMonth);

        List<FestivalEntity> festivals =
                festivalRepository.findByCountryNameAndStartDateStartingWithOrderByStartDateAsc(
                        countryName, yearMonth);

        return festivals.stream()
                .map(festival -> new FestivalResponseDto(
                        festival.getFestivalId(),
                        festival.getTitle(),
                        festival.getCategory(),
                        festival.getDescription(),
                        festival.getStartDate(),
                        festival.getStartTime(),
                        festival.getLocation(),
                        festival.getImageUrl()
                ))
                .toList();
    }
}
