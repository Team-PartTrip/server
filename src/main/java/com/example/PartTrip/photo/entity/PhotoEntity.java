package com.example.PartTrip.photo.entity;

import com.example.PartTrip.entity.signup.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

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
    @Column(name = "photo_id", nullable = false)
    private String photoId;

//    @ManyToOne
//    @JoinColumn(name="travel_id", nullable = false)
//    private TravelEntity travel;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

    @Column(name="image_file", nullable = false)
    private MultipartFile imageFile;

    @Column(name="latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name="ai_commentary", nullable = false)
    private String aiCommentary;

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

    // ERD에 있어서 작성, 보류로 해둠
//    @Column(name="ai_commentary", nullable = false)
//    private String aiCommentary;

}
