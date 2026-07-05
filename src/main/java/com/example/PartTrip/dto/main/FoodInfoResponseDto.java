package com.example.PartTrip.dto.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoResponseDto {

    private String foodName;

    private String description;

    private String imageUrl;
}