package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.TimelineItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TimelineItemResponse {
    private LocalDate date;
    private TimelineItemType type;
    private String placeName;
    private String address;
    private Double rating;
    private String imageUrl;
    private String comment;
    private LocalDateTime takenAt;
    private Double latitude;
    private Double longitude;
}
