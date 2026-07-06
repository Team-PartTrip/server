package com.example.PartTrip.entity.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "character_info")
@Getter
@NoArgsConstructor
public class CharacterInfoEntity {

    @Id
    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "character_name", nullable = false)
    private String characterName;

    @Column(name = "character_type", nullable = false)
    private String characterType;

    @Column(name = "character_level", nullable = false)
    private Integer characterLevel;

    @Column(name = "character_description", nullable = false)
    private String characterDescription;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;
}