package com.example.PartTrip.photo.entity;

import com.example.PartTrip.photo.enums.PhotoAnalysisAccuracyCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="photo_analysis")
public class PhotoAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisId;

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

    // ai 결과 현황
    @Column(name="current_status", nullable = false)
    private String currentStatus;

    // ai 결과 출처
    @Column(name="source", nullable = false)
    private String source;

    // ai 분석 결과 정확도의 정도
    // vision에서 confidece(정확도?)를 전달받음 그거의 정도에 따라 정확도 단계를 구별하기 위함
    @Enumerated(EnumType.STRING)
    @Column(name="photo_analysis_accuracy_category", nullable = false)
    private PhotoAnalysisAccuracyCategory photoAnalysisAccuracyCategory;
}
