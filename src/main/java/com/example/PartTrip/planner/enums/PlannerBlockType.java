package com.example.PartTrip.planner.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum PlannerBlockType {

    TRAVEL_TYPE("여행 타입", true, "자연", "힐링", "미식", "역사", "문화예술", "체험", "쇼핑", "사진", "축제", "종교·순례", "추억 여행"),
    DEPARTURE_PLACE("출발 장소", false, "집 근처", "기차역", "버스터미널", "모임 집결지", "직접 지정"),
    DEPART_RETURN_TIME("출발·귀가 시간", true, "늦은 출발", "이른 귀가", "직접 지정"),
    COMPANION("동행 유형", false, "부부", "친구", "친목회", "동호회", "형제자매", "자녀·손주", "혼자"),
    PARTY_SIZE("참여 인원", false, "성인 수", "어린이 수", "전체 인원"),
    PURPOSE("여행 목적", true, "친목", "기념일", "생일", "동창회", "휴식", "새로운 경험"),
    MOOD("여행 분위기", true, "조용한", "활기찬", "정겨운", "한적한", "특별한", "익숙한"),
    SEASON_THEME("계절 테마", true, "봄꽃", "여름 피서", "가을 단풍", "겨울 풍경", "계절 음식"),
    WEATHER("날씨 대응", true, "비 오면 실내", "더우면 야외 최소화", "추우면 실내 중심", "대체 코스 준비"),
    SIGHT_PLACE("관광 장소 유형", true, "공원", "정원", "수목원", "숲", "바다", "호수", "전망대", "온천", "전통시장", "한옥마을"),
    CULTURE_PLACE("문화 장소 유형", true, "박물관", "미술관", "유적지", "궁궐", "사찰", "성당", "공연장", "문학관"),
    ACTIVITY("체험 활동", true, "공예", "도예", "전통문화", "음식 만들기", "농촌 체험", "유람선", "관광열차"),
    WALK_PREFERENCE("산책 선호", false, "산책 없음", "평지 산책", "짧은 숲길", "해변 산책", "자유롭게 걷기"),
    FOOD_TYPE("음식 종류", true, "한식", "일식", "중식", "양식", "지역 향토음식", "채식"),
    MENU("먹고 싶은 메뉴", true, "생선", "해산물", "고기", "국·탕", "면", "정식", "죽", "시장 음식"),
    TASTE("맛 선호", true, "맵지 않게", "담백하게", "자극 적게", "상관없음"),
    TEXTURE("음식 질감", false, "부드러운 음식", "질긴 음식 피하기", "상관없음"),
    AVOID_FOOD("피할 음식", true, "알레르기 재료", "개인적으로 먹지 않는 재료", "직접 입력"),
    MEAL_TIME("식사 시간", true, "아침 포함", "점심·저녁 시간 고정", "규칙적인 식사"),
    RESTAURANT_CONVENIENCE("식당 편의", true, "의자 좌석", "단체석", "예약 가능", "주차 가능", "대기 짧은 곳"),
    CAFE_SNACK("카페·간식", true, "카페 포함", "전통찻집", "빵·디저트", "지역 간식", "포함하지 않기"),
    BUDGET("여행 예산", false, "1인당 총예산", "그룹 총예산", "여유 있게"),
    BUDGET_ITEMS("항목별 예산", true, "식사", "숙박", "교통", "입장료", "체험"),
    TRANSPORT("이동 수단", true, "자가용", "대절 차량", "기차", "버스", "대중교통", "택시", "도보"),
    LOCAL_TRANSPORT("도시 내 이동", false, "차량 중심", "대중교통 중심", "택시 병행", "걷기 중심"),
    TRANSFER("환승 허용", false, "환승 없이", "최소 환승", "상관없음"),
    TRAVEL_TIME_LIMIT("이동 시간 제한", true, "장소 간 최대 이동 시간", "하루 총 이동 시간"),
    WALKING("걷기 부담", false, "걷기 최소화", "조금 걷기", "충분히 걷기", "직접 지정"),
    CONTINUOUS_WALK("연속 보행 시간", false, "한 번에 10분 이내", "한 번에 20분 이내", "한 번에 30분 이내", "직접 지정"),
    DAILY_WALK("하루 보행량", false, "직접 지정"),
    STAIRS_SLOPE("계단·경사 조건", true, "계단 피하기", "급경사 피하기", "평지 중심"),
    MOBILITY_AID("이동 보조 조건", false, "휠체어 이용", "보행기 이용", "지팡이 이용", "별도 조건 없음"),
    ACCESSIBILITY("장소 접근성", true, "경사로", "엘리베이터", "무장애 화장실", "가까운 하차 지점"),
    REST_FREQUENCY("휴식 빈도", false, "장소마다 휴식", "일정 간격으로 휴식", "필요할 때"),
    REST_PLACE("휴식 장소", true, "벤치", "실내 쉼터", "카페", "숙소에서 낮 휴식"),
    RESTROOM("화장실 편의", true, "방문 장소마다 확인", "가까운 화장실 우선"),
    DAILY_DENSITY("일정 밀도", false, "하루 1~2곳", "하루 3~4곳", "직접 지정"),
    STAY_DURATION("장소별 체류 시간", false, "짧게 둘러보기", "충분히 머물기", "직접 지정"),
    LODGING_TYPE("숙박 유형", false, "호텔", "리조트", "펜션", "한옥", "숙박 없음"),
    LODGING_CONVENIENCE("숙소 편의", true, "침대", "엘리베이터", "조식", "주차", "욕실 편의", "관광지와 가까운 곳"),
    ROOM_SETUP("객실 구성", true, "1인실", "2인실", "가족실", "그룹별 객실"),
    CROWD("혼잡·대기 조건", true, "붐비는 곳 피하기", "대기 짧은 곳", "혼잡 시간 피하기"),
    INDOOR_OUTDOOR("실내·실외 비율", false, "실내 중심", "실외 중심", "반반"),
    MUST_INCLUDE("반드시 포함할 곳", true, "특정 관광지", "식당", "행사", "가족 방문"),
    EXCLUDE("제외할 곳", true, "이미 다녀온 곳", "원하지 않는 장소", "특정 활동"),
    RESERVATION("예약 조건", false, "예약 가능한 곳", "예약 없는 곳", "이미 예약한 일정 고정"),
    PRIORITY("일정 우선순위", true, "편안함", "음식", "비용", "이동 거리", "볼거리", "참여자 의견"),
    START_END_POINT("일정 시작·종료 지점", false, "같은 곳으로 복귀", "숙소 종료", "역·터미널 종료");

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
