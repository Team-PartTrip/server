package com.example.PartTrip.planner.entity;

import com.example.PartTrip.planner.enums.GroupStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 여행 그룹 (Func-008-01)
@Entity
@Table(name = "travel_group")
@Getter
@Setter
@NoArgsConstructor
public class TravelGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    // 그룹을 만든 사람
    @Column(name = "owner_user_id", nullable = false)
    private String ownerUserId;

    @Column(name = "group_name", length = 100)
    private String groupName;

    // 명세서: "여행의 인원수를 설정 & 사용자를 지정. 혼자 여행할 경우는 혼자서"
    @Column(name = "headcount", nullable = false)
    private Integer headcount;

    // 초대 코드로 그룹에 합류시킨다
    @Column(name = "invite_code", unique = true, length = 20)
    private String inviteCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private GroupStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
