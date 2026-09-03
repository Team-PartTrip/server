-- 축제 · 이벤트 시드 데이터 (Func-002-03)
-- 같은 나라 · 같은 이름 · 같은 날짜가 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.
--
-- ※ 날짜는 각 축제가 해마다 열리는 통상 일정을 2026년으로 옮겨 적은 것입니다.
--   주최 측이 2026년 공식 일정을 발표하면 그 날짜로 교체해 주세요.
-- ※ start_time 과 image_url 은 확인된 값이 없으면 NULL 로 둡니다. 없는 값을 지어내지 않습니다.
-- ※ country_name 은 country_info 의 표기를 그대로 따릅니다. (예: '대한민국' 아님, '한국')

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '화천 산천어축제', '체험', '꽁꽁 언 강 위에서 산천어를 잡는 겨울 축제', '2026-01-10', NULL, '강원 화천 화천천', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '화천 산천어축제' AND start_date = '2026-01-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '진해군항제', '체험', '벚꽃길을 따라 걷는 국내 최대 봄 축제', '2026-03-27', NULL, '경남 창원 진해구', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '진해군항제' AND start_date = '2026-03-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '보령 머드축제', '체험', '서해안 갯벌 진흙으로 즐기는 여름 축제', '2026-07-17', NULL, '충남 보령 대천해수욕장', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '보령 머드축제' AND start_date = '2026-07-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '부산 바다축제', '공연', '해운대와 광안리 해변에서 열리는 여름 음악 축제', '2026-08-01', NULL, '부산 해운대해수욕장', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '부산 바다축제' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '안동국제탈춤페스티벌', '공연', '세계 각국의 탈춤 공연이 이어지는 가을 축제', '2026-09-25', NULL, '경북 안동 탈춤공원', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '안동국제탈춤페스티벌' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '서울세계불꽃축제', '공연', '한강 위로 각국 팀이 불꽃을 쏘아 올리는 축제', '2026-10-03', '19:00', '서울 여의도 한강공원', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '서울세계불꽃축제' AND start_date = '2026-10-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '삿포로 눈축제', '건물', '오도리 공원을 채우는 대형 눈·얼음 조각 축제', '2026-02-04', NULL, '홋카이도 삿포로 오도리 공원', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '삿포로 눈축제' AND start_date = '2026-02-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다카야마 마츠리', '건물', '정교한 장식 수레가 옛 거리를 도는 봄 축제', '2026-04-14', NULL, '기후현 다카야마 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다카야마 마츠리' AND start_date = '2026-04-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '기온 마츠리', '공연', '야마보코 수레 순행으로 절정을 맞는 교토의 여름 축제', '2026-07-17', NULL, '교토 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '기온 마츠리' AND start_date = '2026-07-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아오모리 네부타 마츠리', '예술품', '거대한 등 인형이 밤거리를 행진하는 축제', '2026-08-02', '19:00', '아오모리 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아오모리 네부타 마츠리' AND start_date = '2026-08-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아와오도리', '공연', '도쿠시마 거리를 가득 채우는 전통 군무 축제', '2026-08-12', NULL, '도쿠시마 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아와오도리' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '기시와다 단지리 마츠리', '체험', '거대한 목조 수레를 끌고 달리는 오사카의 가을 축제', '2026-09-19', NULL, '오사카 기시와다', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '기시와다 단지리 마츠리' AND start_date = '2026-09-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '핑시 천등축제', '체험', '소원을 적은 천등을 밤하늘에 띄우는 축제', '2026-03-03', '18:00', '신베이 핑시', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '핑시 천등축제' AND start_date = '2026-03-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '다자 마쭈 순행', '체험', '마쭈 신상을 모시고 아흐레를 걷는 순례 행렬', '2026-04-10', NULL, '타이중 다자 전란궁', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '다자 마쭈 순행' AND start_date = '2026-04-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '타이완 열기구 페스티벌', '체험', '초원 위로 열기구가 떠오르는 여름 축제', '2026-07-04', '05:30', '타이둥 루예 고원', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '타이완 열기구 페스티벌' AND start_date = '2026-07-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '지룽 중원제', '공연', '등불 행렬과 물등 띄우기로 이어지는 백중 축제', '2026-08-25', NULL, '지룽 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '지룽 중원제' AND start_date = '2026-08-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '타이베이 중추절', '음식', '달을 보며 월병과 바비큐를 나누는 명절', '2026-09-25', NULL, '타이베이 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '타이베이 중추절' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '타이중 재즈 페스티벌', '공연', '공원 곳곳에서 무료 공연이 열리는 야외 재즈 축제', '2026-10-16', NULL, '타이중 시민광장', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '타이중 재즈 페스티벌' AND start_date = '2026-10-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '태국', '송끄란', '체험', '서로에게 물을 뿌리며 새해를 맞는 축제', '2026-04-13', NULL, '방콕 카오산로드', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '태국' AND title = '송끄란' AND start_date = '2026-04-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '태국', '라용 과일 축제', '음식', '두리안과 망고스틴을 맛보는 여름 과일 축제', '2026-05-15', NULL, '라용 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '태국' AND title = '라용 과일 축제' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '태국', '태국 어머니의 날', '체험', '왕비 탄신일을 기념해 거리를 밝히는 행사', '2026-08-12', NULL, '방콕 왕궁 앞 광장', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '태국' AND title = '태국 어머니의 날' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '태국', '푸껫 채식 축제', '음식', '아홉 신을 기리며 열흘간 채식을 하는 축제', '2026-10-11', NULL, '푸껫 타운', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '태국' AND title = '푸껫 채식 축제' AND start_date = '2026-10-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '태국', '러이끄라통', '공연', '강에 등불 배를 띄워 소원을 비는 축제', '2026-11-24', '18:00', '방콕 차오프라야강', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '태국' AND title = '러이끄라통' AND start_date = '2026-11-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '뗏 응우옌단', '체험', '꽃 시장과 사자춤으로 새해를 맞는 최대 명절', '2026-02-17', NULL, '호치민 응우옌후에 거리', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '뗏 응우옌단' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '후에 페스티벌', '공연', '왕궁을 무대로 열리는 국제 예술 축제', '2026-06-06', NULL, '후에 왕궁', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '후에 페스티벌' AND start_date = '2026-06-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '다낭 국제 불꽃축제', '공연', '한강 위에서 각국 팀이 겨루는 불꽃 대회', '2026-06-13', '20:00', '다낭 한강변', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '다낭 국제 불꽃축제' AND start_date = '2026-06-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '호이안 랜턴 페스티벌', '예술품', '음력 보름마다 전등을 끄고 등불만 밝히는 밤', '2026-08-27', '18:00', '호이안 구시가', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '호이안 랜턴 페스티벌' AND start_date = '2026-08-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '중추절', '체험', '아이들이 등을 들고 사자춤을 보는 명절', '2026-09-25', NULL, '하노이 구시가', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '중추절' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '싱가포르', '싱가포르 아트 위크', '예술품', '도시 곳곳에서 전시와 설치미술이 열리는 미술 주간', '2026-01-16', NULL, '싱가포르 전역', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '싱가포르' AND title = '싱가포르 아트 위크' AND start_date = '2026-01-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '싱가포르', '차이나타운 춘절 등불', '공연', '차이나타운을 붉은 등으로 밝히는 설 행사', '2026-02-06', '19:00', '차이나타운', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '싱가포르' AND title = '차이나타운 춘절 등불' AND start_date = '2026-02-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '싱가포르', '싱가포르 나이트 페스티벌', '예술품', '브라스 바사 일대를 빛으로 채우는 야간 축제', '2026-08-21', '19:30', '브라스 바사·부기스 일대', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '싱가포르' AND title = '싱가포르 나이트 페스티벌' AND start_date = '2026-08-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '싱가포르', 'F1 싱가포르 그랑프리', '체험', '도심 야간 서킷에서 열리는 자동차 경주와 공연', '2026-10-09', NULL, '마리나 베이 스트리트 서킷', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '싱가포르' AND title = 'F1 싱가포르 그랑프리' AND start_date = '2026-10-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '싱가포르', '디파발리', '공연', '리틀인디아를 등불로 밝히는 빛의 축제', '2026-11-08', NULL, '리틀인디아', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '싱가포르' AND title = '디파발리' AND start_date = '2026-11-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '니스 카니발', '공연', '꽃 전차와 가장행렬이 이어지는 겨울 축제', '2026-02-14', NULL, '니스 마세나 광장', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '니스 카니발' AND start_date = '2026-02-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '파리 음악 축제', '공연', '하지에 거리 곳곳에서 무료 공연이 열리는 날', '2026-06-21', '17:00', '파리 전역', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '파리 음악 축제' AND start_date = '2026-06-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '아비뇽 페스티벌', '공연', '교황청 앞마당까지 무대가 되는 연극 축제', '2026-07-04', NULL, '아비뇽 시내', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '아비뇽 페스티벌' AND start_date = '2026-07-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '인터셀틱 페스티벌', '공연', '켈트 문화권 음악가들이 모이는 여름 축제', '2026-08-07', NULL, '브르타뉴 로리앙', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '인터셀틱 페스티벌' AND start_date = '2026-08-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '유럽 문화유산의 날', '건물', '평소 닫혀 있던 관저와 궁을 개방하는 주말', '2026-09-19', NULL, '프랑스 전역', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '유럽 문화유산의 날' AND start_date = '2026-09-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '보졸레 누보', '음식', '그해 첫 포도주를 함께 여는 11월 셋째 목요일', '2026-11-19', NULL, '보졸레 일대', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '보졸레 누보' AND start_date = '2026-11-19');
