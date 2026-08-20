package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRateResponseDto {

    // 현지 통화 코드 (예: USD)
    private String currencyCode;

    // 1 currencyCode 당 원화 환율
    private Double krwRate;

    // 환율 기준일 (Frankfurter API 응답의 date)
    private String date;
}
