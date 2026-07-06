package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CalendarFestivalDto;
import com.example.PartTrip.service.main.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CalenderController {

    private final CalendarService calendarService;

    // 축제 캘린더 조회
    @GetMapping("/festivals")
    public List<CalendarFestivalDto> getCalendarFestivals(
            @RequestParam Long countryInfoId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return calendarService.getFestivals(countryInfoId, year, month);
    }
}
