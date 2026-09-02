"""
OpenStreetMap(Overpass API)에서 주요 여행 도시의 실제 장소를 가져와서
기존 tour_place 데이터는 건드리지 않고, 아직 없는 장소만 추가하는
SQL 파일(seed_tour_places.sql)을 생성한다.

데이터 출처:
  - 장소명/좌표/주소: https://overpass-api.de (OpenStreetMap, 공개, API 키 불필요)
  - 라이선스: ODbL (출처 표기 필요)

주의:
  OSM 에는 평점(rating)과 대표 이미지가 없다.
  없는 값을 지어내지 않고 NULL 로 둔다. 평점·이미지는 별도 소스가 정해지면 채운다.

실행:
    python3 seed_tour_places.py

생성된 SQL 적용:
    mysql -u root --default-character-set=utf8mb4 PartTrip < seed_tour_places.sql

    ※ --default-character-set=utf8mb4 를 빼면 한글이 깨져서 들어간다.
      (mysql 클라이언트가 UTF-8 바이트를 latin1 로 해석해 이중 인코딩됨)
"""

import json
import time
import urllib.parse
import urllib.request

import tour_place_ko

OVERPASS_URL = "https://overpass-api.de/api/interpreter"

# 시딩 대상 도시 (기능명세서 Func-008-02 인기 여행지 기준)
# bbox 는 관광 중심가로 좁게 잡는다. 넓게 잡으면 외곽 동네 가게가 섞여 들어온다.
CITIES = [
    # 우메다 ~ 난바 ~ 신세카이 (오사카성 포함)
    {"country": "일본",   "city": "오사카", "bbox": (34.64, 135.48, 34.71, 135.54)},
    # 왕궁 ~ 시암 ~ 수쿰윗
    {"country": "태국",   "city": "방콕",   "bbox": (13.72, 100.48, 13.78, 100.58)},
    # 한강 ~ 미케비치 ~ 손트라
    {"country": "베트남", "city": "다낭",   "bbox": (16.03, 108.20, 16.09, 108.26)},
]

# 카테고리별 OSM 태그 (앱 C4 카테고리 칩과 동일한 순서)
CATEGORIES = [
    ("RESTAURANT",    ['node["amenity"="restaurant"]["name"]']),
    ("ATTRACTION",    ['node["tourism"="attraction"]["name"]',
                       'node["tourism"="museum"]["name"]',
                       'way["historic"~"castle|monument|temple"]["name"]']),
    ("ACCOMMODATION", ['node["tourism"="hotel"]["name"]']),
    ("CAFE",          ['node["amenity"="cafe"]["name"]']),
    ("ACTIVITY",      ['node["tourism"="theme_park"]["name"]',
                       'way["leisure"="park"]["name"]']),
    ("SHOPPING",      ['node["shop"="mall"]["name"]',
                       'node["shop"="department_store"]["name"]']),
]

# 카테고리당 최대 장소 수
LIMIT_PER_CATEGORY = 12


def escape(value: str) -> str:
    """SQL 문자열 리터럴에 안전하게 넣기 위한 이스케이프"""
    return value.replace("\\", "\\\\").replace("'", "\\'")


def sql_value(value) -> str:
    """None 이면 NULL, 문자열이면 따옴표로 감싼다"""
    if value is None or value == "":
        return "NULL"
    if isinstance(value, (int, float)):
        return str(value)
    return "'" + escape(str(value)) + "'"


def query_overpass(selectors, bbox, attempts=4):
    """Overpass 는 공개 서버라 429/504 가 흔하다. 지수 백오프로 재시도한다."""
    south, west, north, east = bbox
    body = "\n".join(f"  {s}({south},{west},{north},{east});" for s in selectors)
    # 후보를 넉넉히 받아서 아래 rank_element() 로 유명한 순서대로 고른다
    query = f"[out:json][timeout:60];\n(\n{body}\n);\nout center 300;"

    last_error = None

    for attempt in range(attempts):
        if attempt:
            wait = 10 * (2 ** (attempt - 1))  # 10s, 20s, 40s
            print(f"      재시도 {attempt}/{attempts - 1} — {wait}초 대기 ({last_error})")
            time.sleep(wait)

        try:
            req = urllib.request.Request(
                OVERPASS_URL,
                data=urllib.parse.urlencode({"data": query}).encode("utf-8"),
                headers={"User-Agent": "PartTrip/1.0 (seed script)"},
            )
            with urllib.request.urlopen(req, timeout=120) as res:
                return json.loads(res.read().decode("utf-8")).get("elements", [])
        except Exception as e:
            last_error = f"{type(e).__name__} {e}"

    raise RuntimeError(last_error)


def pick_name(tags):
    """한국어 이름 > 영어 이름 > 원어 이름 순으로 고른다"""
    name = tags.get("name:ko") or tags.get("name:en") or tags.get("name")
    if not name:
        return None
    # OSM 은 별칭을 세미콜론으로 이어 붙인다 ("오사카성;오사카 성") → 첫 번째만 쓴다
    return name.split(";")[0].strip()


