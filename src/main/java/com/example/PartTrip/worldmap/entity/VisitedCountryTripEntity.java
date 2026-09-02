package com.example.PartTrip.worldmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "visited_country_trip",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_visited_country_trip_card",
                columnNames = "trip_card_id")
)
@Getter
@Setter
@NoArgsConstructor
public class VisitedCountryTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visited_country_trip_id")
    private Long visitedCountryTripId;

    @Column(name = "visited_country_id", nullable = false)
    private Long visitedCountryId;

    @Column(name = "trip_card_id", nullable = false)
    private Long tripCardId;

    @Column(name = "city_name", length = 100)
    private String cityName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
