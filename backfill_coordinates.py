"""
기존 country_info 행들에 위도/경도(latitude/longitude)를 채워 넣는 백필 스크립트.
country_name(한글)으로 매칭해서 이미 존재하는 국가만 UPDATE 한다 (새 행 추가 없음).

데이터 출처: https://github.com/mledoze/countries (공개, API 키 불필요)

실행:
    python3 backfill_coordinates.py

생성된 SQL 적용:
    mysql -u root PartTrip < backfill_coordinates.sql
"""

import json
import urllib.request

DATA_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json"


def escape(value: str) -> str:
    return value.replace("\\", "\\\\").replace("'", "\\'")


def main():
    req = urllib.request.Request(DATA_URL, headers={"User-Agent": "PartTrip/1.0"})
    with urllib.request.urlopen(req, timeout=30) as res:
        countries = json.loads(res.read().decode("utf-8"))

    lines = [
        "-- mledoze/countries 데이터로 기존 country_info 행에 위도/경도 백필",
        "-- country_name으로 매칭되는 기존 행만 UPDATE (새 행 추가 없음)",
        "",
    ]

    count = 0
    for c in countries:
        translations = c.get("translations") or {}
        kor = translations.get("kor") or {}
        country_name = kor.get("common") or (c.get("name") or {}).get("common")
        latlng = c.get("latlng") or []

        if not country_name or len(latlng) != 2:
            continue

        lat, lng = latlng
        cn = escape(country_name)
        lines.append(
            f"UPDATE country_info SET latitude = {lat}, longitude = {lng} "
            f"WHERE country_name = '{cn}' AND latitude IS NULL;"
        )
        count += 1

    with open("backfill_coordinates.sql", "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print(f"{count}개 국가에 대한 UPDATE 문을 backfill_coordinates.sql에 작성했습니다.")


if __name__ == "__main__":
    main()
