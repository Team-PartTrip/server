package com.example.PartTrip.domain.main.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "today_phrase")
@Getter
@Setter
@NoArgsConstructor
public class TodayPhraseEntity {

    // 오늘의 한마디 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "today_phrase_id")
    private Long todayPhraseId;

    // 나라 이름
    @Column(name = "country_name", nullable = false)
    private String countryName;

    // Day 번호
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    // 현지 표현
    @Column(name = "phrase", nullable = false)
    private String phrase;

    // 한국어 뜻
    @Column(name = "meaning", nullable = false)
    private String meaning;
}