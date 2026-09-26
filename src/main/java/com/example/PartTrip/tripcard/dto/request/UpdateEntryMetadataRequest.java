package com.example.PartTrip.tripcard.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 사진 촬영 정보 직접 지정(API-003-09)의 요청 본문은 { latitude, longitude, placeName, takenAt } 다.
//
// 보내지 않은 항목은 그대로 둔다. 값을 비우는 기능은 없다 — 잘못 골랐으면 다시 고르면 된다.
// 좌표 범위 검사는 위치 공유(LocationDtos.UpdateRequest)와 같은 규칙을 쓴다.
// 쌍 검사(위도만 왔거나, 이름만 왔거나)는 서비스에서 본다.
@Getter
@Setter
public class UpdateEntryMetadataRequest {

    @DecimalMin(value = "-90", message = "위도가 올바르지 않습니다.")
    @DecimalMax(value = "90", message = "위도가 올바르지 않습니다.")
    private Double latitude;

    @DecimalMin(value = "-180", message = "경도가 올바르지 않습니다.")
    @DecimalMax(value = "180", message = "경도가 올바르지 않습니다.")
    private Double longitude;

    @Size(max = 255, message = "장소 이름은 255자까지 쓸 수 있습니다.")
    private String placeName;

    // 촬영 시각. 앱이 날짜만 고르게 한다면 00:00 으로 보내면 된다.
    // 서버가 올린 시각을 대신 넣는 일은 없다 (TripCardEntryServiceImpl.addEntry 주석 참고).
    private LocalDateTime takenAt;
}
