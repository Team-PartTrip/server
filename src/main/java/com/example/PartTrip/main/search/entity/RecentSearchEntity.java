package com.example.PartTrip.main.search.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recent_search")
public class RecentSearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recentSearchId;

    private String userId;   // String으로 변경

    private Long countryInfoId;

    private LocalDateTime searchedAt;
}