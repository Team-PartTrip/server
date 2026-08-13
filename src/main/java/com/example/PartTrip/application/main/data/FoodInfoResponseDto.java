package com.example.PartTrip.application.main.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoResponseDto {

    private String foodName;

    private String description;

    private String imageUrl;
}