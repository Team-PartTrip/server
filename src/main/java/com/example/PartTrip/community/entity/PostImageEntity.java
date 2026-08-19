package com.example.PartTrip.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_image")
@Getter
@Setter
@NoArgsConstructor
public class PostImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    // 이미지가 속한 대상 타입: BOARD, REVIEW, TRIP
    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
