package com.example.PartTrip.photo.entity;

import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.photo.enums.GuideCameraMissionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "guide_camera_mission")
public class GuideCameraMissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_type", nullable = false)
    private GuideCameraMissionType missionType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "target_place_name", nullable = false)
    private String targetPlaceName;

    @Column(name = "place_type")
    private String placeType;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}