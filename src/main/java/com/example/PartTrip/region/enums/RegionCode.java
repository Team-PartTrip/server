package com.example.PartTrip.region.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 대한민국 시·도 17곳 (#162)
 *
 * 코드는 행정표준코드(법정동 코드 앞 2자리)다. 지도 GeoJSON 이 이 코드로
 * 구역을 구분하기 때문에, 우리가 고른 값이 아니라 지도가 정해 준 값이다.
 *
 * 강원(51)·전북(52)은 특별자치도로 바뀐 뒤의 코드를 쓴다. 웹이 들고 있는
 * 시·도 상수표가 이 값이다. 옛 코드(42·45)로 두면 그 두 곳만 지도에서
 * 안 칠해진다. 코드는 표준이 아니라 지도가 쓰는 값이 정답이다.
 *
 * 이름을 DB 에 저장하지 않고 여기서 꺼내 쓰는 이유는, 이름이 또 바뀌어도
 * (강원도 → 강원특별자치도) 이 줄만 고치면 되기 때문이다.
 */
@Getter
public enum RegionCode {

    SEOUL("11", "서울특별시"),
    BUSAN("26", "부산광역시"),
    DAEGU("27", "대구광역시"),
    INCHEON("28", "인천광역시"),
    GWANGJU("29", "광주광역시"),
    DAEJEON("30", "대전광역시"),
    ULSAN("31", "울산광역시"),
    SEJONG("36", "세종특별자치시"),
    GYEONGGI("41", "경기도"),
    GANGWON("51", "강원특별자치도"),
    CHUNGBUK("43", "충청북도"),
    CHUNGNAM("44", "충청남도"),
    JEONBUK("52", "전북특별자치도"),
    JEONNAM("46", "전라남도"),
    GYEONGBUK("47", "경상북도"),
    GYEONGNAM("48", "경상남도"),
    JEJU("50", "제주특별자치도");

    private static final Map<String, RegionCode> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(RegionCode::getCode, Function.identity()));

    private final String code;
    private final String regionName;

    RegionCode(String code, String regionName) {
        this.code = code;
        this.regionName = regionName;
    }

    public static RegionCode of(String code) {
        RegionCode region = BY_CODE.get(code);
        if (region == null) {
            throw new IllegalArgumentException("존재하지 않는 시·도 코드입니다: " + code);
        }
        return region;
    }

    /**
      * 이름을 못 찾으면 null 이다. 던지지 않는다.
      *
      * 지도에서 빠진 옛 여행 기록은 코드가 없고, 코드표에 없는 값이 DB 에 남아
      * 있을 수도 있다. 여기서 던지면 그 한 줄 때문에 D-day · 플래너 목록 ·
      * 여행카드 응답이 통째로 실패한다. 값을 받을 때는 of() 가 막는다.
      */
    public static String nameOf(String code) {
        RegionCode region = code == null ? null : BY_CODE.get(code);
        return region == null ? null : region.getRegionName();
    }

    public static boolean exists(String code) {
        return BY_CODE.containsKey(code);
    }
}
