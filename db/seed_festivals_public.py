"""
공개 데이터로 축제 시드 SQL(seed_festivals_public.sql)을 생성한다.

한 곳에서 다 받아올 수가 없어서 두 곳을 겹쳐 쓴다.

  - 위키데이터 : 축제가 무엇인지 (이름 · 설명 · 장소 · 사진 · 나라)
                 https://query.wikidata.org  (CC0, API 키 불필요)
  - Nager.Date : 그 축제가 2026년 며칠인지
                 https://date.nager.at       (공개, API 키 불필요, 204개국)
  - mledoze/countries : 나라 이름을 country_info 와 같은 한국어 표기로 맞추는 용도
                 https://github.com/mledoze/countries

위키데이터에는 축제가 1만 8천 건 있지만 '해마다 며칠에 열리는가'가 적힌 건 478건뿐이다.
반대로 Nager.Date 는 2026년 날짜를 정확히 주지만 공휴일 목록이라 축제가 아닌 것이 섞여 있다.
그래서 둘을 겹쳐서, **위키데이터가 축제라고 말하고 날짜를 알 수 있는 것**만 남긴다.

날짜를 정하는 순서
  1) Nager.Date 의 2026년 공휴일과 이름이 맞으면 그 날짜  ← 해마다 날이 바뀌는 축제도 정확하다
  2) 위키데이터 P837(해마다 열리는 날)이 양력 월·일이면 2026년의 그 날
  3) 둘 다 없으면 버린다 (start_date 는 비워둘 수 없다)

실행:
    python3 seed_festivals_public.py

생성된 SQL 적용:
    mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_festivals_public.sql
"""

import json
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request

YEAR = 2026

COUNTRIES_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json"
SPARQL_URL = "https://query.wikidata.org/sparql"
NAGER_URL = "https://date.nager.at/api/v3/PublicHolidays/{year}/{code}"

UA = "PartTrip/1.0 (student project)"

# 위키데이터 분류를 명세서(Func-002-03)의 다섯 가지로 옮긴다. 위에 있는 것부터 맞춰본다.
CATEGORY_RULES = [
    ("음식", ["음식", "맥주", "포도주", "와인", "food", "beer", "wine", "gastronom"]),
    ("공연", ["음악", "영화", "연극", "무용", "공연", "music", "film", "theatre",
              "theater", "dance", "carnival", "카니발", "퍼레이드", "parade"]),
    ("예술품", ["미술", "예술", "art", "biennale", "비엔날레", "등불", "lantern"]),
    ("건물", ["건축", "architect", "불꽃", "firework"]),
]
DEFAULT_CATEGORY = "체험"

# 축제 이름 같지 않은 항목을 걸러낸다.
# 위키데이터에는 개별 회차("2019 Rio Carnival")나 결과 문서도 축제로 등록돼 있다.
JUNK_PATTERNS = [
    r"\d{4}",                       # 연도가 들어간 회차 항목
    r"results?\b", r"edition\b", r"season\b",
    r"^list of", r"목록$",
]

Q_LABELS = """
SELECT ?f ?cca2 ?label WHERE {
  ?f wdt:P31/wdt:P279* wd:Q132241 ; wdt:P17 ?c .
  ?c wdt:P297 ?cca2 .
  ?f rdfs:label ?label . FILTER(lang(?label) IN ("ko","en"))
}
"""

Q_DAYS = """
SELECT ?f ?dayLabel WHERE {
  ?f wdt:P31/wdt:P279* wd:Q132241 ; wdt:P17 [] ; wdt:P837 ?day .
  SERVICE wikibase:label { bd:serviceParam wikibase:language "ko,en". }
}
"""

Q_DETAIL = """
SELECT ?f ?ko ?desc ?locLabel ?img ?typeLabel WHERE {
  VALUES ?f { %s }
  OPTIONAL { ?f rdfs:label ?ko . FILTER(lang(?ko) = "ko") }
  OPTIONAL { ?f schema:description ?desc . FILTER(lang(?desc) = "ko") }
  OPTIONAL { ?f wdt:P276 ?loc }
  OPTIONAL { ?f wdt:P18 ?img }
  OPTIONAL { ?f wdt:P31 ?type }
  SERVICE wikibase:label { bd:serviceParam wikibase:language "ko,en". }
}
"""


def get_json(url: str, timeout: int = 120):
    req = urllib.request.Request(url, headers={"User-Agent": UA})
    with urllib.request.urlopen(req, timeout=timeout) as res:
        return json.loads(res.read().decode("utf-8"))


