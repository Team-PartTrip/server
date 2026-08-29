# DB 시드 · 백필 스크립트

`country_info`, `tour_place`, `travel_theme` 처럼 **애플리케이션이 만들지 않고 미리 채워 넣어야 하는
기준 데이터**를 생성하는 스크립트입니다.

스키마 자체는 JPA(`ddl-auto=update`)가 만들므로 여기에 테이블 생성 SQL은 없습니다.

## 파일

| 파일 | 채우는 대상 | 데이터 출처 |
|---|---|---|
| `seed_countries.py` → `seed_countries.sql` | `country_info` (국가명 · 수도 · 지역 · 국기) | [mledoze/countries](https://github.com/mledoze/countries), [flagcdn.com](https://flagcdn.com) |
| `backfill_coordinates.py` → `backfill_coordinates.sql` | `country_info.latitude/longitude` | mledoze/countries |
| `seed_tour_places.py` → `seed_tour_places.sql` | `tour_place` (오사카 · 방콕 · 다낭 205곳) | [Overpass API](https://overpass-api.de) (OpenStreetMap, ODbL) |
| `seed_travel_themes.sql` | `travel_theme` | 직접 작성 |
| `seed_festivals.sql` | `festival` (7개국 39건) | 직접 작성 |
| `seed_festivals_public.py` → `seed_festivals_public.sql` | `festival` (51개국 281건) | [위키데이터](https://query.wikidata.org) (CC0), [Nager.Date](https://date.nager.at) |

`.py` 는 공개 데이터를 받아 `.sql` 을 **생성**하고, `.sql` 은 그걸 DB에 **적용**합니다.
이미 생성된 `.sql` 이 함께 들어 있으니 보통은 `.sql` 만 실행하면 됩니다.

`.py` 를 다시 돌릴 필요가 있을 때만 실행하세요. 외부 API를 호출하므로 네트워크가 필요하고,
Overpass 는 공개 서버라 결과가 실행 시점마다 조금씩 달라집니다.

## 실행

```bash
cd db
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_countries.sql
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < backfill_coordinates.sql
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_tour_places.sql
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_travel_themes.sql
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_festivals.sql
mysql -u <user> -p --default-character-set=utf8mb4 PartTrip < seed_festivals_public.sql
```

순서는 `seed_countries` → `backfill_coordinates` 만 지키면 됩니다. 나머지는 서로 독립입니다.

### ⚠️ `--default-character-set=utf8mb4` 를 빼지 마세요

빼면 한글이 깨져서 들어갑니다. mysql 클라이언트가 UTF-8 바이트를 latin1 로 해석해
이중 인코딩되기 때문입니다.

```
일본  →  ì¼ë³¸
```

### 클라우드 DB(Aiven)에 적용할 때

SSL 이 필수입니다.

```bash
mysql -h <host> -P <port> -u <user> -p \
      --ssl-mode=REQUIRED --default-character-set=utf8mb4 \
      <database> < seed_countries.sql
```

접속 정보는 저장소에 없습니다. `application.properties` 는 gitignore 되어 있고
**이 저장소는 공개이니 비밀번호를 절대 커밋하지 마세요.**

## 여러 번 실행해도 안전합니다

시드 스크립트는 `WHERE NOT EXISTS` 로 이미 있는 행을 건너뛰고,
백필 스크립트는 기존 행을 `UPDATE` 할 뿐 새 행을 만들지 않습니다.

## 축제 날짜에 대해

`seed_festivals.sql` 의 날짜는 각 축제가 해마다 열리는 **통상 일정**을 2026년으로 옮겨 적은
것입니다. 주최 측이 2026년 공식 일정을 발표하면 그 날짜로 교체해 주세요.

`start_time` 과 `image_url` 은 확인된 값이 없으면 NULL 입니다. 앱은 시각이 없으면 날짜만
보여주고, 이미지는 아직 화면에서 쓰지 않습니다.

## 아직 비어 있는 것

`tour_place.rating` 과 `tour_place.image_url` 은 **205행 전부 NULL** 입니다.
OpenStreetMap 에는 평점과 대표 이미지가 없어서 없는 값을 지어내지 않고 비워 두었습니다.
별도 소스가 정해지면 백필 스크립트를 추가하세요.

`festival` 은 두 시드를 합쳐 53개국 321건입니다. 나머지 나라를 여행지로 고르면
축제 화면이 비어 보입니다.

공개 데이터에서 온 281건 중 위키데이터에 한국어 이름이 있는 건 26건뿐이라,
나머지는 `festival_ko.py` 에 표기를 손으로 적어 두고 시드를 만들 때 갖다 씁니다
(축제 이름 254개 · 장소 이름 91개). 시드를 다시 뽑아도 한국어 표기는 유지됩니다.
새 축제가 늘면 이 표에 한 줄 더 적으면 됩니다.

분류는 위키데이터의 종류를 키워드로 옮긴 것이라 대부분 `체험` 으로 몰립니다.
사람이 확인해 고칠 값이 있다면 이쪽입니다.
