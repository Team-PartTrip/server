package com.example.PartTrip.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_place_accessibility")
@Getter
@Setter
public class TourPlaceAccessibilityEntity {

    @Id
    private Long tourPlaceId;

    // 공공데이터의 장소 ID. 짝을 못 찾았으면 null
    @Column(length = 20)
    private String contentId;

    // 공공데이터 쪽 장소 이름. 짝이 맞았는지 사람이 확인할 때 본다
    private String matchedTitle;

    // [{key, label, text}] 를 JSON 으로 둔다. 항목이 공공데이터 필드 그대로라 표로 쪼갤 이유가 없다
    @Column(columnDefinition = "text")
    private String itemsJson;

    @Column(nullable = false)
    private LocalDateTime fetchedAt;
}
