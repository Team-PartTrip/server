package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoResponseDto {

    private String foodName;

    private String description;

    private String imageUrl;
}