def run_sparql(query: str, attempts: int = 4):
    """공개 엔드포인트라 붐빌 때 502 가 난다. 몇 번 다시 시도한다."""
    body = urllib.parse.urlencode({"query": query, "format": "json"}).encode()
    for attempt in range(1, attempts + 1):
        try:
            req = urllib.request.Request(
                SPARQL_URL, data=body,
                headers={"User-Agent": UA, "Accept": "application/sparql-results+json"},
            )
            with urllib.request.urlopen(req, timeout=300) as res:
                return json.loads(res.read().decode("utf-8"))["results"]["bindings"]
        except Exception as e:
            if attempt == attempts:
                raise
            print(f"  위키데이터 응답 실패({e}) — {attempt * 10}초 뒤 다시 시도합니다")
            time.sleep(attempt * 10)


def escape(value: str) -> str:
    """SQL 문자열 리터럴에 안전하게 넣기 위한 이스케이프"""
    return value.replace("\\", "\\\\").replace("'", "\\'")


def korean_country_names() -> dict:
    """ISO 2자리 코드 -> country_info 와 같은 한국어 국가명"""
    names = {}
    for c in get_json(COUNTRIES_URL, timeout=60):
        code = (c.get("cca2") or "").upper()
        kor = (c.get("translations") or {}).get("kor") or {}
        name = kor.get("common") or (c.get("name") or {}).get("common")
        if code and name:
            names[code] = name
    return names


def fetch_holidays(codes) -> dict:
    """나라별 2026년 공휴일. Nager.Date 가 모르는 나라는 빈 목록으로 둔다"""
    holidays = {}
    for code in sorted(codes):
        try:
            holidays[code] = get_json(NAGER_URL.format(year=YEAR, code=code), timeout=30)
        except Exception:
            holidays[code] = []
    return holidays


def normalize(text: str) -> str:
    """이름을 맞춰보기 전에 괄호·기호·공백을 걷어낸다"""
    text = re.sub(r"\(.*?\)", " ", text or "")
    text = re.sub(r"[^0-9A-Za-z가-힣]+", " ", text)
    return " ".join(text.split()).lower()


def is_junk(label: str) -> bool:
    low = (label or "").lower()
    return any(re.search(p, low) for p in JUNK_PATTERNS)


def match_holiday(labels, holidays):
    """공휴일 이름이 축제 이름 안에 '단어 단위'로 들어 있을 때만 같은 행사로 본다.

    느슨하게 보면 Carnatal 이 Christmas Day 에 붙는 식으로 엉뚱하게 묶인다.
    """
    normalized = [normalize(label) for label in labels]
    normalized = [n for n in normalized if len(n) >= 4]

    for holiday in holidays:
        for name in (normalize(holiday.get("name")), normalize(holiday.get("localName"))):
            if len(name) < 6:
                continue
            for label in normalized:
                if label == name or re.search(rf"(^|\s){re.escape(name)}($|\s)", label):
                    return holiday["date"], holiday.get("name")
    return None, None


DAY_KO = re.compile(r"^(\d{1,2})\s*월\s*(\d{1,2})\s*일$")
DAY_EN = re.compile(
    r"^(January|February|March|April|May|June|July|August|September|October|November|December)"
    r"\s+(\d{1,2})$",
    re.IGNORECASE,
)
MONTHS = ["january", "february", "march", "april", "may", "june",
          "july", "august", "september", "october", "november", "december"]


def day_in_year_date(label: str):
    """'8월 15일' 또는 'August 15' → '2026-08-15'.

    '10월' 처럼 달만 있거나 힌두력 날짜인 값은 양력으로 옮길 수 없어 버린다.
    """
    if not label:
        return None
    label = label.strip()

    korean = DAY_KO.match(label)
    if korean:
        month, day = int(korean.group(1)), int(korean.group(2))
    else:
        english = DAY_EN.match(label)
        if not english:
            return None
        month = MONTHS.index(english.group(1).lower()) + 1
        day = int(english.group(2))

    if not (1 <= month <= 12 and 1 <= day <= 31):
        return None
    try:
        import datetime
        datetime.date(YEAR, month, day)
    except ValueError:
        return None
    return f"{YEAR}-{month:02d}-{day:02d}"


def fetch_details(uris):
    """살아남은 축제만 상세를 받아온다. VALUES 가 길면 잘라서 여러 번 부른다."""
    details = {}
    batch = 150
    uri_list = sorted(uris)

    for i in range(0, len(uri_list), batch):
        chunk = " ".join(f"<{u}>" for u in uri_list[i:i + batch])
        for row in run_sparql(Q_DETAIL % chunk):
            def val(key):
                return row.get(key, {}).get("value")

            item = details.setdefault(val("f"), {"types": set()})
            if val("typeLabel"):
                item["types"].add(val("typeLabel"))
            for field in ("ko", "desc", "img"):
                if not item.get(field) and val(field):
                    item[field] = val(field)
            if not item.get("loc") and val("locLabel"):
                item["loc"] = val("locLabel")
        print(f"  상세 {min(i + batch, len(uri_list))}/{len(uri_list)}")

    return details


