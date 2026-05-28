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



}
