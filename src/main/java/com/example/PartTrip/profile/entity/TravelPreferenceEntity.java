package com.example.PartTrip.profile.entity;

import com.example.PartTrip.profile.enums.PreferredTransport;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "travel_preference")
@Getter
@Setter
@NoArgsConstructor
public class TravelPreferenceEntity {

    @Id
    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_transport", nullable = false, length = 30)
    private PreferredTransport preferredTransport;

    @Column(name = "daily_schedule_count", nullable = false)
    private Integer dailyScheduleCount;

    @Column(name = "can_use_stairs", nullable = false)
    private Boolean canUseStairs;
}