def rank_element(el):
    """
    유명한 장소일수록 높은 점수.

    OSM 에는 평점이나 방문자 수가 없어서 '얼마나 잘 문서화됐는가'를 유명세의
    대리 지표로 쓴다. wikidata / wikipedia 가 붙어 있으면 별도 문서가 있을 만큼
    알려진 곳이고, 다국어 이름이 붙어 있으면 외국인 방문이 많은 곳이다.
    이 신호가 없으면 동네 가게가 관광지보다 먼저 올라온다.
    """
    tags = el.get("tags") or {}
    score = 0

    if tags.get("wikidata"):
        score += 100
    if tags.get("wikipedia"):
        score += 50
    if tags.get("name:ko"):
        score += 30
    if tags.get("name:en"):
        score += 20
    if tags.get("website") or tags.get("contact:website"):
        score += 10
    if tags.get("stars"):
        score += 10

    # 태그가 많을수록 관리가 잘 되는 항목
    score += min(len(tags), 20)

    return score


def build_address(tags, city_name):
    """OSM addr:* 태그로 주소를 조립한다. 없으면 도시명만."""
    parts = [
        tags.get("addr:province") or tags.get("addr:state"),
        tags.get("addr:city") or city_name,
        tags.get("addr:suburb") or tags.get("addr:district"),
        tags.get("addr:street"),
        tags.get("addr:housenumber"),
    ]
    joined = " ".join(p for p in parts if p)
    return joined or city_name


def build_description(tags, category, city_name):
    """OSM 태그에서 확인되는 사실만으로 짧은 설명을 만든다"""
    labels = {
        "RESTAURANT": "맛집", "ATTRACTION": "명소", "ACCOMMODATION": "숙소",
        "CAFE": "카페", "ACTIVITY": "액티비티", "SHOPPING": "쇼핑",
    }
    bits = [f"{city_name}의 {labels[category]}"]

    cuisine = tags.get("cuisine")
    if cuisine:
        raw = cuisine.split(";")[0].replace("_", " ")
        # 카페의 "coffee shop" 은 요리가 아니다. 표에 있는 말은 그대로 붙인다.
        ko = tour_place_ko.CUISINES.get(raw)
        bits.append(ko if ko else raw + " 요리")

    stars = tags.get("stars")
    if stars:
        bits.append(f"{stars}성급")

    return " · ".join(bits)


def main():
    lines = [
        "-- OpenStreetMap(Overpass API) 공개 데이터로 자동 생성된 tour_place 시드 데이터",
        "-- 출처: © OpenStreetMap contributors, ODbL 라이선스",
        "-- 같은 나라/도시/장소명이 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.",
        "-- rating, image_url 은 OSM 에 없는 값이라 NULL 입니다.",
        "",
    ]

    total = 0
    summary = []

    for entry in CITIES:
        country_name = entry["country"]
        city_name = entry["city"]

        for category, selectors in CATEGORIES:
            try:
                elements = query_overpass(selectors, entry["bbox"])
            except Exception as e:
                print(f"  ! {city_name}/{category} 조회 실패: {type(e).__name__} {e}")
                summary.append((city_name, category, 0))
                continue

            # 유명한 순서대로 정렬한 뒤 위에서부터 채운다
            elements.sort(key=rank_element, reverse=True)

            seen = set()
            count = 0

            for el in elements:
                if count >= LIMIT_PER_CATEGORY:
                    break

                tags = el.get("tags") or {}
                # 앱에는 한국어만 보여야 한다. 표에 없으면 원문을 그대로 둔다.
                name = tour_place_ko.name(pick_name(tags) or "")
                if not name or name in seen:
                    continue

                lat = el.get("lat") or (el.get("center") or {}).get("lat")
                lon = el.get("lon") or (el.get("center") or {}).get("lon")
                if lat is None or lon is None:
                    continue

                seen.add(name)
                count += 1

                cn = escape(country_name)
                ci = escape(city_name)
                pn = escape(name)

                lines.append(
                    "INSERT INTO tour_place\n"
                    "  (country_name, city_name, place_name, category, description,\n"
                    "   address, rating, image_url, latitude, longitude)\n"
                    f"SELECT {sql_value(country_name)}, {sql_value(city_name)}, {sql_value(name)},\n"
                    f"       {sql_value(category)}, {sql_value(build_description(tags, category, city_name))},\n"
                    f"       {sql_value(build_address(tags, city_name))}, NULL, NULL,\n"
                    f"       {lat}, {lon}\n"
                    "FROM DUAL\n"
                    "WHERE NOT EXISTS (\n"
                    "  SELECT 1 FROM tour_place\n"
                    f"  WHERE country_name = '{cn}' AND city_name = '{ci}' AND place_name = '{pn}'\n"
                    ");\n"
                )
                total += 1

            print(f"  {city_name} / {category}: {count}건")
            summary.append((city_name, category, count))

            # Overpass 공개 서버는 동시 슬롯이 2개뿐이라 간격을 넉넉히 둔다
            time.sleep(6)

    with open("seed_tour_places.sql", "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print()
    print(f"총 {total}건의 INSERT 문을 seed_tour_places.sql 에 작성했습니다.")


if __name__ == "__main__":
    main()
