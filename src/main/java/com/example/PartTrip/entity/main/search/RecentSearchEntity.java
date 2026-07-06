package com.example.PartTrip.entity.main.search;

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

    private Long userId;

    private Long countryInfoId;

    private LocalDateTime searchedAt;
}