package com.example.PartTrip.planner.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum PlannerBlockType {

    TRAVEL_TYPE("여행 타입", true, "자연", "힐링", "미식", "역사", "문화예술", "체험", "쇼핑", "사진", "축제", "종교·순례", "추억 여행"),
    DEPARTURE_PLACE("출발 장소", false, "집 근처", "기차역", "버스터미널", "모임 집결지", "직접 지정"),
    COMPANION("동행 유형", false, "부부", "친구", "친목회", "동호회", "형제자매", "자녀·손주", "혼자"),
    PURPOSE("여행 목적", true, "친목", "기념일", "생일", "동창회", "휴식", "새로운 경험"),
    MOOD("여행 분위기", true, "조용한", "활기찬", "정겨운", "한적한", "특별한", "익숙한"),
    SEASON_THEME("계절 테마", true, "봄꽃", "여름 피서", "가을 단풍", "겨울 풍경", "계절 음식"),
    SIGHT_PLACE("관광 장소 유형", true, "공원", "정원", "수목원", "숲", "바다", "호수", "전망대", "온천", "전통시장", "한옥마을"),
    CULTURE_PLACE("문화 장소 유형", true, "박물관", "미술관", "유적지", "궁궐", "사찰", "성당", "공연장", "문학관"),
    ACTIVITY("체험 활동", true, "공예", "도예", "전통문화", "음식 만들기", "농촌 체험", "유람선", "관광열차"),
    FOOD_TYPE("음식 종류", true, "한식", "일식", "중식", "양식", "지역 향토음식", "채식"),
    TASTE("맛 선호", true, "맵지 않게", "담백하게", "자극 적게", "상관없음"),
    CAFE_SNACK("카페·간식", true, "카페 포함", "전통찻집", "빵·디저트", "지역 간식", "포함하지 않기"),
    WALKING("걷기 부담", false, "10분 이내", "30분 이내", "1시간 이상"),
    DAILY_DENSITY("일정 밀도", false, "하루 1~2곳", "하루 3~4곳", "직접 지정"),
    LODGING_TYPE("숙박 유형", false, "호텔", "리조트", "펜션", "한옥", "숙박 없음"),
    CROWD("혼잡·대기 조건", true, "붐비는 곳 피하기", "대기 짧은 곳", "혼잡 시간 피하기"),
    INDOOR_OUTDOOR("실내·실외 비율", false, "실내 중심", "실외 중심", "반반"),
    MUST_INCLUDE("반드시 포함할 곳", true, "특정 관광지", "식당", "행사", "가족 방문"),
    EXCLUDE("제외할 곳", true, "이미 다녀온 곳", "원하지 않는 장소", "특정 활동");

    private final String label;
    /** 여러 값을 같이 고를 수 있는지. 앱이 칩을 하나만 켤지 여러 개 켤지 정한다 */
    private final boolean multiple;
    private final List<String> options;

    PlannerBlockType(String label, boolean multiple, String... options) {
        this.label = label;
        this.multiple = multiple;
        this.options = List.of(options);
    }
}
