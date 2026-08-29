-- 공개 데이터로 자동 생성된 축제 시드 (Func-002-03)
--   축제 정보 : 위키데이터 (CC0)  https://query.wikidata.org
--   2026년 날짜: Nager.Date       https://date.nager.at
--   국가명 표기: mledoze/countries
-- 같은 나라 · 같은 이름 · 같은 날짜가 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.
--
-- 날짜 출처: 공휴일 대조 41건 / 위키데이터 연례 개최일 240건
-- 날짜를 어느 쪽에서도 알 수 없는 축제는 넣지 않았습니다.
-- 위키데이터에 한국어 이름이 없는 축제는 db/festival_ko.py 의 표기표로 옮겼습니다.

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '가나', '앙코스 축제', '체험', '가나에서 열리는 축제', '2026-12-24', NULL, '타코라디', 'http://commons.wikimedia.org/wiki/Special:FilePath/Takoradi%20Masquerade%20Festival%2015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '가나' AND title = '앙코스 축제' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '나이지리아', '누페 문화의 날', '체험', '나이지리아에서 열리는 축제', '2026-06-26', NULL, '코기주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '나이지리아' AND title = '누페 문화의 날' AND start_date = '2026-06-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '나이지리아', '에데아로 축제', '체험', '나이지리아에서 열리는 축제', '2026-12-29', NULL, '아남브라주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '나이지리아' AND title = '에데아로 축제' AND start_date = '2026-12-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '남아프리카', '케이프타운 민스트럴 카니발', '공연', '남아프리카에서 열리는 축제', '2026-01-02', NULL, '케이프타운', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '남아프리카' AND title = '케이프타운 민스트럴 카니발' AND start_date = '2026-01-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', '538 코닝스다흐(왕의 날)', '공연', '네덜란드에서 열리는 축제', '2026-04-27', NULL, '네덜란드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = '538 코닝스다흐(왕의 날)' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', '오란여 페스트', '체험', '네덜란드에서 열리는 축제', '2026-04-27', NULL, '네덜란드', 'http://commons.wikimedia.org/wiki/Special:FilePath/Netherlands-Australia%2002.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = '오란여 페스트' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', '해방의 날 음악축제', '공연', '네덜란드에서 열리는 축제', '2026-05-05', NULL, '하를레메르하우트', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bvf%20haarlem%2011%20hq.jpeg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = '해방의 날 음악축제' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네팔', '쿠쿠르 티하르(개의 날)', '체험', '네팔에서 열리는 축제', '2026-11-14', NULL, '네팔', 'http://commons.wikimedia.org/wiki/Special:FilePath/Dog%20in%20Kathmandu%20after%20Kukur%20Puja.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네팔' AND title = '쿠쿠르 티하르(개의 날)' AND start_date = '2026-11-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '뉴질랜드', '마타리키', '체험', '뉴질랜드에서 열리는 축제', '2026-07-10', NULL, '뉴질랜드', 'http://commons.wikimedia.org/wiki/Special:FilePath/The%20Matariki%20Stars.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '뉴질랜드' AND title = '마타리키' AND start_date = '2026-07-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '중화민국 개국기념일', '체험', '대만에서 열리는 축제', '2026-01-01', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '중화민국 개국기념일' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '타이완 등불축제', '예술품', '대만에서 열리는 축제', '2026-01-15', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Taiwan%20Lantern%20Festival%20in%202008.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '타이완 등불축제' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '평화기념일', '체험', '대만에서 열리는 축제', '2026-02-28', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '평화기념일' AND start_date = '2026-02-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '타이완 침략 반대의 날', '체험', '대만에서 열리는 축제', '2026-03-14', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '타이완 침략 반대의 날' AND start_date = '2026-03-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '단오', '체험', '음력 5월 5일의 절기', '2026-05-05', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Qing%20Dynasty%20Dragon%20Boat%20Festival.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '단오' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '국가 방재의 날', '체험', '대만에서 열리는 축제', '2026-09-21', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/09.19%20%E7%B8%BD%E7%B5%B1%E8%A6%96%E5%B0%8E%E3%80%8C114%E5%B9%B4%E5%9C%8B%E5%AE%B6%E9%98%B2%E7%81%BD%E6%97%A5%EF%BC%8D%E5%90%84%E7%B4%9A%E5%AD%B8%E6%A0%A1%E5%8F%8A%E5%B9%BC%E5%85%92%E5%9C%92%E5%9C%B0%E9%9C%87%E9%81%BF%E9%9B%A3%E6%8E%A9%E8%AD%B7%E6%BC%94%E7%B7%B4%E3%80%8D%20-%2054797657905.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '국가 방재의 날' AND start_date = '2026-09-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '쌍십절', '체험', '중화민국의 국경일', '2026-10-10', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mother%20and%20sons%20attending%20the%20celebration%20ceremony%20of%202018%20Republic%20of%20China%20National%20Day.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '쌍십절' AND start_date = '2026-10-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '독일', '비이케 불축제', '체험', '독일에서 열리는 축제', '2026-02-21', NULL, '북프리슬란트', 'http://commons.wikimedia.org/wiki/Special:FilePath/Biikebrennen%20II%202025.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '독일' AND title = '비이케 불축제' AND start_date = '2026-02-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '독일', '안나베르크 축제', '체험', '독일에서 열리는 축제', '2026-07-26', NULL, '줄츠바흐로젠베르크', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '독일' AND title = '안나베르크 축제' AND start_date = '2026-07-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '러시아', '타트 컬트 페스트', '체험', '러시아에서 열리는 축제', '2026-08-30', NULL, '카잔 크렘린', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '러시아' AND title = '타트 컬트 페스트' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '말레이시아', '카아마탄', '체험', '말레이시아에서 열리는 축제', '2026-05-30', NULL, '말레이시아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Penampang%20Sabah%20Kaamatan-Celebrations-2014-01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '말레이시아' AND title = '카아마탄' AND start_date = '2026-05-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '말레이시아', '가와이 다약', '체험', '말레이시아에서 열리는 축제', '2026-06-01', NULL, '서칼리만탄주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iban%20girls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '말레이시아' AND title = '가와이 다약' AND start_date = '2026-06-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '모로코', '이밀실 무셈', '체험', '모로코에서 열리는 축제', '2026-09-18', NULL, '이밀실', 'http://commons.wikimedia.org/wiki/Special:FilePath/Imilchil%20folklor.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '모로코' AND title = '이밀실 무셈' AND start_date = '2026-09-18');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '몽골국', '나담', '체험', '몽골국에서 열리는 축제', '2026-07-13', NULL, '몽골국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Naadam%20Festival%202024%20Opening%20Ceremony.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '몽골국' AND title = '나담' AND start_date = '2026-07-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '전국 종 축제', '체험', '미국에서 열리는 축제', '2026-01-01', NULL, '미국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '전국 종 축제' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '애니메 뉴멕시코', '체험', '미국에서 열리는 축제', '2026-03-27', NULL, '앨버커키 엠버시 스위트', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '애니메 뉴멕시코' AND start_date = '2026-03-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '해리스버그 독립기념일 행사', '공연', '미국에서 열리는 축제', '2026-07-03', NULL, '미국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hbg%20Riverfront%20Park%207-4-21.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '해리스버그 독립기념일 행사' AND start_date = '2026-07-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '커낼윈체스터 노동절 축제', '체험', '미국에서 열리는 축제', '2026-09-07', NULL, '미국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '커낼윈체스터 노동절 축제' AND start_date = '2026-09-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '노동절 카니발', '공연', '미국에서 열리는 축제', '2026-09-07', NULL, '뉴욕', 'http://commons.wikimedia.org/wiki/Special:FilePath/West%20Indian%20Day%20Parade%202008-09-01%20woman%20in%20red%20costume.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '노동절 카니발' AND start_date = '2026-09-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', '시카고 원주민의 날', '공연', '미국에서 열리는 축제', '2026-10-12', NULL, '시카고', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = '시카고 원주민의 날' AND start_date = '2026-10-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미크로네시아', '얍 데이', '체험', '미크로네시아에서 열리는 축제', '2026-03-01', NULL, '미크로네시아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미크로네시아' AND title = '얍 데이' AND start_date = '2026-03-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베냉', '부두교 축제', '체험', '베냉에서 열리는 축제', '2026-01-10', NULL, '우이다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Vodun%20Days%20-%20Ar%C3%A8ne%20de%20Ouidah.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베냉' AND title = '부두교 축제' AND start_date = '2026-01-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베네수엘라', '엘 포손 델 살라디요', '체험', '베네수엘라에서 열리는 축제', '2026-12-24', NULL, '엘살라디요', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베네수엘라' AND title = '엘 포손 델 살라디요' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', '득치똔 기념일', '체험', '베트남에서 열리는 축제', '2026-01-09', NULL, '까오다이 대성전', 'http://commons.wikimedia.org/wiki/Special:FilePath/C%C3%BAng%20%C4%91%C3%A0n%20%C4%90%E1%BA%A1i%20l%E1%BB%85%20%C4%90%E1%BB%A9c%20Ch%C3%AD%20T%C3%B4n%2C%20T%C3%A2y%20Ninh%2C%202022.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = '득치똔 기념일' AND start_date = '2026-01-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '벨기에', '발 나시오날(국민 무도회)', '공연', '벨기에에서 열리는 축제', '2026-07-20', NULL, '죄드발 광장', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '벨기에' AND title = '발 나시오날(국민 무도회)' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '벨기에', '홀롤로올', '체험', '벨기에에서 열리는 축제', '2026-11-10', NULL, '벨기에', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '벨기에' AND title = '홀롤로올' AND start_date = '2026-11-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '보스니아 헤르체고비나', '침부리야다(달걀 축제)', '체험', '보스니아 헤르체고비나에서 열리는 축제', '2026-03-21', NULL, '보스니아 헤르체고비나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '보스니아 헤르체고비나' AND title = '침부리야다(달걀 축제)' AND start_date = '2026-03-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '볼리비아', '오루로 카니발', '공연', '볼리비아에서 열리는 축제', '2026-02-16', NULL, '오루로', 'http://commons.wikimedia.org/wiki/Special:FilePath/DiabladaFerroviariadeOruroBolivia.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '볼리비아' AND title = '오루로 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '볼리비아', '냐티타스 축제', '체험', '볼리비아에서 열리는 축제', '2026-11-08', NULL, '볼리비아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '볼리비아' AND title = '냐티타스 축제' AND start_date = '2026-11-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '바이아 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '바이아주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bloco%20As%20Raparigas%20no%20Carnaval%20de%20Barreiras%20em%20%2820.02%29.%20Foto-%20Ronaldo%20Carvalho%20-%20Ag%20A%20TARDE%20%286916616465%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '바이아 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '브라질리아 카니발', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20em%20Bras%C3%ADlia%20de%20bailes%20em%20clubes%20a%20blocos%20de%20rua%20%2849560301488%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '브라질리아 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '플로리아노폴리스 카니발', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '플로리아노폴리스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Desfileuim2014.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '플로리아노폴리스 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '페르남부쿠 카니발', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '페르남부쿠 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '마라고지피 카니발', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Maragogipe%20foto%20Fernando%20Barbosa%20%284%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '마라고지피 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '코룸바 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Corumb%C3%A1%202017%20-%20Mocidade%20Nova%20Corumb%C3%A1%20-%20Carnaval%20Float.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '코룸바 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '치바지 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Tibagi.5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '치바지 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '바이샤다산치스타 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Brasil%20-%20Carnaval%202013.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '바이샤다산치스타 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '올린다 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '올린다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Passistas%20%283219530631%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '올린다 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '상파울루 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '상파울루', 'http://commons.wikimedia.org/wiki/Special:FilePath/Camisa%20Verde%20e%20Branco%20%282010%29.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '상파울루 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '플로리아노폴리스 게이 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '플로리아노폴리스 게이 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '리우 뮤직 카니발', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '리우 뮤직 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '리우 카니발', '공연', '브라질의 리우데자네이루에서 열리는 삼바 축제', '2026-02-16', NULL, '리우데자네이루', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnival%20in%20Rio%20de%20Janeiro.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '리우 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '치라덴치스 영화제', '공연', '브라질에서 열리는 축제', '2026-04-21', NULL, '치라덴치스', 'http://commons.wikimedia.org/wiki/Special:FilePath/16%C2%AA%20Mostra%20de%20Cinema%20de%20Tiradentes%20%288395191148%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '치라덴치스 영화제' AND start_date = '2026-04-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '상투메 프린시페', '아우투 드 플로리페스', '공연', '상투메 프린시페에서 열리는 축제', '2026-08-10', NULL, '프린시페섬', 'http://commons.wikimedia.org/wiki/Special:FilePath/Principe%20Island%201357.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '상투메 프린시페' AND title = '아우투 드 플로리페스' AND start_date = '2026-08-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '세르비아', '어린이 예술 표현 비엔날레', '예술품', '세르비아에서 열리는 축제', '2026-05-15', NULL, '판체보', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '세르비아' AND title = '어린이 예술 표현 비엔날레' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '수리남', '스레피덴시 데이(독립기념일)', '체험', '수리남에서 열리는 축제', '2026-11-25', NULL, '수리남', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '수리남' AND title = '스레피덴시 데이(독립기념일)' AND start_date = '2026-11-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '알가이다 코시에르스 춤 축제', '체험', '스페인에서 열리는 축제', '2026-01-16', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ball%20dels%20cossiers%20d%27Algaida%202014-07-24%2018-31.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '알가이다 코시에르스 춤 축제' AND start_date = '2026-01-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '아세우체 라스 카란토냐스', '체험', '스페인에서 열리는 축제', '2026-01-20', NULL, '아세우체', 'http://commons.wikimedia.org/wiki/Special:FilePath/Las%20Caranto%C3%B1as%20de%20Aceh%C3%BAche%20en%20el%20VIII%20Festival%20de%20la%20M%C3%A1scara%20de%20Zamora%20%2830083593165%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '아세우체 라스 카란토냐스' AND start_date = '2026-01-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '탐보라다', '체험', '스페인에서 열리는 축제', '2026-01-20', NULL, '산세바스티안', 'http://commons.wikimedia.org/wiki/Special:FilePath/San%20Sebastian%20Tamborrada%20Infantil.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '탐보라다' AND start_date = '2026-01-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '칸델라 성모 10년 축제', '체험', '스페인에서 열리는 축제', '2026-02-02', NULL, '발스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Decenals%20de%20Valls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '칸델라 성모 10년 축제' AND start_date = '2026-02-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '아테카 산블라스 축제', '체험', '스페인에서 열리는 축제', '2026-02-03', NULL, '아테카', 'http://commons.wikimedia.org/wiki/Special:FilePath/1926-02-11%2C%20La%20Voz%20de%20Arag%C3%B3n%2C%20Las%20fiestas%20de%20San%20Blas%20en%20Ateca.%E2%80%94La%20tradicional%20fiesta%20de%20la%20m%C3%A1scara%2C%20o%20el%20terror%20de%20los%20chiquillos%2C%20Rodero%20%28cropped%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '아테카 산블라스 축제' AND start_date = '2026-02-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '세르베라 콤플레테스', '체험', '스페인에서 열리는 축제', '2026-02-05', NULL, '세르베라 산타마리아 대성당', 'http://commons.wikimedia.org/wiki/Special:FilePath/Les%20Completes%20de%20Cervera%202021.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '세르베라 콤플레테스' AND start_date = '2026-02-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '만레사 빛 축제', '체험', '스페인에서 열리는 축제', '2026-02-21', NULL, '만레사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '만레사 빛 축제' AND start_date = '2026-02-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '알코이 무어인과 기독교인 축제', '체험', '스페인에서 열리는 축제', '2026-04-21', NULL, '알코이', 'http://commons.wikimedia.org/wiki/Special:FilePath/Detalle%20alardo%20tarde%202007.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '알코이 무어인과 기독교인 축제' AND start_date = '2026-04-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '4월 25일 기념일', '체험', '스페인에서 열리는 축제', '2026-04-25', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Val%C3%A8ncia%2016-04-2011.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '4월 25일 기념일' AND start_date = '2026-04-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산프루덴시오 축제', '체험', '스페인에서 열리는 축제', '2026-04-27', NULL, '비토리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Campas%20de%20Armentia%20el%20d%C3%ADa%20de%20San%20Prudencio%20%2826455091780%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산프루덴시오 축제' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산이시드로 축일', '체험', '스페인에서 열리는 축제', '2026-05-15', NULL, '마드리드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산이시드로 축일' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '성체축일 꽃카펫 축제', '체험', '스페인에서 열리는 축제', '2026-06-04', NULL, '라오로타바', 'http://commons.wikimedia.org/wiki/Special:FilePath/Orotava%20Corpus2008%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '성체축일 꽃카펫 축제' AND start_date = '2026-06-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '라포블라데세구르 팔레스 축제', '체험', '스페인에서 열리는 축제', '2026-06-17', NULL, '라포블라데세구르', 'http://commons.wikimedia.org/wiki/Special:FilePath/Desfilada%20de%20falles%20de%20La%20Pobla%20de%20Segur%20l%27any%202014.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '라포블라데세구르 팔레스 축제' AND start_date = '2026-06-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '알리칸테 산후안 불축제', '체험', '스페인에서 열리는 축제', '2026-06-23', NULL, '알리칸테', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hogueras%202008%20-%20Gran%20Via%20La%20Ceramica%202.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '알리칸테 산후안 불축제' AND start_date = '2026-06-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '시우타데야 산주안 축제', '체험', '스페인에서 열리는 축제', '2026-06-24', NULL, '시우타데야데메노르카', 'http://commons.wikimedia.org/wiki/Special:FilePath/SantJoan%287%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '시우타데야 산주안 축제' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산주안 펠로스', '체험', '스페인에서 열리는 축제', '2026-06-24', NULL, '펠라니치', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sant%20Joan%20Pel%C3%B3s%20ballant.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산주안 펠로스' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산주안 데 알라칸트 영화제', '공연', '스페인에서 열리는 축제', '2026-06-24', NULL, '산주안 데 알라칸트', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산주안 데 알라칸트 영화제' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '폴렌사 코시에르스 춤 축제', '체험', '스페인에서 열리는 축제', '2026-06-29', NULL, '폴렌사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '폴렌사 코시에르스 춤 축제' AND start_date = '2026-06-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산 페르민 축제', '체험', '스페인에서 열리는 축제', '2026-07-07', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Pamplona%20Sanfermines%20Calle%20San%20Nicolas.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산 페르민 축제' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '성 야고보 축일 축제', '체험', '스페인에서 열리는 축제', '2026-07-25', NULL, '산티아고데콤포스텔라', 'http://commons.wikimedia.org/wiki/Special:FilePath/Santiago.%20Fuegos%20del%20Ap%C3%B3stol%202012%20%287640755270%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '성 야고보 축일 축제' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '레스산테스 축제', '체험', '스페인에서 열리는 축제', '2026-07-27', NULL, '마타로', 'http://commons.wikimedia.org/wiki/Special:FilePath/FB-7zK-SgpZ87VIHVp7x2w-1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '레스산테스 축제' AND start_date = '2026-07-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '빌라노바 이 라 젤트루 상상 행렬', '체험', '스페인에서 열리는 축제', '2026-08-06', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/L%27arribada%20del%20ferrocarril%20%283796752350%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '빌라노바 이 라 젤트루 상상 행렬' AND start_date = '2026-08-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '니트 데 랄바(새벽의 밤)', '체험', '스페인에서 열리는 축제', '2026-08-13', NULL, '엘체', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nit%20de%20L%27Alb%C3%A0%20Elche.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '니트 데 랄바(새벽의 밤)' AND start_date = '2026-08-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '그라시아 축제', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, '그라시아 지구', 'http://commons.wikimedia.org/wiki/Special:FilePath/Festa%20Major%20de%20Gr%C3%A0cia%202026%20-%20Pla%C3%A7a%20Rovira%20i%20Trias%20-%2002.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '그라시아 축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '레케나 성모와 산로케 축제', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, '레케나', 'http://commons.wikimedia.org/wiki/Special:FilePath/%22Imatge%20de%20Sant%20Roc%20de%20la%20Majordomia%20del%20Sant%20a%20Requena.%20Amb%20la%20seua%20banda%2C%20que%20porta%20una%20inscripci%C3%B3%20brodada%20que%20diu%20Viva%20San%20Roque%20y%20sus%20devotos.%20A%C3%B1o%201865.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '레케나 성모와 산로케 축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '몬투이리 코시에르스 춤 축제', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, '몬투이리', 'http://commons.wikimedia.org/wiki/Special:FilePath/Cossiersmont1.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '몬투이리 코시에르스 춤 축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '빌라프랑카 델 페네데스 축제', '체험', '스페인에서 열리는 축제', '2026-08-30', NULL, '빌라프랑카 델 페네데스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Entrada%20St%20Felix.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '빌라프랑카 델 페네데스 축제' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '필록세라 축제', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Fil%C2%B7losereta%20a%20la%20Festa%20de%20la%20Fil%C2%B7loxera.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '필록세라 축제' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '투라 축제', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, '올로트', 'http://commons.wikimedia.org/wiki/Special:FilePath/Els%20Caballets%20of%20Olot%202008.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '투라 축제' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '알제메시 살루드 성모 축제', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Algemes%C3%AD%20MDS%20-%20Les%20Llauradores%2004.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '알제메시 살루드 성모 축제' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '몬트바우 수호성인 축제', '체험', '스페인에서 열리는 축제', '2026-09-30', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Som%20Montbau%202021.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '몬트바우 수호성인 축제' AND start_date = '2026-09-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '알칼라 데 에나레스 세르반테스 주간', '체험', '스페인에서 열리는 축제', '2026-10-09', NULL, '알칼라 데 에나레스 축제장', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '알칼라 데 에나레스 세르반테스 주간' AND start_date = '2026-10-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '엘 데사르메', '음식', '스페인에서 열리는 축제', '2026-10-19', NULL, '스페인', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '엘 데사르메' AND start_date = '2026-10-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '피아파이아 불축제', '체험', '스페인에서 열리는 축제', '2026-12-24', NULL, '바가', 'http://commons.wikimedia.org/wiki/Special:FilePath/La%20Fia-faia%20Baga%20Sant%20Julia.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '피아파이아 불축제' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '니트 델스 파초스(횃불의 밤)', '체험', '스페인에서 열리는 축제', '2026-12-24', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Concurso%20Fatxo%20gran.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '니트 델스 파초스(횃불의 밤)' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '센테예스 소나무 축제', '체험', '스페인에서 열리는 축제', '2026-12-30', NULL, '센테예스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Festadelpi%20penjant.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '센테예스 소나무 축제' AND start_date = '2026-12-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '슬로베니아', '살라미야다(살라미 축제)', '음식', '슬로베니아에서 열리는 축제', '2026-03-10', NULL, '슬로베니아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '슬로베니아' AND title = '살라미야다(살라미 축제)' AND start_date = '2026-03-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아르헨티나', '부에노스아이레스 카니발', '공연', '아르헨티나에서 열리는 축제', '2026-02-16', NULL, '아르헨티나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아르헨티나' AND title = '부에노스아이레스 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아르헨티나', '전국 크리스마스 축제', '체험', '아르헨티나에서 열리는 축제', '2026-12-25', NULL, '아르헨티나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아르헨티나' AND title = '전국 크리스마스 축제' AND start_date = '2026-12-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '툴러메인 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-01-21', NULL, '메이언 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '툴러메인 패턴 축제' AND start_date = '2026-01-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '울라드 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-02-08', NULL, '울라드 교회', 'http://commons.wikimedia.org/wiki/Special:FilePath/St%20Fiacre%27s%20pattern%20crosses%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '울라드 패턴 축제' AND start_date = '2026-02-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '툴러헤린 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-03-05', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '툴러헤린 패턴 축제' AND start_date = '2026-03-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '콜럼킬 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-06-09', NULL, '성 콜럼킬 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '콜럼킬 패턴 축제' AND start_date = '2026-06-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '콜럼실 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-06-09', NULL, '성 콜럼킬 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '콜럼실 패턴 축제' AND start_date = '2026-06-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '킬마나 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '킬마나 패턴 축제' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '킬라루 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, '토버몰루아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '킬라루 패턴 축제' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '토버라니던 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, '토버나던', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '토버라니던 패턴 축제' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '성 리오크 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-01', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '성 리오크 패턴 축제' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '오닝 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-05', NULL, '터버나머크티', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '오닝 패턴 축제' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '뉴마켓 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-12', NULL, '성 브렌던 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '뉴마켓 패턴 축제' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '브랩스타운 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-15', NULL, '성 마거릿 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '브랩스타운 패턴 축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '킬모가니 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-25', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '킬모가니 패턴 축제' AND start_date = '2026-08-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '시스타운 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-08-30', NULL, '성 피어크라 우물', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bishop%20Coll%20et%20al.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '시스타운 패턴 축제' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '킬라히 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-09-14', NULL, '터버나크루크니', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '킬라히 패턴 축제' AND start_date = '2026-09-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', '성 레너드 우물 패턴 축제', '체험', '아일랜드에서 열리는 축제', '2026-11-06', NULL, '성 레너드 우물', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = '성 레너드 우물 패턴 축제' AND start_date = '2026-11-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '알바니아', '여름의 날', '체험', '알바니아에서 열리는 축제', '2026-03-16', NULL, '알바니아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '알바니아' AND title = '여름의 날' AND start_date = '2026-03-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '앙골라', '루안다 카니발', '공연', '앙골라에서 열리는 축제', '2026-02-16', NULL, '루안다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Luanda%20Skyline%20-%20Angola%202015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '앙골라' AND title = '루안다 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '에콰도르', '피야로 디아블라다', '체험', '에콰도르에서 열리는 축제', '2026-01-01', NULL, '에콰도르', 'http://commons.wikimedia.org/wiki/Special:FilePath/Devil%20at%20the%20Diablada%20de%20Pillaro.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '에콰도르' AND title = '피야로 디아블라다' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '에콰도르', '알수르델시엘로 축제', '체험', '에콰도르에서 열리는 축제', '2026-12-31', NULL, '키토', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '에콰도르' AND title = '알수르델시엘로 축제' AND start_date = '2026-12-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', '앨런타이드', '체험', '영국에서 열리는 축제', '2026-10-31', NULL, '콘월주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = '앨런타이드' AND start_date = '2026-10-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', '루이스 본파이어 축제', '체험', '영국에서 열리는 축제', '2026-11-05', NULL, '루이스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Lewes%20Bonfire%2C%20Martyrs%20Crosses%2002%20detail.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = '루이스 본파이어 축제' AND start_date = '2026-11-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', '몬톨 축제', '체험', '영국에서 열리는 축제', '2026-12-21', NULL, '영국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = '몬톨 축제' AND start_date = '2026-12-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '오스트리아', '루페르티키르탁 축제', '체험', '오스트리아에서 열리는 축제', '2026-09-24', NULL, '오스트리아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '오스트리아' AND title = '루페르티키르탁 축제' AND start_date = '2026-09-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '우크라이나', '마흐노와 함께하는 독립기념일', '체험', '우크라이나에서 열리는 축제', '2026-08-24', NULL, '훌리아이폴', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '우크라이나' AND title = '마흐노와 함께하는 독립기념일' AND start_date = '2026-08-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이란', '얄다', '체험', '이란에서 열리는 축제', '2026-12-20', NULL, '캐나다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sarpol-e%20Zahab%20Yalda%20Night%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이란' AND title = '얄다' AND start_date = '2026-12-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', '팔리오 델 니발로', '체험', '이탈리아에서 열리는 축제', '2026-01-05', NULL, '이탈리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Palio%20del%20Niballo%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = '팔리오 델 니발로' AND start_date = '2026-01-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', '밀 축제', '체험', '이탈리아에서 열리는 축제', '2026-08-16', NULL, '이탈리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Foglianise%20-%20Festa%20del%20Grano%202009%20-%2015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = '밀 축제' AND start_date = '2026-08-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', '역사 행렬 축제', '체험', '이탈리아에서 열리는 축제', '2026-09-08', NULL, '이탈리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Fuochi%20artificiali%20in%20piazza%20Duomo%20in%20occasione%20della%20Madonna%20della%20Fiera.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = '역사 행렬 축제' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', '국가 과학의 날', '체험', '인도에서 열리는 축제', '2026-02-28', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = '국가 과학의 날' AND start_date = '2026-02-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', '세계 파칼라의 날', '체험', '인도에서 열리는 축제', '2026-03-20', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = '세계 파칼라의 날' AND start_date = '2026-03-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', '아비루치 스포츠 데이', '체험', '인도에서 열리는 축제', '2026-09-03', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = '아비루치 스포츠 데이' AND start_date = '2026-09-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도네시아', '가와이 다약', '체험', '인도네시아에서 열리는 축제', '2026-06-01', NULL, '서칼리만탄주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iban%20girls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도네시아' AND title = '가와이 다약' AND start_date = '2026-06-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '나노리·시메나와키리·불축제', '체험', '일본에서 열리는 축제', '2026-01-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '나노리·시메나와키리·불축제' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '도카 에비스', '체험', '일본에서 열리는 축제', '2026-01-09', NULL, '니시노미야 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Horikawaebisu-jinja%20Osaka%20Japan04-r.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '도카 에비스' AND start_date = '2026-01-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '무코나게·스미누리', '체험', '일본에서 열리는 축제', '2026-01-15', NULL, '마쓰노야마 온천', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mukonage%20Matsunoyama%202026.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '무코나게·스미누리' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '고쇼가쓰(소정월)', '체험', '일본에서 열리는 축제', '2026-01-15', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '고쇼가쓰(소정월)' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아타고샤 불축제', '체험', '일본에서 열리는 축제', '2026-01-26', NULL, '우오즈시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아타고샤 불축제' AND start_date = '2026-01-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '세쓰분', '체험', '일본에서 열리는 축제', '2026-02-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Setsubun.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '세쓰분' AND start_date = '2026-02-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오토 마쓰리', '체험', '일본에서 열리는 축제', '2026-02-06', NULL, '가미쿠라 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/518wakayama-Kumano%20Oto%20Festival-xl.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오토 마쓰리' AND start_date = '2026-02-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가세도리', '체험', '일본에서 열리는 축제', '2026-02-11', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kasedori%202026%20Kaminoyama%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가세도리' AND start_date = '2026-02-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '게로 다노카미 축제', '체험', '일본에서 열리는 축제', '2026-02-14', NULL, '모리 미나시 하치만 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gero%20no%20Ta%20no%20Kami%20Festival%2C%20Marching%20people.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '게로 다노카미 축제' AND start_date = '2026-02-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히부리 가마쿠라', '체험', '일본에서 열리는 축제', '2026-02-14', NULL, '가쿠노다테', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hiburi%20Kamakura%20in%20Kakunodate%202019b.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히부리 가마쿠라' AND start_date = '2026-02-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히나마쓰리', '체험', '소녀의 성장을 축하하는 일본의 전통 축제', '2026-03-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ist%20di%20Cultura%20giapponese%20-%20altare%20della%20festa%20delle%20bambole%20P1100919.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히나마쓰리' AND start_date = '2026-03-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오미즈토리', '체험', '일본에서 열리는 축제', '2026-03-12', NULL, '니가쓰도', 'http://commons.wikimedia.org/wiki/Special:FilePath/Omizutori.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오미즈토리' AND start_date = '2026-03-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가스가사이', '체험', '일본에서 열리는 축제', '2026-03-13', NULL, '가스가타이샤', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가스가사이' AND start_date = '2026-03-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '호넨 마쓰리', '체험', '일본에서 열리는 축제', '2026-03-15', NULL, '다가타 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/H%C5%8Dnen%20Matsuri%202.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '호넨 마쓰리' AND start_date = '2026-03-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '진무 천황제', '체험', '일본에서 열리는 축제', '2026-04-03', NULL, '궁중삼전', 'http://commons.wikimedia.org/wiki/Special:FilePath/Jinmusai-fes1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '진무 천황제' AND start_date = '2026-04-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지치부 신사 오타우에사이', '체험', '일본에서 열리는 축제', '2026-04-04', NULL, '지치부 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지치부 신사 오타우에사이' AND start_date = '2026-04-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하나마쓰리(꽃축제)', '체험', '일본에서 열리는 축제', '2026-04-08', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/A%20birthday%20of%20Buddha%2Chanamatsuri%2Ckanpukuji-temple%2Ckatori-city%2Cjapan.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하나마쓰리(꽃축제)' AND start_date = '2026-04-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히요시 산노사이', '체험', '일본에서 열리는 축제', '2026-04-14', NULL, '히요시타이샤', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히요시 산노사이' AND start_date = '2026-04-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구마노 혼구 대사 예대제', '체험', '일본에서 열리는 축제', '2026-04-15', NULL, '구마노 혼구 대사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구마노 혼구 대사 예대제' AND start_date = '2026-04-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다카야마 마쓰리', '체험', '일본에서 열리는 축제', '2026-04-15', NULL, '히에 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%A4%A7%E5%9B%BD%E5%8F%B0%20%28%E5%B2%90%E9%98%9C%E7%9C%8C%E9%AB%98%E5%B1%B1%E5%B8%82%29%20-%20panoramio%20%282%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다카야마 마쓰리' AND start_date = '2026-04-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '후루카와 마쓰리', '체험', '일본에서 열리는 축제', '2026-04-20', NULL, '게타 와카미야 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Furukawa-yatai.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '후루카와 마쓰리' AND start_date = '2026-04-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다카오카 미쿠루마야마 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-01', NULL, '다카오카 세키노 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%AB%98%E5%B2%A1%E5%B8%82%E8%A1%97%E3%81%AE%E9%A2%A8%E6%99%AF%20-%20panoramio.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다카오카 미쿠루마야마 마쓰리' AND start_date = '2026-05-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지류 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-02', NULL, '지류 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tiryuumatsuri7.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지류 마쓰리' AND start_date = '2026-05-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하카타 마쓰바야시', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Matsubayashi05.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하카타 마쓰바야시' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히로시마 플라워 페스티벌', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hana-no-to2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히로시마 플라워 페스티벌' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가메자키 시오히 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '가미사키 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kamezakishiohi%20Festival2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가메자키 시오히 마쓰리' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '마루가메성 축제', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '마루가메성', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '마루가메성 축제' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오다와라 호조 5대 마쓰리', '공연', '일본에서 열리는 축제', '2026-05-03', NULL, '오다와라시', 'http://commons.wikimedia.org/wiki/Special:FilePath/HJ5SH1.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오다와라 호조 5대 마쓰리' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하카타 돈타쿠', '체험', '일본 후쿠오카의 연례 행사', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hakata%20Dontaku%2078338697%20org.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하카타 돈타쿠' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다도 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-04', NULL, '다도 대사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tado%20Festival%202.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다도 마쓰리' AND start_date = '2026-05-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '조하나 히키야마 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%9F%8E%E7%AB%AF%E7%94%BA%20%E6%9B%B3%E5%B1%B1%E7%A5%AD%E3%82%8A%20SLKY20180505%200000057.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '조하나 히키야마 마쓰리' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '고쿠후사이', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '고쿠후사이' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구라야미 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '오쿠니타마 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Okunitama-jinja-24.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구라야미 마쓰리' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '스이텐구 봄 대제', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '구루메 스이텐구', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '스이텐구 봄 대제' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '일본의 단오', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Flying%20Koi%20by%20tiseb%20in%20Nagasaki.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '일본의 단오' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미야즈 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-13', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ukidaiko.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미야즈 마쓰리' AND start_date = '2026-05-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오가키 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-15', NULL, '오가키 하치만 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%A4%A7%E5%9E%A3%E5%B8%82%28%E5%A4%A7%E5%9E%A3%E3%81%BE%E3%81%A4%E3%82%8A%29%20-%20panoramio.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오가키 마쓰리' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아오이마쓰리', '체험', '일본에서 열리는 축제', '2026-05-15', NULL, '가모 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Aoi%20Matsuri.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아오이마쓰리' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미쿠니 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-19', NULL, '미쿠니 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mikuni%20festival%202022.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미쿠니 마쓰리' AND start_date = '2026-05-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '산노 마쓰리', '체험', '일본에서 열리는 축제', '2026-05-31', NULL, '히에 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '산노 마쓰리' AND start_date = '2026-05-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아가타 마쓰리', '체험', '일본에서 열리는 축제', '2026-06-05', NULL, '아가타 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bonten%20togyo.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아가타 마쓰리' AND start_date = '2026-06-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오타우에 신지(모내기 의식)', '체험', '일본에서 열리는 축제', '2026-06-14', NULL, '온다, 스미요시 대사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sumiyoshi%20jinja%20Otaue.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오타우에 신지(모내기 의식)' AND start_date = '2026-06-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '간겐사이', '체험', '일본에서 열리는 축제', '2026-06-17', NULL, '히로시마만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kangen%20jigozen2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '간겐사이' AND start_date = '2026-06-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히메지 유카타 마쓰리', '체험', '일본에서 열리는 축제', '2026-06-22', NULL, '오사카베 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Himeji%20Yukata%20Matsuri%202009p1%20003.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히메지 유카타 마쓰리' AND start_date = '2026-06-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '시마다치 하다카 마쓰리', '체험', '일본에서 열리는 축제', '2026-06-30', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '시마다치 하다카 마쓰리' AND start_date = '2026-06-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아이젠마쓰리', '체험', '일본 오사카부 오사카시 덴노지구의 아이젠도 쇼만인에서 열리는 축제', '2026-07-01', NULL, '쇼만인', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아이젠마쓰리' AND start_date = '2026-07-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '한게쇼(반하생)', '체험', '일본에서 열리는 축제', '2026-07-02', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Saururus%20chinensis%20kz01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '한게쇼(반하생)' AND start_date = '2026-07-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다나바타', '체험', '위키미디어 분류', '2026-07-07', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E4%B8%83%E5%A4%95%20%2819545533256%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다나바타' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '칠석', '체험', '칠석(한자: 七夕)은 중국 사대 민간전설의 견우와 직녀 전설에서 견우와 직녀가 1년에 한 번 만나는 날로, 칠석날로도 불린다. 한국과 중국에서는 음력 7월 7일이지만, 일본은 양력 7월 7일이다.', '2026-07-07', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Niulang%20and%20Zhinv%20%28Long%20Corridor%29.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '칠석' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '이쿠타마 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-12', NULL, '이쿠쿠니타마 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '이쿠타마 마쓰리' AND start_date = '2026-07-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히라노고 여름 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-13', NULL, '히라노', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히라노고 여름 마쓰리' AND start_date = '2026-07-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오기 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-14', NULL, '구마노 나치 대사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오기 마쓰리' AND start_date = '2026-07-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지치부 가와세 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-20', NULL, '지치부 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Chichibu%20Kawase%20Matsuri.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지치부 가와세 마쓰리' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '진다이지 꽈리 축제', '체험', '일본에서 열리는 축제', '2026-07-20', NULL, '진다이지', 'http://commons.wikimedia.org/wiki/Special:FilePath/JindaijiMonzen.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '진다이지 꽈리 축제' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '이이다마치 도로야마 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-21', NULL, '가스가 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%A3%AF%E7%94%B0%E7%94%BA%E7%87%88%E7%B1%A0%E5%B1%B1%E7%A5%AD%E3%82%8A%EF%BC%88%E3%81%84%E3%81%84%E3%81%A0%E3%81%BE%E3%81%A1%E3%81%A8%E3%82%8D%E3%82%84%E3%81%BE%E3%81%BE%E3%81%A4%E3%82%8A%EF%BC%89.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '이이다마치 도로야마 마쓰리' AND start_date = '2026-07-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '쓰치자키 신메이샤 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-21', NULL, '쓰치자키 신메이샤', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tsuchi-yama-tsunafuru.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '쓰치자키 신메이샤 마쓰리' AND start_date = '2026-07-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지조본', '체험', '일본에서 열리는 축제', '2026-07-24', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지조본' AND start_date = '2026-07-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '교토 기온마쓰리 야마보코 순행', '공연', '일본에서 열리는 축제', '2026-07-24', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gion%20Matsuri%202017-5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '교토 기온마쓰리 야마보코 순행' AND start_date = '2026-07-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '마시코 기온마쓰리', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '마시코 기온마쓰리' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다나베 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, '도케이 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E7%94%B0%E8%BE%BA%E7%A5%AD%28%E9%AC%AA%E9%9B%9E%E7%A5%9E%E7%A4%BE%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다나베 마쓰리' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '덴진마쓰리', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, '오사카 덴만구', 'http://commons.wikimedia.org/wiki/Special:FilePath/120725%20Osaka%20Tenjinmatsuri%20Japan08bs.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '덴진마쓰리' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '기부네 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-28', NULL, '마나즈루정', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E8%A5%BF%E5%B0%8F%E6%97%A9%E8%88%B9%E3%83%BB%E8%B2%B4%E5%AE%AE%E4%B8%B8%20%E6%B5%B7%E4%B8%8A%E6%B8%A1%E5%BE%A1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '기부네 마쓰리' AND start_date = '2026-07-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '홋카이도 배꼽 축제', '체험', '일본에서 열리는 축제', '2026-07-29', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '홋카이도 배꼽 축제' AND start_date = '2026-07-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '나메리카와 네부타 나가시', '체험', '일본에서 열리는 축제', '2026-07-31', NULL, '나메리카와시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '나메리카와 네부타 나가시' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '스미요시 마쓰리', '체험', '일본에서 열리는 축제', '2026-07-31', NULL, '스미요시타이샤', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sumiyoshi%20Matsuri%20%2804%29%20IMG%203224-2%2020140801.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '스미요시 마쓰리' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '니시니혼 오호리 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-01', NULL, '오호리 공원', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nishi-Nippon%20Ohori%20Fireworks%20Festival%202009.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '니시니혼 오호리 불꽃축제' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오키요 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오키요 마쓰리' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'PL 불꽃예술 축제', '예술품', '일본에서 열리는 축제', '2026-08-01', NULL, '돈다바야시시', 'http://commons.wikimedia.org/wiki/Special:FilePath/PL%20Fireworks2010-5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'PL 불꽃예술 축제' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하기 여름축제', '체험', '일본에서 열리는 축제', '2026-08-02', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하기 여름축제' AND start_date = '2026-08-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '기비쓰히코 신사 모내기 축제', '체험', '일본에서 열리는 축제', '2026-08-02', NULL, '기비쓰히코 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '기비쓰히코 신사 모내기 축제' AND start_date = '2026-08-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '모리오카 산사오도리', '체험', '일본에서 열리는 축제', '2026-08-04', NULL, '센트럴 애비뉴', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sansa%20Odori%202.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '모리오카 산사오도리' AND start_date = '2026-08-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지쿠고강 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-05', NULL, '지쿠고강', 'http://commons.wikimedia.org/wiki/Special:FilePath/ColorfulFireworks.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지쿠고강 불꽃축제' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '센다이 다나바타 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-05', NULL, '니시 공원', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sendai%20Tanabata%20Fireworks%20Festival%202009.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '센다이 다나바타 불꽃축제' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아키타 간토 축제', '체험', '일본 아키타현 아키타시에서 열리는 축제', '2026-08-05', NULL, '아키타시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Akita%20Kanto%20Festival%202017.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아키타 간토 축제' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아오모리 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-07', NULL, '아오모리항', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아오모리 불꽃축제' AND start_date = '2026-08-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '나루토 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-07', NULL, '나루토 문화회관', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '나루토 불꽃축제' AND start_date = '2026-08-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '센다이 다나바타 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-08', NULL, '센다이시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sendai%20Tanabata%202023.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '센다이 다나바타 마쓰리' AND start_date = '2026-08-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '안진 마쓰리 해상 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-10', NULL, '이토시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '안진 마쓰리 해상 불꽃축제' AND start_date = '2026-08-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미쿠니 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-11', NULL, '미쿠니 선셋 비치', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mikuni%20fireworks%202013.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미쿠니 불꽃축제' AND start_date = '2026-08-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '우바가미 대신궁 도교사이', '체험', '일본에서 열리는 축제', '2026-08-11', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E7%A5%9E%E8%BC%BF%E6%B8%A1%E5%BE%A11.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '우바가미 대신궁 도교사이' AND start_date = '2026-08-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '요사코이 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-12', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Yosakoi%20Performers%20at%20Kochi%20Yosakoi%20Matsuri%202005%2065.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '요사코이 마쓰리' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가치마이 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-13', NULL, '오비히로시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가치마이 불꽃축제' AND start_date = '2026-08-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '사누키 다카마쓰 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-14', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Japan%20-%20Takamatsu%20Awa%20Odori%20Bon%20Festival%2003.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '사누키 다카마쓰 마쓰리' AND start_date = '2026-08-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '후카가와 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, '도미오카 하치만 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tomioka%20hachimangu4.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '후카가와 마쓰리' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구지라부네 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, '도리데 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kujirabune01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구지라부네 마쓰리' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '스와호 불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-15', NULL, '스와호', 'http://commons.wikimedia.org/wiki/Special:FilePath/Suwa-ko%20firework%2020080815%2002.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '스와호 불꽃축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '난부 불축제', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, '난부정', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '난부 불축제' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '도미다 이시도리 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, '도미다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tomida%20ishidori.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '도미다 이시도리 마쓰리' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '고잔노오쿠리비(다이몬지 送り火)', '체험', '일본에서 열리는 축제', '2026-08-16', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gozanokuribi%20Daimonji2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '고잔노오쿠리비(다이몬지 送り火)' AND start_date = '2026-08-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구마노 대불꽃축제', '건물', '일본에서 열리는 축제', '2026-08-17', NULL, '시치리미 해변', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구마노 대불꽃축제' AND start_date = '2026-08-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하나와바야시', '체험', '일본에서 열리는 축제', '2026-08-20', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hanawabayashi%202012.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하나와바야시' AND start_date = '2026-08-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '욧카부이', '체험', '일본에서 열리는 축제', '2026-08-22', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '욧카부이' AND start_date = '2026-08-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '시노미야 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-23', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '시노미야 마쓰리' AND start_date = '2026-08-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '신조 마쓰리', '체험', '일본에서 열리는 축제', '2026-08-25', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/ShinjoMatsuriNight.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '신조 마쓰리' AND start_date = '2026-08-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '요시다 불축제', '체험', '일본에서 열리는 축제', '2026-08-26', NULL, '기타구치 혼구 후지센겐 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Torches%20burning%20Yoshida%20Fire%20Festival%20A.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '요시다 불축제' AND start_date = '2026-08-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '니햐쿠토카', '체험', '일본에서 열리는 축제', '2026-09-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '니햐쿠토카' AND start_date = '2026-09-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가제노본', '체험', '일본에서 열리는 축제', '2026-09-02', NULL, '도야마시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kazenobon01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가제노본' AND start_date = '2026-09-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '쓰루가 마쓰리', '체험', '일본에서 열리는 축제', '2026-09-04', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '쓰루가 마쓰리' AND start_date = '2026-09-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가쿠노다테 마쓰리', '체험', '일본에서 열리는 축제', '2026-09-08', NULL, '가쿠노다테', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kakunodate%20maturi%202008a.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가쿠노다테 마쓰리' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '니햐쿠하쓰카', '체험', '일본에서 열리는 축제', '2026-09-11', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '니햐쿠하쓰카' AND start_date = '2026-09-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '이와시미즈사이', '체험', '일본에서 열리는 축제', '2026-09-15', NULL, '이와시미즈 하치만궁', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '이와시미즈사이' AND start_date = '2026-09-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오쿠마 가부토 마쓰리', '체험', '일본에서 열리는 축제', '2026-09-20', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오쿠마 가부토 마쓰리' AND start_date = '2026-09-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '쓰쿠리몬 마쓰리', '체험', '일본에서 열리는 축제', '2026-09-23', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/National%20Museum%20of%20Ethnology%2C%20Osaka%20-%20Ranry%C3%B4-%C3%B4%20statue%20made%20of%20vegetables%20-%20Festival%20%22Tsukurimon-matsuri%22%20-%20Takaoka%2C%20Toyama%20pref.%20-%20Collected%20in%202012.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '쓰쿠리몬 마쓰리' AND start_date = '2026-09-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '사다 신노(사다 신사 신악)', '체험', '일본에서 열리는 축제', '2026-09-25', NULL, '사다 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '사다 신노(사다 신사 신악)' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미아레 마쓰리', '체험', '일본에서 열리는 축제', '2026-10-01', NULL, '겐카이나다', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%AE%97%E5%83%8F%E5%A4%A7%E7%A4%BE%E3%81%BF%E3%81%82%E3%82%8C%E7%A5%AD.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미아레 마쓰리' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '야하기 신사 가을 대제', '체험', '일본에서 열리는 축제', '2026-10-01', NULL, '야하기 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Yahagi-Jinja-1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '야하기 신사 가을 대제' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '사나 신사 축제', '체험', '일본에서 열리는 축제', '2026-10-08', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sana%20Shrine.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '사나 신사 축제' AND start_date = '2026-10-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '나가사키 군치', '체험', '일본에서 열리는 축제', '2026-10-09', NULL, '스와 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Jaodori%20of%20Nagasaki%20Kunchi.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '나가사키 군치' AND start_date = '2026-10-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구마노 하야타마 마쓰리', '체험', '일본에서 열리는 축제', '2026-10-15', NULL, '구마노 하야타마 대사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%BE%A1%E8%88%B9%E7%A5%AD%20%E6%97%A9%E8%88%B9.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구마노 하야타마 마쓰리' AND start_date = '2026-10-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '나다 겐카 마쓰리(싸움 축제)', '체험', '일본에서 열리는 축제', '2026-10-15', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nada%20no%20Kenka%20matsuri%2004.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '나다 겐카 마쓰리(싸움 축제)' AND start_date = '2026-10-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '이베 신사 축제(마쓰에시)', '체험', '일본에서 열리는 축제', '2026-10-19', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%BF%8C%E9%83%A8%E7%A5%9E%E7%A4%BE.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '이베 신사 축제(마쓰에시)' AND start_date = '2026-10-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가쓰야마 마쓰리', '체험', '일본에서 열리는 축제', '2026-10-20', NULL, '마니와시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가쓰야마 마쓰리' AND start_date = '2026-10-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '구라마 불축제', '체험', '일본에서 열리는 축제', '2026-10-22', NULL, '유키 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%9E%8D%E9%A6%AC%E3%81%AE%E7%81%AB%E7%A5%AD4.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '구라마 불축제' AND start_date = '2026-10-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지다이마쓰리', '공연', '일본에서 열리는 축제', '2026-10-22', NULL, '교토시', 'http://commons.wikimedia.org/wiki/Special:FilePath/JidaiMatsuri%20Gohouren.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지다이마쓰리' AND start_date = '2026-10-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하카타 오쿤치', '체험', '일본에서 열리는 축제', '2026-10-24', NULL, '구시다 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하카타 오쿤치' AND start_date = '2026-10-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '우에노 덴진 마쓰리', '체험', '일본에서 열리는 축제', '2026-10-25', NULL, '스가와라 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iga%20City%20Danjiri%20Kaikan%20ac.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '우에노 덴진 마쓰리' AND start_date = '2026-10-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미야자키 신궁 대제', '체험', '일본에서 열리는 축제', '2026-10-26', NULL, '미야자키 신궁', 'http://commons.wikimedia.org/wiki/Special:FilePath/Miyazaki%20Shrine%20Grand%20Festival%20in%202008%20Gohouren%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미야자키 신궁 대제' AND start_date = '2026-10-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가라쓰쿤치', '체험', '일본에서 열리는 축제', '2026-11-04', NULL, '가라쓰 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hikiyama.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가라쓰쿤치' AND start_date = '2026-11-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '후지노미야 마쓰리', '체험', '일본에서 열리는 축제', '2026-11-05', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '후지노미야 마쓰리' AND start_date = '2026-11-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '시치고산', '체험', '일본의 연중 행사', '2026-11-15', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Brooklyn%20Museum%20-%20Dressing%20a%20Boy%20on%20the%20Occasion%20of%20His%20First%20Letting%20His%20Hair%20Grow%20-%20Kitagawa%20Utamaro%20-%20overall.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '시치고산' AND start_date = '2026-11-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '미타 마쓰리', '체험', '일본에서 열리는 축제', '2026-11-23', NULL, '미나토구', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mita%20Festival%2C%20Keio%20University%20-%20Nov%2025%2C%202007%20%281%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '미타 마쓰리' AND start_date = '2026-11-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '야쓰시로 묘켄 마쓰리', '체험', '일본에서 열리는 축제', '2026-11-23', NULL, '야쓰시로 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '야쓰시로 묘켄 마쓰리' AND start_date = '2026-11-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지치부 요마쓰리', '체험', '일본에서 열리는 축제', '2026-12-02', NULL, '지치부 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/ChichibuFes1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지치부 요마쓰리' AND start_date = '2026-12-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '오쿠노토 아에노코토', '체험', '일본에서 열리는 축제', '2026-12-05', NULL, '오쿠노토', 'http://commons.wikimedia.org/wiki/Special:FilePath/Oku-noto%20no%20Aenokoto%2C%20offering%20meals%20to%20the%20deities.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '오쿠노토 아에노코토' AND start_date = '2026-12-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '한자의 날', '체험', '일본에서 열리는 축제', '2026-12-12', NULL, '기요미즈데라', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '한자의 날' AND start_date = '2026-12-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아코 기시 마쓰리', '체험', '일본에서 열리는 축제', '2026-12-14', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ako%20Gishisai%20De09%2013.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아코 기시 마쓰리' AND start_date = '2026-12-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '기시사이(의사제)', '체험', '일본에서 열리는 축제', '2026-12-14', NULL, '센가쿠지', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sengakuji%20Gishisai%20191214e.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '기시사이(의사제)' AND start_date = '2026-12-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가스가 와카미야 온마쓰리', '체험', '일본에서 열리는 축제', '2026-12-17', NULL, '가스가타이샤', 'http://commons.wikimedia.org/wiki/Special:FilePath/Motonobu%20Nakagawa%2C%20Mayor%20of%20Nara.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가스가 와카미야 온마쓰리' AND start_date = '2026-12-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다이쇼 천황제', '체험', '일본에서 열리는 축제', '2026-12-25', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다이쇼 천황제' AND start_date = '2026-12-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '조지아', '디드고로바', '체험', '조지아에서 열리는 축제', '2026-08-12', NULL, '조지아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Didgoroba%202012%20%282%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '조지아' AND title = '디드고로바' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '단오', '체험', '음력 5월 5일의 절기', '2026-05-05', NULL, '중국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Qing%20Dynasty%20Dragon%20Boat%20Festival.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '단오' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '단오', '체험', '동아시아의 명절', '2026-06-19', NULL, '중국', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%8D%97%E6%96%B9%E6%BE%B3%E9%BE%8D%E8%88%9F1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '단오' AND start_date = '2026-06-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '칠석', '체험', '칠석(한자: 七夕)은 중국 사대 민간전설의 견우와 직녀 전설에서 견우와 직녀가 1년에 한 번 만나는 날로, 칠석날로도 불린다. 한국과 중국에서는 음력 7월 7일이지만, 일본은 양력 7월 7일이다.', '2026-07-07', NULL, '중국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Niulang%20and%20Zhinv%20%28Long%20Corridor%29.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '칠석' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '중국 의사의 날', '체험', '중국에서 열리는 축제', '2026-08-19', NULL, '중국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '중국 의사의 날' AND start_date = '2026-08-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '중추절', '체험', '중국에서 열리는 축제', '2026-09-25', NULL, '중국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Montreal%20JBotanique1%20tango7174.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '중추절' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '한의절(한푸의 날)', '체험', '중국에서 열리는 축제', '2026-10-01', NULL, '중국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '한의절(한푸의 날)' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '케냐', '잠후리 데이(독립기념일)', '체험', '케냐에서 열리는 축제', '2026-12-12', NULL, '케냐', 'http://commons.wikimedia.org/wiki/Special:FilePath/March%20Defenders%20of%20Ukraine%20on%20Independence%20Day%20in%20Kyiv%2C%202021%20106.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '케냐' AND title = '잠후리 데이(독립기념일)' AND start_date = '2026-12-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '크로아티아', '크로아티아 책의 날', '체험', '크로아티아에서 열리는 축제', '2026-04-22', NULL, '크로아티아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '크로아티아' AND title = '크로아티아 책의 날' AND start_date = '2026-04-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '크로아티아', '책의 밤', '체험', '크로아티아에서 열리는 축제', '2026-04-23', NULL, '크로아티아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '크로아티아' AND title = '책의 밤' AND start_date = '2026-04-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '터키', '카보타주의 날', '체험', '터키에서 열리는 축제', '2026-07-01', NULL, '터키', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '터키' AND title = '카보타주의 날' AND start_date = '2026-07-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '파나마', '파나마 카니발', '공연', '파나마에서 열리는 축제', '2026-02-16', NULL, '파나마시티', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '파나마' AND title = '파나마 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', '라자림 카니발', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '포르투갈', 'http://commons.wikimedia.org/wiki/Special:FilePath/Caretos.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = '라자림 카니발' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', '마데이라 카니발', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '푼샬', 'http://commons.wikimedia.org/wiki/Special:FilePath/Funchal%2C%20Cortejo%20Aleg%C3%B3rico%20de%20Carnaval%20%282026%29%2006.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = '마데이라 카니발' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', '포덴스 카니발', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '포덴스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Podence%202011%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = '포덴스 카니발' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', '토레스베드라스 카니발', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '포르투갈', 'http://commons.wikimedia.org/wiki/Special:FilePath/The%20Shinning%20Portuguese%20Mardi%20Gr%C3%A1s%20%28120757887%29.jpeg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = '토레스베드라스 카니발' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '그랑 포코니에 축제', '체험', '프랑스에서 열리는 축제', '2026-07-14', NULL, '프랑스', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '그랑 포코니에 축제' AND start_date = '2026-07-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', '리옹 빛 축제', '체험', '프랑스에서 열리는 축제', '2026-12-08', NULL, '리옹', 'http://commons.wikimedia.org/wiki/Special:FilePath/F%C3%AAte%20des%20Lumi%C3%A8res%20%28Lyon%2C%202024%2C%20place%20Bellecour%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = '리옹 빛 축제' AND start_date = '2026-12-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '핀란드', '헬싱키의 날', '체험', '핀란드에서 열리는 축제', '2026-06-12', NULL, '핀란드', 'http://commons.wikimedia.org/wiki/Special:FilePath/Aerial%20photograph%20of%20Helsinki%20downtown.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '핀란드' AND title = '헬싱키의 날' AND start_date = '2026-06-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '땅끝해넘이해맞이축제', '체험', '대한민국 전라남도 해남군 땅끝마을에서 매년 열리는 해넘이·해맞이 축제', '2026-01-01', NULL, '땅끝마을', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '땅끝해넘이해맞이축제' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '완도 해맞이 행사', '체험', '한국에서 열리는 축제', '2026-01-01', NULL, '한국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Wando%20sunrise.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '완도 해맞이 행사' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '헝가리', '데브레첸 꽃 카니발', '공연', '헝가리에서 열리는 축제', '2026-08-20', NULL, '헝가리', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '헝가리' AND title = '데브레첸 꽃 카니발' AND start_date = '2026-08-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', '오스트레일리아 데이 라이브 콘서트', '공연', '호주에서 열리는 축제', '2026-01-26', NULL, '호주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = '오스트레일리아 데이 라이브 콘서트' AND start_date = '2026-01-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', '오란여 페스트', '체험', '호주에서 열리는 축제', '2026-04-27', NULL, '호주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Netherlands-Australia%2002.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = '오란여 페스트' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', '뉴이어스이브 인 더 파크', '공연', '호주에서 열리는 축제', '2026-12-31', NULL, '빅토리아 파크', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = '뉴이어스이브 인 더 파크' AND start_date = '2026-12-31');
