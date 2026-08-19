package com.example.PartTrip.mission.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mission_db")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long missionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "mission_title")
    private String missionTitle;

    @Column(name = "mission_description")
    private String missionDescription;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "mission_country")
    private String missionCountry;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_passed_check")
    private boolean isPassedCheck;

    @Column(name = "mission_category")
    private String missionCategory;

    @Column(name = "mission_point")
    private Integer missionPoint;
}