def pick_category(types, title) -> str:
    haystack = " ".join(types).lower() + " " + (title or "").lower()
    for category, keywords in CATEGORY_RULES:
        if any(keyword.lower() in haystack for keyword in keywords):
            return category
    return DEFAULT_CATEGORY


def main():
    print("국가 한국어 표기를 받는 중…")
    country_names = korean_country_names()

    print("위키데이터에서 축제 이름을 받는 중… (1~2분)")
    festivals = {}
    for row in run_sparql(Q_LABELS):
        key = (row["f"]["value"], row["cca2"]["value"])
        festivals.setdefault(key, set()).add(row["label"]["value"])
    print(f"  축제 {len({k[0] for k in festivals})}건 / {len({k[1] for k in festivals})}개국")

    print("해마다 열리는 날(P837)을 받는 중…")
    days = {}
    for row in run_sparql(Q_DAYS):
        days.setdefault(row["f"]["value"], row.get("dayLabel", {}).get("value"))

    codes = {k[1] for k in festivals if k[1] in country_names}
    print(f"Nager.Date 에서 {len(codes)}개국의 {YEAR}년 공휴일을 받는 중…")
    holidays = fetch_holidays(codes)

    # 날짜를 알 수 있는 것만 추린다
    dated = {}
    from_holiday = from_day = 0
    for (uri, code), labels in festivals.items():
        if code not in country_names:
            continue
        if all(is_junk(label) for label in labels):
            continue

        date, _ = match_holiday(labels, holidays.get(code, []))
        source = "공휴일"
        if date:
            from_holiday += 1
        else:
            date = day_in_year_date(days.get(uri))
            source = "위키데이터"
            if date:
                from_day += 1
        if not date:
            continue

        dated[(uri, code)] = (date, labels, source)

    print(f"날짜를 알아낸 축제 {len(dated)}건 "
          f"(공휴일 대조 {from_holiday} · 연례 개최일 {from_day})")

    print("살아남은 축제의 상세를 받는 중…")
    details = fetch_details({uri for uri, _ in dated})

    rows = []
    seen = set()
    for (uri, code), (date, labels, source) in dated.items():
        detail = details.get(uri, {})
        country_name = country_names[code]

        # 한국어 이름이 있으면 그걸 쓰고, 없으면 영어 이름을 그대로 쓴다
        title = detail.get("ko") or sorted(labels, key=len)[0]
        if is_junk(title):
            continue

        key = (country_name, title, date)
        if key in seen:
            continue
        seen.add(key)

        description = detail.get("desc") or f"{country_name}에서 열리는 축제"
        rows.append({
            "country_name": country_name,
            "title": title[:255],
            "category": pick_category(detail.get("types", set()), title),
            "description": description[:500],
            "start_date": date,
            "location": (detail.get("loc") or country_name)[:255],
            "image_url": detail.get("img"),
        })

    rows.sort(key=lambda r: (r["country_name"], r["start_date"], r["title"]))

    lines = [
        "-- 공개 데이터로 자동 생성된 축제 시드 (Func-002-03)",
        "--   축제 정보 : 위키데이터 (CC0)  https://query.wikidata.org",
        "--   2026년 날짜: Nager.Date       https://date.nager.at",
        "--   국가명 표기: mledoze/countries",
        "-- 같은 나라 · 같은 이름 · 같은 날짜가 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.",
        "--",
        f"-- 날짜 출처: 공휴일 대조 {from_holiday}건 / 위키데이터 연례 개최일 {from_day}건",
        "-- 날짜를 어느 쪽에서도 알 수 없는 축제는 넣지 않았습니다.",
        "-- 한국어 이름이 없는 축제는 영어 이름 그대로 들어갑니다.",
        "",
    ]

    for row in rows:
        image = "NULL" if not row["image_url"] else f"'{escape(row['image_url'])}'"
        lines.append(
            "INSERT INTO festival "
            "(country_name, title, category, description, start_date, start_time, location, image_url)\n"
            f"SELECT '{escape(row['country_name'])}', '{escape(row['title'])}', "
            f"'{row['category']}', '{escape(row['description'])}', "
            f"'{row['start_date']}', NULL, '{escape(row['location'])}', {image}\n"
            "FROM DUAL WHERE NOT EXISTS (\n"
            f"    SELECT 1 FROM festival WHERE country_name = '{escape(row['country_name'])}' "
            f"AND title = '{escape(row['title'])}' AND start_date = '{row['start_date']}');\n"
        )

    with open("seed_festivals_public.sql", "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    countries = len({r["country_name"] for r in rows})
    print(f"\nseed_festivals_public.sql 생성 완료 — {len(rows)}건 / {countries}개국")


if __name__ == "__main__":
    try:
        main()
    except urllib.error.URLError as e:
        print(f"공개 데이터를 받지 못했습니다: {e}")
        sys.exit(1)
