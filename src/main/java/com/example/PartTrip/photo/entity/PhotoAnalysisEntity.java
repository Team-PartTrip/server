package com.example.PartTrip.photo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="photo_analysis")
public class PhotoAnalysisEntity {
    @Id
    @Column(name="analysis_id", nullable = false)
    private String analysisId;

    @OneToOne
    @JoinColumn(name="photo_id", nullable = false)
    private PhotoEntity photo;

    // ai 결과 제목
    @Column(name="title", nullable = false)
    private String title;

    // ai 결과 연대
    @Column(name="era", nullable = false)
    private String era;

    // ai 결과 부제목/지정, 지시 ex) 유네스코세계문화유산 혹은 부제목 넣을 예정
    @Column(name="designation", nullable = false)
    private String designation;

    // ai 결과 제목
    @Column(name="overview", nullable = false)
    private String overview;

    // ai 결과 유래/역사
    @Column(name="background", nullable = false)
    private String background;

    // ai 결과 특징
    @Column(name="features", nullable = false)
    private String features;

    // ai 결과
    @Column(name="current_status", nullable = false)
    private String current_status;
}
