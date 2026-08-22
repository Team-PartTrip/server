"""
전 세계 국가 정보를 공개 데이터로 가져와서
기존 country_info 데이터는 건드리지 않고, 아직 없는 나라만 추가하는
SQL 파일(seed_countries.sql)을 생성한다.

데이터 출처:
  - 국가명/수도/지역/인구: https://github.com/mledoze/countries (공개, API 키 불필요)
  - 국기 이미지: https://flagcdn.com (공개 무료 CDN, API 키 불필요)

실행:
    python3 seed_countries.py

생성된 SQL 적용:
    mysql -u root PartTrip < seed_countries.sql
"""

import json
import sys
import urllib.request

DATA_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json"


def escape(value: str) -> str:
    """SQL 문자열 리터럴에 안전하게 넣기 위한 이스케이프"""
    return value.replace("\\", "\\\\").replace("'", "\\'")


def fetch_countries():
    req = urllib.request.Request(DATA_URL, headers={"User-Agent": "PartTrip/1.0"})
    with urllib.request.urlopen(req, timeout=30) as res:
        raw = res.read().decode("utf-8")

    data = json.loads(raw)

    if not isinstance(data, list):
        print("예상과 다른 형식(목록이 아님)의 응답입니다. 원본 응답 앞부분:")
        print(raw[:1000])
        sys.exit(1)

    return data


def build_summary(country_name: str, region: str, subregion: str, capital: str, population: int) -> str:
    place = subregion or region or ""
    pop_str = f"{population:,}" if population else "정보 없음"
    capital_str = capital or "정보 없음"
    return f"{country_name}은(는) {place}에 위치한 나라로, 수도는 {capital_str}이며 인구는 약 {pop_str}명입니다."


def main():
    countries = fetch_countries()
    print(f"{len(countries)}개 국가 정보를 받아왔습니다.")

    lines = [
        "-- 공개 국가 데이터(mledoze/countries + flagcdn.com)로 자동 생성된 시드 데이터",
        "-- 이미 country_name이 존재하는 행은 건너뛰므로 여러 번 실행해도 안전합니다.",
        "",
    ]

    count = 0
    skipped = 0
    for c in countries:
        if not isinstance(c, dict):
            skipped += 1
            continue

        translations = c.get("translations") or {}
        kor = translations.get("kor") or {}
        country_name = kor.get("common") or (c.get("name") or {}).get("common")
        if not country_name:
            skipped += 1
            continue

        capitals = c.get("capital") or []
        city_name = capitals[0] if capitals else country_name

        cca2 = (c.get("cca2") or "").lower()
        image_url = f"https://flagcdn.com/w320/{cca2}.png" if cca2 else ""

        region = c.get("region", "") or ""
        subregion = c.get("subregion", "") or ""
        population = c.get("population", 0) or 0

        summary = build_summary(country_name, region, subregion, city_name, population)

        cn = escape(country_name)
        ct = escape(city_name)
        img = escape(image_url)
        sm = escape(summary)

        lines.append(
            "INSERT INTO country_info (country_name, city_name, image_url, summary)\n"
            f"SELECT '{cn}', '{ct}', '{img}', '{sm}'\n"
            "FROM DUAL\n"
            f"WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '{cn}');\n"
        )
        count += 1

    with open("seed_countries.sql", "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print(f"{count}개 국가에 대한 INSERT 문을 seed_countries.sql 파일에 작성했습니다. (건너뜀: {skipped}개)")


if __name__ == "__main__":
    main()
