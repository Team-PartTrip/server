package com.example.PartTrip.tripcard.dto.response;

import com.example.PartTrip.tripcard.entity.MetadataSource;
import com.example.PartTrip.tripcard.entity.TimelineItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TimelineItemResponse {
    /** 사진 항목의 식별자. 삭제(API-003-07)와 촬영 정보 지정(API-003-09)에 쓴다. 장소 항목은 null */
    private Long entryId;
    private LocalDate date;
    private TimelineItemType type;
    private String placeName;
    private String address;
    private Double rating;
    private String imageUrl;
    private String comment;
    private LocalDateTime takenAt;
    private Double latitude;
    private Double longitude;
    /**
     * 사진의 좌표·촬영 시각이 EXIF 에서 온 값인지, 사용자가 직접 고른 값인지.
     * 앱은 이 값으로 "위치 지정" 버튼을 띄울지 정한다. EXIF 면 잠긴 값이라 띄우지 않는다.
     * 비어 있으면 아직 값이 없다는 뜻이다. 장소 항목은 둘 다 null.
     */
    private MetadataSource locationSource;
    private MetadataSource takenAtSource;
}
