package com.example.PartTrip.entity.home;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country_info")
@Getter
@Setter
@NoArgsConstructor
public class CountryInfoEntity {

    // 국가 정보 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_info_id")
    private Long countryInfoId;

}
