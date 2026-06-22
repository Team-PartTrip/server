package com.example.PartTrip.entity.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_manage")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "character_id")
    private String characterId;

}
