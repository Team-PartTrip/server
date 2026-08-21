package com.example.PartTrip.photo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 코멘트 수정 이력 (Func-005-05)
//
// 명세서 비고: "수정 날짜 이력 기록"
// 앱 D5 의 "수정 이력 — 최초 작성 2026.08.23 19:50 / 1차 수정 2026.08.24 09:12"
//
// 수정 직전의 내용을 한 행씩 쌓는다. 현재 내용은 photo_manage 에 있다.
@Entity
@Table(
        name = "photo_comment_history",
        indexes = @Index(name = "idx_photo_comment_history_photo",
                columnList = "photo_id, created_at")
)
@Getter
@Setter
@NoArgsConstructor
public class PhotoCommentHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_comment_history_id")
    private Long photoCommentHistoryId;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    // 수정되기 전의 내용
    @Column(name = "comm_title")
    private String commTitle;

    @Column(name = "comm_content")
    private String commContent;

    // 0 = 최초 작성, 1 = 1차 수정 ...
    @Column(name = "revision", nullable = false)
    private Integer revision;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
