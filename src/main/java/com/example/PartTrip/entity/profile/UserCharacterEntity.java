package com.example.PartTrip.entity.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_character")
@Getter
@NoArgsConstructor
public class UserCharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_character_id")
    private Long userCharacterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfileEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private CharacterInfoEntity character;

    @Column(name = "user_level", nullable = false)
    private Integer userLevel;

    @Column(name = "character_point", nullable = false)
    private Integer characterPoint;
}
