package com.example.PartTrip.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mission_db")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mission_id;

    @Column(name="mission_title", nullable=false)
    private String title;

    @Column(name="mission_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="mission_category", nullable = false)
    private String MissionCategory;

    @Column(name="mission_point")
    private int point;

    @Column(name="is_passed_check", nullable = false)
    private boolean isPassedCheck;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="mission_country", nullable = false)
    private String country;

}
