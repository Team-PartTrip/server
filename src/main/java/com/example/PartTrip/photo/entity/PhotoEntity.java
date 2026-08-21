package com.example.PartTrip.photo.entity;

import com.example.PartTrip.signup.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="photo_manage")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

//    @ManyToOne
//    @JoinColumn(name="travel_id", nullable = false)
//    private TravelEntity travel;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @Column(name="img_url", nullable = false)
    private String imgUrl;

    @Column(name="latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @Column(name="comm_title")
    private String commTitle;

    @Column(name="comm_content")
    private String commContent;

    // 관련 사진을 사용자가 업로드 할 경우에만 사용
    @Column(name="photo_date")
    private LocalDate photoDate;

    @CreationTimestamp
    @Column(name="create_date", nullable = false)
    private LocalDateTime createDate;


    // 여행별 기록 관리 (Func-005-02) — 어느 여행 카드에 속한 사진인지.
    // 여행과 연결되지 않은 사진은 null 이다.
    @Column(name = "trip_card_id")
    private Long tripCardId;
}
