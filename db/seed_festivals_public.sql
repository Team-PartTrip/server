-- 공개 데이터로 자동 생성된 축제 시드 (Func-002-03)
--   축제 정보 : 위키데이터 (CC0)  https://query.wikidata.org
--   2026년 날짜: Nager.Date       https://date.nager.at
--   국가명 표기: mledoze/countries
-- 같은 나라 · 같은 이름 · 같은 날짜가 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.
--
-- 날짜 출처: 공휴일 대조 41건 / 위키데이터 연례 개최일 240건
-- 날짜를 어느 쪽에서도 알 수 없는 축제는 넣지 않았습니다.
-- 한국어 이름이 없는 축제는 영어 이름 그대로 들어갑니다.

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '가나', 'Ankos Festival', '체험', '가나에서 열리는 축제', '2026-12-24', NULL, 'Takoradi', 'http://commons.wikimedia.org/wiki/Special:FilePath/Takoradi%20Masquerade%20Festival%2015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '가나' AND title = 'Ankos Festival' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '나이지리아', 'Nupe Cultural Day', '체험', '십년', '2026-06-26', NULL, '코기주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '나이지리아' AND title = 'Nupe Cultural Day' AND start_date = '2026-06-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '나이지리아', 'Ede-Aroh festival', '체험', '나이지리아에서 열리는 축제', '2026-12-29', NULL, '아남브라주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '나이지리아' AND title = 'Ede-Aroh festival' AND start_date = '2026-12-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '남아프리카', 'Cape Town Minstrel Carnival', '공연', '남아프리카에서 열리는 축제', '2026-01-02', NULL, '케이프타운', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '남아프리카' AND title = 'Cape Town Minstrel Carnival' AND start_date = '2026-01-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', '538 Koningsdag', '공연', '네덜란드에서 열리는 축제', '2026-04-27', NULL, '네덜란드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = '538 Koningsdag' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', 'Oranje Fest', '체험', '네덜란드에서 열리는 축제', '2026-04-27', NULL, '네덜란드', 'http://commons.wikimedia.org/wiki/Special:FilePath/Netherlands-Australia%2002.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = 'Oranje Fest' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네덜란드', 'Bevrijdingspop', '공연', '네덜란드에서 열리는 축제', '2026-05-05', NULL, 'Haarlemmerhout', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bvf%20haarlem%2011%20hq.jpeg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네덜란드' AND title = 'Bevrijdingspop' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '네팔', 'Kukur Tihar', '체험', '네팔에서 열리는 축제', '2026-11-14', NULL, '네팔', 'http://commons.wikimedia.org/wiki/Special:FilePath/Dog%20in%20Kathmandu%20after%20Kukur%20Puja.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '네팔' AND title = 'Kukur Tihar' AND start_date = '2026-11-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '뉴질랜드', 'Matariki', '체험', '뉴질랜드에서 열리는 축제', '2026-07-10', NULL, '뉴질랜드', 'http://commons.wikimedia.org/wiki/Special:FilePath/The%20Matariki%20Stars.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '뉴질랜드' AND title = 'Matariki' AND start_date = '2026-07-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', 'Founding of the Republic of China Day', '체험', '대만에서 열리는 축제', '2026-01-01', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = 'Founding of the Republic of China Day' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', 'Taiwan Lantern Festival', '예술품', '대만에서 열리는 축제', '2026-01-15', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Taiwan%20Lantern%20Festival%20in%202008.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = 'Taiwan Lantern Festival' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', 'Peace Memorial Day', '체험', '대만에서 열리는 축제', '2026-02-28', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = 'Peace Memorial Day' AND start_date = '2026-02-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', 'Taiwan Day Against Invasion', '체험', '대만에서 열리는 축제', '2026-03-14', NULL, '대만', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = 'Taiwan Day Against Invasion' AND start_date = '2026-03-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '단오', '체험', '음력 5월 5일의 절기', '2026-05-05', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Qing%20Dynasty%20Dragon%20Boat%20Festival.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '단오' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', 'National Disaster Prevention Day', '체험', '대만에서 열리는 축제', '2026-09-21', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/09.19%20%E7%B8%BD%E7%B5%B1%E8%A6%96%E5%B0%8E%E3%80%8C114%E5%B9%B4%E5%9C%8B%E5%AE%B6%E9%98%B2%E7%81%BD%E6%97%A5%EF%BC%8D%E5%90%84%E7%B4%9A%E5%AD%B8%E6%A0%A1%E5%8F%8A%E5%B9%BC%E5%85%92%E5%9C%92%E5%9C%B0%E9%9C%87%E9%81%BF%E9%9B%A3%E6%8E%A9%E8%AD%B7%E6%BC%94%E7%B7%B4%E3%80%8D%20-%2054797657905.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = 'National Disaster Prevention Day' AND start_date = '2026-09-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '대만', '쌍십절', '체험', '중화민국의 국경일', '2026-10-10', NULL, '대만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mother%20and%20sons%20attending%20the%20celebration%20ceremony%20of%202018%20Republic%20of%20China%20National%20Day.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '대만' AND title = '쌍십절' AND start_date = '2026-10-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '독일', 'Biike Burning', '체험', '독일에서 열리는 축제', '2026-02-21', NULL, 'North Frisia', 'http://commons.wikimedia.org/wiki/Special:FilePath/Biikebrennen%20II%202025.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '독일' AND title = 'Biike Burning' AND start_date = '2026-02-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '독일', 'Annabergfest', '체험', '독일에서 열리는 축제', '2026-07-26', NULL, 'Sulzbach-Rosenberg', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '독일' AND title = 'Annabergfest' AND start_date = '2026-07-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '러시아', 'Tat Cult Fest', '체험', '러시아에서 열리는 축제', '2026-08-30', NULL, '카잔 크렘린', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '러시아' AND title = 'Tat Cult Fest' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '말레이시아', '카아마탄', '체험', '말레이시아에서 열리는 축제', '2026-05-30', NULL, '말레이시아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Penampang%20Sabah%20Kaamatan-Celebrations-2014-01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '말레이시아' AND title = '카아마탄' AND start_date = '2026-05-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '말레이시아', 'Gawai Dayak', '체험', '말레이시아에서 열리는 축제', '2026-06-01', NULL, '서칼리만탄주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iban%20girls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '말레이시아' AND title = 'Gawai Dayak' AND start_date = '2026-06-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '모로코', 'Imilchil Moussem', '체험', '모로코에서 열리는 축제', '2026-09-18', NULL, 'Imilchil', 'http://commons.wikimedia.org/wiki/Special:FilePath/Imilchil%20folklor.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '모로코' AND title = 'Imilchil Moussem' AND start_date = '2026-09-18');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '몽골국', '나담', '체험', '몽골국에서 열리는 축제', '2026-07-13', NULL, '몽골국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Naadam%20Festival%202024%20Opening%20Ceremony.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '몽골국' AND title = '나담' AND start_date = '2026-07-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'National Bell Festival', '체험', '미국에서 열리는 축제', '2026-01-01', NULL, '미국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'National Bell Festival' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'Anime New Mexico', '체험', '미국에서 열리는 축제', '2026-03-27', NULL, 'Embassy Suites Albuquerque', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'Anime New Mexico' AND start_date = '2026-03-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'Harrisburg Independence Day Celebration', '공연', '미국에서 열리는 축제', '2026-07-03', NULL, '미국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hbg%20Riverfront%20Park%207-4-21.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'Harrisburg Independence Day Celebration' AND start_date = '2026-07-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'Canal Winchester Labor Day Festival', '체험', '미국에서 열리는 축제', '2026-09-07', NULL, '미국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'Canal Winchester Labor Day Festival' AND start_date = '2026-09-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'Labor Day Carnival', '공연', '미국에서 열리는 축제', '2026-09-07', NULL, '뉴욕', 'http://commons.wikimedia.org/wiki/Special:FilePath/West%20Indian%20Day%20Parade%202008-09-01%20woman%20in%20red%20costume.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'Labor Day Carnival' AND start_date = '2026-09-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미국', 'Indigenous Peoples Day Chicago', '공연', '미국에서 열리는 축제', '2026-10-12', NULL, '시카고', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미국' AND title = 'Indigenous Peoples Day Chicago' AND start_date = '2026-10-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '미크로네시아', 'Yap Day', '체험', '미크로네시아에서 열리는 축제', '2026-03-01', NULL, '미크로네시아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '미크로네시아' AND title = 'Yap Day' AND start_date = '2026-03-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베냉', 'Fête du Vodoun', '체험', '베냉에서 열리는 축제', '2026-01-10', NULL, '우이다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Vodun%20Days%20-%20Ar%C3%A8ne%20de%20Ouidah.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베냉' AND title = 'Fête du Vodoun' AND start_date = '2026-01-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베네수엘라', 'El Pozón del Saladillo', '체험', '베네수엘라에서 열리는 축제', '2026-12-24', NULL, 'El Saladillo', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베네수엘라' AND title = 'El Pozón del Saladillo' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '베트남', 'Anniversary of Duc Chi Ton', '체험', '베트남에서 열리는 축제', '2026-01-09', NULL, 'Great Divine Temple', 'http://commons.wikimedia.org/wiki/Special:FilePath/C%C3%BAng%20%C4%91%C3%A0n%20%C4%90%E1%BA%A1i%20l%E1%BB%85%20%C4%90%E1%BB%A9c%20Ch%C3%AD%20T%C3%B4n%2C%20T%C3%A2y%20Ninh%2C%202022.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '베트남' AND title = 'Anniversary of Duc Chi Ton' AND start_date = '2026-01-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '벨기에', 'Bal National', '공연', '벨기에에서 열리는 축제', '2026-07-20', NULL, 'Place du Jeu de Balle - Vossenplein', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '벨기에' AND title = 'Bal National' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '벨기에', 'Hololool', '체험', '벨기에에서 열리는 축제', '2026-11-10', NULL, '벨기에', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '벨기에' AND title = 'Hololool' AND start_date = '2026-11-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '보스니아 헤르체고비나', 'Čimburijada', '체험', '보스니아 헤르체고비나에서 열리는 축제', '2026-03-21', NULL, '보스니아 헤르체고비나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '보스니아 헤르체고비나' AND title = 'Čimburijada' AND start_date = '2026-03-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '볼리비아', 'Oruro carnival', '공연', '볼리비아에서 열리는 축제', '2026-02-16', NULL, '오루로', 'http://commons.wikimedia.org/wiki/Special:FilePath/DiabladaFerroviariadeOruroBolivia.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '볼리비아' AND title = 'Oruro carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '볼리비아', 'feast of the ñatitas', '체험', '볼리비아에서 열리는 축제', '2026-11-08', NULL, '볼리비아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '볼리비아' AND title = 'feast of the ñatitas' AND start_date = '2026-11-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnaval da Bahia', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '바이아주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bloco%20As%20Raparigas%20no%20Carnaval%20de%20Barreiras%20em%20%2820.02%29.%20Foto-%20Ronaldo%20Carvalho%20-%20Ag%20A%20TARDE%20%286916616465%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnaval da Bahia' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnaval de Brasília', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20em%20Bras%C3%ADlia%20de%20bailes%20em%20clubes%20a%20blocos%20de%20rua%20%2849560301488%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnaval de Brasília' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnaval de Florianópolis', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '플로리아노폴리스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Desfileuim2014.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnaval de Florianópolis' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnaval de Pernambuco', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnaval de Pernambuco' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnaval in Maragogipe', '체험', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Maragogipe%20foto%20Fernando%20Barbosa%20%284%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnaval in Maragogipe' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnival in Corumba', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Corumb%C3%A1%202017%20-%20Mocidade%20Nova%20Corumb%C3%A1%20-%20Carnaval%20Float.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnival in Corumba' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnival in Tibagi', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Tibagi.5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnival in Tibagi' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnival of Baixada Santista', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', 'http://commons.wikimedia.org/wiki/Special:FilePath/Brasil%20-%20Carnaval%202013.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnival of Baixada Santista' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnival of Olinda', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '올린다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Passistas%20%283219530631%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnival of Olinda' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Carnival of São Paulo', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '상파울루', 'http://commons.wikimedia.org/wiki/Special:FilePath/Camisa%20Verde%20e%20Branco%20%282010%29.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Carnival of São Paulo' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Florianópolis Gay Carnival', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Florianópolis Gay Carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Rio Music Carnival', '공연', '브라질에서 열리는 축제', '2026-02-16', NULL, '브라질', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Rio Music Carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', '리우 카니발', '공연', '브라질의 리우데자네이루에서 열리는 삼바 축제', '2026-02-16', NULL, '리우데자네이루', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnival%20in%20Rio%20de%20Janeiro.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = '리우 카니발' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '브라질', 'Tiradentes Film Festival', '공연', '브라질에서 열리는 축제', '2026-04-21', NULL, 'Tiradentes', 'http://commons.wikimedia.org/wiki/Special:FilePath/16%C2%AA%20Mostra%20de%20Cinema%20de%20Tiradentes%20%288395191148%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '브라질' AND title = 'Tiradentes Film Festival' AND start_date = '2026-04-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '상투메 프린시페', 'Auto de Floripes', '공연', '상투메 프린시페에서 열리는 축제', '2026-08-10', NULL, '프린시페섬', 'http://commons.wikimedia.org/wiki/Special:FilePath/Principe%20Island%201357.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '상투메 프린시페' AND title = 'Auto de Floripes' AND start_date = '2026-08-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '세르비아', 'Biennial of children\'s artistic expression', '예술품', '세르비아에서 열리는 축제', '2026-05-15', NULL, '판체보', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '세르비아' AND title = 'Biennial of children\'s artistic expression' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '수리남', 'Srefidensi Dey', '체험', '수리남에서 열리는 축제', '2026-11-25', NULL, '수리남', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '수리남' AND title = 'Srefidensi Dey' AND start_date = '2026-11-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'cossiers d\'Algaida', '체험', '스페인에서 열리는 축제', '2026-01-16', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ball%20dels%20cossiers%20d%27Algaida%202014-07-24%2018-31.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'cossiers d\'Algaida' AND start_date = '2026-01-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Las Carantoñas, Acehúche', '체험', '스페인에서 열리는 축제', '2026-01-20', NULL, 'Acehúche', 'http://commons.wikimedia.org/wiki/Special:FilePath/Las%20Caranto%C3%B1as%20de%20Aceh%C3%BAche%20en%20el%20VIII%20Festival%20de%20la%20M%C3%A1scara%20de%20Zamora%20%2830083593165%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Las Carantoñas, Acehúche' AND start_date = '2026-01-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Tamborrada', '체험', '스페인에서 열리는 축제', '2026-01-20', NULL, '산세바스티안', 'http://commons.wikimedia.org/wiki/Special:FilePath/San%20Sebastian%20Tamborrada%20Infantil.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Tamborrada' AND start_date = '2026-01-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festes Decennals de la Mare de Déu de la Candela', '체험', '스페인에서 열리는 축제', '2026-02-02', NULL, '발스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Decenals%20de%20Valls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festes Decennals de la Mare de Déu de la Candela' AND start_date = '2026-02-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festivities of San Blas, Ateca', '체험', '스페인에서 열리는 축제', '2026-02-03', NULL, 'Ateca', 'http://commons.wikimedia.org/wiki/Special:FilePath/1926-02-11%2C%20La%20Voz%20de%20Arag%C3%B3n%2C%20Las%20fiestas%20de%20San%20Blas%20en%20Ateca.%E2%80%94La%20tradicional%20fiesta%20de%20la%20m%C3%A1scara%2C%20o%20el%20terror%20de%20los%20chiquillos%2C%20Rodero%20%28cropped%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festivities of San Blas, Ateca' AND start_date = '2026-02-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Completes de Cervera', '체험', '스페인에서 열리는 축제', '2026-02-05', NULL, 'Basilica of Santa Maria de Cervera', 'http://commons.wikimedia.org/wiki/Special:FilePath/Les%20Completes%20de%20Cervera%202021.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Completes de Cervera' AND start_date = '2026-02-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Manresa Festival of Light', '체험', '스페인에서 열리는 축제', '2026-02-21', NULL, '만레사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Manresa Festival of Light' AND start_date = '2026-02-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Moors and Christians of Alcoy', '체험', '스페인에서 열리는 축제', '2026-04-21', NULL, 'Alcoy', 'http://commons.wikimedia.org/wiki/Special:FilePath/Detalle%20alardo%20tarde%202007.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Moors and Christians of Alcoy' AND start_date = '2026-04-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Diada del 25 d\'abril', '체험', '스페인에서 열리는 축제', '2026-04-25', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Val%C3%A8ncia%2016-04-2011.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Diada del 25 d\'abril' AND start_date = '2026-04-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'San Prudencio festival', '체험', '스페인에서 열리는 축제', '2026-04-27', NULL, '비토리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Campas%20de%20Armentia%20el%20d%C3%ADa%20de%20San%20Prudencio%20%2826455091780%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'San Prudencio festival' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'San Isidro Day', '체험', '스페인에서 열리는 축제', '2026-05-15', NULL, '마드리드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'San Isidro Day' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Corpus Christi flower carpets', '체험', '스페인에서 열리는 축제', '2026-06-04', NULL, 'La Orotava', 'http://commons.wikimedia.org/wiki/Special:FilePath/Orotava%20Corpus2008%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Corpus Christi flower carpets' AND start_date = '2026-06-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Falles de La Pobla de Segur', '체험', '스페인에서 열리는 축제', '2026-06-17', NULL, 'La Pobla de Segur', 'http://commons.wikimedia.org/wiki/Special:FilePath/Desfilada%20de%20falles%20de%20La%20Pobla%20de%20Segur%20l%27any%202014.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Falles de La Pobla de Segur' AND start_date = '2026-06-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Bonfires of Saint John in Alicante', '체험', '스페인에서 열리는 축제', '2026-06-23', NULL, '알리칸테', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hogueras%202008%20-%20Gran%20Via%20La%20Ceramica%202.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Bonfires of Saint John in Alicante' AND start_date = '2026-06-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festes de Sant Joan de Ciutadella', '체험', '스페인에서 열리는 축제', '2026-06-24', NULL, '시우타데야데메노르카', 'http://commons.wikimedia.org/wiki/Special:FilePath/SantJoan%287%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festes de Sant Joan de Ciutadella' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Sant Joan Pelós', '체험', '스페인에서 열리는 축제', '2026-06-24', NULL, 'Felanitx', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sant%20Joan%20Pel%C3%B3s%20ballant.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Sant Joan Pelós' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Sant Joan d\'Alacant film festival', '공연', '스페인에서 열리는 축제', '2026-06-24', NULL, 'Sant Joan d\'Alacant', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Sant Joan d\'Alacant film festival' AND start_date = '2026-06-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'cossiers de Pollença', '체험', '스페인에서 열리는 축제', '2026-06-29', NULL, 'Pollença', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'cossiers de Pollença' AND start_date = '2026-06-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', '산 페르민 축제', '체험', '스페인에서 열리는 축제', '2026-07-07', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Pamplona%20Sanfermines%20Calle%20San%20Nicolas.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = '산 페르민 축제' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Santiago Apóstol Festival', '체험', '스페인에서 열리는 축제', '2026-07-25', NULL, '산티아고데콤포스텔라', 'http://commons.wikimedia.org/wiki/Special:FilePath/Santiago.%20Fuegos%20del%20Ap%C3%B3stol%202012%20%287640755270%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Santiago Apóstol Festival' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Les Santes', '체험', '스페인에서 열리는 축제', '2026-07-27', NULL, '마타로', 'http://commons.wikimedia.org/wiki/Special:FilePath/FB-7zK-SgpZ87VIHVp7x2w-1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Les Santes' AND start_date = '2026-07-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Cercavila de l\'Imaginari de Vilanova i la Geltrú', '체험', '스페인에서 열리는 축제', '2026-08-06', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/L%27arribada%20del%20ferrocarril%20%283796752350%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Cercavila de l\'Imaginari de Vilanova i la Geltrú' AND start_date = '2026-08-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Nit de l\'Albà', '체험', '스페인에서 열리는 축제', '2026-08-13', NULL, '엘체', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nit%20de%20L%27Alb%C3%A0%20Elche.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Nit de l\'Albà' AND start_date = '2026-08-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festa Major de Gràcia', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, 'Vila de Gràcia', 'http://commons.wikimedia.org/wiki/Special:FilePath/Festa%20Major%20de%20Gr%C3%A0cia%202026%20-%20Pla%C3%A7a%20Rovira%20i%20Trias%20-%2002.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festa Major de Gràcia' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Fiesta de la Virgen de Agosto y San Roque, Requena', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, 'Requena', 'http://commons.wikimedia.org/wiki/Special:FilePath/%22Imatge%20de%20Sant%20Roc%20de%20la%20Majordomia%20del%20Sant%20a%20Requena.%20Amb%20la%20seua%20banda%2C%20que%20porta%20una%20inscripci%C3%B3%20brodada%20que%20diu%20Viva%20San%20Roque%20y%20sus%20devotos.%20A%C3%B1o%201865.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Fiesta de la Virgen de Agosto y San Roque, Requena' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'cossiers de Montuïri', '체험', '스페인에서 열리는 축제', '2026-08-15', NULL, 'Montuïri', 'http://commons.wikimedia.org/wiki/Special:FilePath/Cossiersmont1.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'cossiers de Montuïri' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festa Major de Vilafranca del Penedès', '체험', '스페인에서 열리는 축제', '2026-08-30', NULL, 'Vilafranca del Penedès', 'http://commons.wikimedia.org/wiki/Special:FilePath/Entrada%20St%20Felix.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festa Major de Vilafranca del Penedès' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festa de la Fil·loxera', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Fil%C2%B7losereta%20a%20la%20Festa%20de%20la%20Fil%C2%B7loxera.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festa de la Fil·loxera' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festes del Tura', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, 'Olot', 'http://commons.wikimedia.org/wiki/Special:FilePath/Els%20Caballets%20of%20Olot%202008.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festes del Tura' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festivity of ‘la Mare de Déu de la Salut’ of Algemesí', '체험', '스페인에서 열리는 축제', '2026-09-08', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Algemes%C3%AD%20MDS%20-%20Les%20Llauradores%2004.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festivity of ‘la Mare de Déu de la Salut’ of Algemesí' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Montbau\'s patronal festival', '체험', '스페인에서 열리는 축제', '2026-09-30', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Som%20Montbau%202021.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Montbau\'s patronal festival' AND start_date = '2026-09-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Semana Cervantina de Alcalá de Henares', '체험', '스페인에서 열리는 축제', '2026-10-09', NULL, 'Fair of Alcalá de Henares', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Semana Cervantina de Alcalá de Henares' AND start_date = '2026-10-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'El Desarme', '음식', '스페인에서 열리는 축제', '2026-10-19', NULL, '스페인', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'El Desarme' AND start_date = '2026-10-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Fia-Faia', '체험', '스페인에서 열리는 축제', '2026-12-24', NULL, 'Bagà', 'http://commons.wikimedia.org/wiki/Special:FilePath/La%20Fia-faia%20Baga%20Sant%20Julia.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Fia-Faia' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Nit dels Fatxos', '체험', '스페인에서 열리는 축제', '2026-12-24', NULL, '스페인', 'http://commons.wikimedia.org/wiki/Special:FilePath/Concurso%20Fatxo%20gran.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Nit dels Fatxos' AND start_date = '2026-12-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '스페인', 'Festa del Pi de Centelles', '체험', '스페인에서 열리는 축제', '2026-12-30', NULL, 'Centelles', 'http://commons.wikimedia.org/wiki/Special:FilePath/Festadelpi%20penjant.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '스페인' AND title = 'Festa del Pi de Centelles' AND start_date = '2026-12-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '슬로베니아', 'Salamijada', '음식', '슬로베니아에서 열리는 축제', '2026-03-10', NULL, '슬로베니아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '슬로베니아' AND title = 'Salamijada' AND start_date = '2026-03-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아르헨티나', 'Buenos Aires Carnival', '공연', '아르헨티나에서 열리는 축제', '2026-02-16', NULL, '아르헨티나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아르헨티나' AND title = 'Buenos Aires Carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아르헨티나', 'Fiesta Nacional de la Navidad', '체험', '아르헨티나에서 열리는 축제', '2026-12-25', NULL, '아르헨티나', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아르헨티나' AND title = 'Fiesta Nacional de la Navidad' AND start_date = '2026-12-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Tullamaine Pattern', '체험', '아일랜드에서 열리는 축제', '2026-01-21', NULL, 'Maon\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Tullamaine Pattern' AND start_date = '2026-01-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Ullard Pattern', '체험', '아일랜드에서 열리는 축제', '2026-02-08', NULL, 'Ullard Church', 'http://commons.wikimedia.org/wiki/Special:FilePath/St%20Fiacre%27s%20pattern%20crosses%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Ullard Pattern' AND start_date = '2026-02-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Pattern of Tullaherin', '체험', '아일랜드에서 열리는 축제', '2026-03-05', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Pattern of Tullaherin' AND start_date = '2026-03-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Columbkille Pattern', '체험', '아일랜드에서 열리는 축제', '2026-06-09', NULL, 'Saint Columbkille\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Columbkille Pattern' AND start_date = '2026-06-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Pattern of Columcille', '체험', '아일랜드에서 열리는 축제', '2026-06-09', NULL, 'Saint Columbkille\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Pattern of Columcille' AND start_date = '2026-06-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Kilmanagh Pattern', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Kilmanagh Pattern' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Pattern of Killaloe', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, 'Tobermolua', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Pattern of Killaloe' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Toberaniddaun Pattern', '체험', '아일랜드에서 열리는 축제', '2026-07-31', NULL, 'Tobernadaun', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Toberaniddaun Pattern' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'St. Rioch\'s Pattern', '체험', '아일랜드에서 열리는 축제', '2026-08-01', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'St. Rioch\'s Pattern' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Owning Pattern', '체험', '아일랜드에서 열리는 축제', '2026-08-05', NULL, 'Tubbernamuchthee', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Owning Pattern' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Newmarket Pattern', '체험', '아일랜드에서 열리는 축제', '2026-08-12', NULL, 'Saint Brendan\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Newmarket Pattern' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Brabstown Pattern', '체험', '아일랜드에서 열리는 축제', '2026-08-15', NULL, 'St. Margaret\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Brabstown Pattern' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Kilmoganny Pattern', '체험', '아일랜드에서 열리는 축제', '2026-08-25', NULL, '아일랜드', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Kilmoganny Pattern' AND start_date = '2026-08-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Pattern of Sheestown', '체험', '아일랜드에서 열리는 축제', '2026-08-30', NULL, 'Saint Fiachra\'s Well', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bishop%20Coll%20et%20al.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Pattern of Sheestown' AND start_date = '2026-08-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'Pattern of Killahy', '체험', '아일랜드에서 열리는 축제', '2026-09-14', NULL, 'Tubbernacruchnee', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'Pattern of Killahy' AND start_date = '2026-09-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '아일랜드', 'St Leonard\'s Well Pattern', '체험', '아일랜드에서 열리는 축제', '2026-11-06', NULL, 'St Leonard\'s Well', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '아일랜드' AND title = 'St Leonard\'s Well Pattern' AND start_date = '2026-11-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '알바니아', 'Dita e Verës', '체험', '알바니아에서 열리는 축제', '2026-03-16', NULL, '알바니아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '알바니아' AND title = 'Dita e Verës' AND start_date = '2026-03-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '앙골라', 'Luanda Carnival', '공연', '앙골라에서 열리는 축제', '2026-02-16', NULL, '루안다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Luanda%20Skyline%20-%20Angola%202015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '앙골라' AND title = 'Luanda Carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '에콰도르', 'Diablada de Píllaro', '체험', '에콰도르에서 열리는 축제', '2026-01-01', NULL, '에콰도르', 'http://commons.wikimedia.org/wiki/Special:FilePath/Devil%20at%20the%20Diablada%20de%20Pillaro.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '에콰도르' AND title = 'Diablada de Píllaro' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '에콰도르', 'Al Sur del Cielo', '체험', '에콰도르에서 열리는 축제', '2026-12-31', NULL, '키토', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '에콰도르' AND title = 'Al Sur del Cielo' AND start_date = '2026-12-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', 'Allantide', '체험', '영국에서 열리는 축제', '2026-10-31', NULL, '콘월주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = 'Allantide' AND start_date = '2026-10-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', 'Lewes Bonfire', '체험', '영국에서 열리는 축제', '2026-11-05', NULL, '루이스', 'http://commons.wikimedia.org/wiki/Special:FilePath/Lewes%20Bonfire%2C%20Martyrs%20Crosses%2002%20detail.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = 'Lewes Bonfire' AND start_date = '2026-11-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '영국', 'Montol', '체험', '영국에서 열리는 축제', '2026-12-21', NULL, '영국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '영국' AND title = 'Montol' AND start_date = '2026-12-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '오스트리아', 'Rupertikirtag', '체험', '오스트리아에서 열리는 축제', '2026-09-24', NULL, '오스트리아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '오스트리아' AND title = 'Rupertikirtag' AND start_date = '2026-09-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '우크라이나', 'Independence Day with Makhno', '체험', '우크라이나에서 열리는 축제', '2026-08-24', NULL, '훌리아이폴', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '우크라이나' AND title = 'Independence Day with Makhno' AND start_date = '2026-08-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이란', '얄다', '체험', '이란에서 열리는 축제', '2026-12-20', NULL, '캐나다', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sarpol-e%20Zahab%20Yalda%20Night%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이란' AND title = '얄다' AND start_date = '2026-12-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', 'Palio del Niballo', '체험', '이탈리아에서 열리는 축제', '2026-01-05', NULL, 'Q3967752', 'http://commons.wikimedia.org/wiki/Special:FilePath/Palio%20del%20Niballo%201.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = 'Palio del Niballo' AND start_date = '2026-01-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', 'Festa del grano', '체험', '이탈리아에서 열리는 축제', '2026-08-16', NULL, '이탈리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Foglianise%20-%20Festa%20del%20Grano%202009%20-%2015.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = 'Festa del grano' AND start_date = '2026-08-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '이탈리아', 'Corteggio storico', '체험', '이탈리아에서 열리는 축제', '2026-09-08', NULL, '이탈리아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Fuochi%20artificiali%20in%20piazza%20Duomo%20in%20occasione%20della%20Madonna%20della%20Fiera.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '이탈리아' AND title = 'Corteggio storico' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', 'National Science Day', '체험', '인도에서 열리는 축제', '2026-02-28', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = 'National Science Day' AND start_date = '2026-02-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', 'Universal Pakhala Day', '체험', '인도에서 열리는 축제', '2026-03-20', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = 'Universal Pakhala Day' AND start_date = '2026-03-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도', 'Abhiruchi Sports Day', '체험', '인도에서 열리는 축제', '2026-09-03', NULL, '인도', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도' AND title = 'Abhiruchi Sports Day' AND start_date = '2026-09-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '인도네시아', 'Gawai Dayak', '체험', '인도네시아에서 열리는 축제', '2026-06-01', NULL, '서칼리만탄주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iban%20girls.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '인도네시아' AND title = 'Gawai Dayak' AND start_date = '2026-06-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nanori, Shimenawa-kiri, and Fire Festival', '체험', '일본에서 열리는 축제', '2026-01-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nanori, Shimenawa-kiri, and Fire Festival' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tōka Ebisu', '체험', '일본에서 열리는 축제', '2026-01-09', NULL, 'Nishinomiya Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Horikawaebisu-jinja%20Osaka%20Japan04-r.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tōka Ebisu' AND start_date = '2026-01-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Mukonage and Suminuri', '체험', '일본에서 열리는 축제', '2026-01-15', NULL, 'Matsunoyama Onsen', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mukonage%20Matsunoyama%202026.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Mukonage and Suminuri' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'koshōgatsu', '체험', '일본에서 열리는 축제', '2026-01-15', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'koshōgatsu' AND start_date = '2026-01-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Atagosya no Hi Matsuri', '체험', '일본에서 열리는 축제', '2026-01-26', NULL, '우오즈시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Atagosya no Hi Matsuri' AND start_date = '2026-01-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '세쓰분', '체험', '일본에서 열리는 축제', '2026-02-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Setsubun.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '세쓰분' AND start_date = '2026-02-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Otō Matsuri', '체험', '일본에서 열리는 축제', '2026-02-06', NULL, 'Kamikura Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/518wakayama-Kumano%20Oto%20Festival-xl.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Otō Matsuri' AND start_date = '2026-02-06');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kasedori', '체험', '일본에서 열리는 축제', '2026-02-11', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kasedori%202026%20Kaminoyama%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kasedori' AND start_date = '2026-02-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Gero Ta-no-Kami Festival', '체험', '일본에서 열리는 축제', '2026-02-14', NULL, 'Mori Minashi Hachiman Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gero%20no%20Ta%20no%20Kami%20Festival%2C%20Marching%20people.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Gero Ta-no-Kami Festival' AND start_date = '2026-02-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hiburi Kamakura', '체험', '일본에서 열리는 축제', '2026-02-14', NULL, 'Kakunodate', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hiburi%20Kamakura%20in%20Kakunodate%202019b.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hiburi Kamakura' AND start_date = '2026-02-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '히나마쓰리', '체험', '소녀의 성장을 축하하는 일본의 전통 축제', '2026-03-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ist%20di%20Cultura%20giapponese%20-%20altare%20della%20festa%20delle%20bambole%20P1100919.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '히나마쓰리' AND start_date = '2026-03-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'omizutori', '체험', '일본에서 열리는 축제', '2026-03-12', NULL, 'Nigatsu-dō', 'http://commons.wikimedia.org/wiki/Special:FilePath/Omizutori.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'omizutori' AND start_date = '2026-03-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kasuga-sai', '체험', '일본에서 열리는 축제', '2026-03-13', NULL, '가스가타이샤', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kasuga-sai' AND start_date = '2026-03-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hōnen Matsuri', '체험', '일본에서 열리는 축제', '2026-03-15', NULL, 'Tagata Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/H%C5%8Dnen%20Matsuri%202.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hōnen Matsuri' AND start_date = '2026-03-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Emperor Jinmu Festival', '체험', '일본에서 열리는 축제', '2026-04-03', NULL, 'Three Palace Sanctuaries', 'http://commons.wikimedia.org/wiki/Special:FilePath/Jinmusai-fes1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Emperor Jinmu Festival' AND start_date = '2026-04-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Chichibu Shrine Otauesai', '체험', '일본에서 열리는 축제', '2026-04-04', NULL, 'Chichibu Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Chichibu Shrine Otauesai' AND start_date = '2026-04-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hana matsuri', '체험', '일본에서 열리는 축제', '2026-04-08', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/A%20birthday%20of%20Buddha%2Chanamatsuri%2Ckanpukuji-temple%2Ckatori-city%2Cjapan.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hana matsuri' AND start_date = '2026-04-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hiyoshi Sannō-sai', '체험', '일본에서 열리는 축제', '2026-04-14', NULL, '히요시타이샤', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hiyoshi Sannō-sai' AND start_date = '2026-04-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kumano Hongū Taisha Reitaisha', '체험', '일본에서 열리는 축제', '2026-04-15', NULL, 'Kumano Hongū Taisha', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kumano Hongū Taisha Reitaisha' AND start_date = '2026-04-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Takayama Festival', '체험', '일본에서 열리는 축제', '2026-04-15', NULL, 'Hie Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%A4%A7%E5%9B%BD%E5%8F%B0%20%28%E5%B2%90%E9%98%9C%E7%9C%8C%E9%AB%98%E5%B1%B1%E5%B8%82%29%20-%20panoramio%20%282%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Takayama Festival' AND start_date = '2026-04-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Furukawa Festival', '체험', '일본에서 열리는 축제', '2026-04-20', NULL, 'Keta Wakamiya Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Furukawa-yatai.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Furukawa Festival' AND start_date = '2026-04-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Takaoka Mikurumayama Festival', '체험', '일본에서 열리는 축제', '2026-05-01', NULL, 'Takaoka Sekino Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%AB%98%E5%B2%A1%E5%B8%82%E8%A1%97%E3%81%AE%E9%A2%A8%E6%99%AF%20-%20panoramio.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Takaoka Mikurumayama Festival' AND start_date = '2026-05-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Chiryū Festival', '체험', '일본에서 열리는 축제', '2026-05-02', NULL, 'Chiryū Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tiryuumatsuri7.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Chiryū Festival' AND start_date = '2026-05-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hakata Matsubayashi', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Matsubayashi05.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hakata Matsubayashi' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hiroshima Flower Festival', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hana-no-to2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hiroshima Flower Festival' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kamezaki Shiohi Festival', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, 'Kamisaki Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kamezakishiohi%20Festival2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kamezaki Shiohi Festival' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Marugame Castle Festival', '체험', '일본에서 열리는 축제', '2026-05-03', NULL, '마루가메성', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Marugame Castle Festival' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Odawara Hōjō Godai Festival', '공연', '일본에서 열리는 축제', '2026-05-03', NULL, '오다와라시', 'http://commons.wikimedia.org/wiki/Special:FilePath/HJ5SH1.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Odawara Hōjō Godai Festival' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '하카타 돈타쿠', '체험', '일본 후쿠오카의 연례 행사', '2026-05-03', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hakata%20Dontaku%2078338697%20org.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '하카타 돈타쿠' AND start_date = '2026-05-03');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tado Festival', '체험', '일본에서 열리는 축제', '2026-05-04', NULL, 'Tado Taisha', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tado%20Festival%202.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tado Festival' AND start_date = '2026-05-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Johana Hikiyama Festival', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%9F%8E%E7%AB%AF%E7%94%BA%20%E6%9B%B3%E5%B1%B1%E7%A5%AD%E3%82%8A%20SLKY20180505%200000057.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Johana Hikiyama Festival' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kokufu-sai', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kokufu-sai' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kurayami Matsuri', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, 'Ōkunitama Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Okunitama-jinja-24.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kurayami Matsuri' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Suitengū Spring Festival', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, 'Kurume Suitengū', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Suitengū Spring Festival' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '일본의 단오', '체험', '일본에서 열리는 축제', '2026-05-05', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Flying%20Koi%20by%20tiseb%20in%20Nagasaki.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '일본의 단오' AND start_date = '2026-05-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Miyazu Matsuri', '체험', '일본에서 열리는 축제', '2026-05-13', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ukidaiko.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Miyazu Matsuri' AND start_date = '2026-05-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Ōgaki Festival', '체험', '일본에서 열리는 축제', '2026-05-15', NULL, 'Ōgaki Hachiman Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%A4%A7%E5%9E%A3%E5%B8%82%28%E5%A4%A7%E5%9E%A3%E3%81%BE%E3%81%A4%E3%82%8A%29%20-%20panoramio.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Ōgaki Festival' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아오이마쓰리', '체험', '일본에서 열리는 축제', '2026-05-15', NULL, 'Kamo Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Aoi%20Matsuri.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아오이마쓰리' AND start_date = '2026-05-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Mikuni Matsuri', '체험', '일본에서 열리는 축제', '2026-05-19', NULL, 'Mikuni Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mikuni%20festival%202022.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Mikuni Matsuri' AND start_date = '2026-05-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sannō Matsuri', '체험', '일본에서 열리는 축제', '2026-05-31', NULL, 'Hie Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sannō Matsuri' AND start_date = '2026-05-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Agata Matsuri', '체험', '일본에서 열리는 축제', '2026-06-05', NULL, 'Agata Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Bonten%20togyo.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Agata Matsuri' AND start_date = '2026-06-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Otaue Shinto Service', '체험', '일본에서 열리는 축제', '2026-06-14', NULL, 'Onda, Sumiyoshi Taisha', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sumiyoshi%20jinja%20Otaue.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Otaue Shinto Service' AND start_date = '2026-06-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kangensai', '체험', '일본에서 열리는 축제', '2026-06-17', NULL, '히로시마만', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kangen%20jigozen2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kangensai' AND start_date = '2026-06-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Himeji Yukata Matsuri', '체험', '일본에서 열리는 축제', '2026-06-22', NULL, 'Osakabe Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Himeji%20Yukata%20Matsuri%202009p1%20003.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Himeji Yukata Matsuri' AND start_date = '2026-06-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Shimadachi Hadaka Matsuri', '체험', '일본에서 열리는 축제', '2026-06-30', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Shimadachi Hadaka Matsuri' AND start_date = '2026-06-30');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아이젠마쓰리', '체험', '일본 오사카부 오사카시 덴노지구의 아이젠도 쇼만인에서 열리는 축제', '2026-07-01', NULL, 'Shōman-in Temple', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아이젠마쓰리' AND start_date = '2026-07-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Crow-dipper sprouts', '체험', '일본에서 열리는 축제', '2026-07-02', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Saururus%20chinensis%20kz01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Crow-dipper sprouts' AND start_date = '2026-07-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '다나바타', '체험', '위키미디어 분류', '2026-07-07', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E4%B8%83%E5%A4%95%20%2819545533256%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '다나바타' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '칠석', '체험', '칠석(한자: 七夕)은 중국 사대 민간전설의 견우와 직녀 전설에서 견우와 직녀가 1년에 한 번 만나는 날로, 칠석날로도 불린다. 한국과 중국에서는 음력 7월 7일이지만, 일본은 양력 7월 7일이다.', '2026-07-07', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Niulang%20and%20Zhinv%20%28Long%20Corridor%29.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '칠석' AND start_date = '2026-07-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Ikutama Matsuri', '체험', '일본에서 열리는 축제', '2026-07-12', NULL, 'Ikukunitama Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Ikutama Matsuri' AND start_date = '2026-07-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'HIrano\'gō Natsu-matsuri', '체험', '일본에서 열리는 축제', '2026-07-13', NULL, 'Hirano', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'HIrano\'gō Natsu-matsuri' AND start_date = '2026-07-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Ōgi Matsuri', '체험', '일본에서 열리는 축제', '2026-07-14', NULL, 'Kumano Nachi Taisha', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Ōgi Matsuri' AND start_date = '2026-07-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Chichibu Kawase Matsuri', '체험', '일본에서 열리는 축제', '2026-07-20', NULL, 'Chichibu Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Chichibu%20Kawase%20Matsuri.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Chichibu Kawase Matsuri' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Jindai-ji Hōzuki Festival', '체험', '일본에서 열리는 축제', '2026-07-20', NULL, 'Jindai-ji Temple', 'http://commons.wikimedia.org/wiki/Special:FilePath/JindaijiMonzen.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Jindai-ji Hōzuki Festival' AND start_date = '2026-07-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Iida-machi Toroyama Festival', '체험', '일본에서 열리는 축제', '2026-07-21', NULL, 'Kasuga Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%A3%AF%E7%94%B0%E7%94%BA%E7%87%88%E7%B1%A0%E5%B1%B1%E7%A5%AD%E3%82%8A%EF%BC%88%E3%81%84%E3%81%84%E3%81%A0%E3%81%BE%E3%81%A1%E3%81%A8%E3%82%8D%E3%82%84%E3%81%BE%E3%81%BE%E3%81%A4%E3%82%8A%EF%BC%89.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Iida-machi Toroyama Festival' AND start_date = '2026-07-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tsuchizaki Shinmei Shrine Festival', '체험', '일본에서 열리는 축제', '2026-07-21', NULL, 'Tsuchizaki Shinmeisha', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tsuchi-yama-tsunafuru.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tsuchizaki Shinmei Shrine Festival' AND start_date = '2026-07-21');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Jizobon', '체험', '일본에서 열리는 축제', '2026-07-24', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Jizobon' AND start_date = '2026-07-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kyoto Gion Festival Yamahoko Parade', '공연', '일본에서 열리는 축제', '2026-07-24', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gion%20Matsuri%202017-5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kyoto Gion Festival Yamahoko Parade' AND start_date = '2026-07-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Mashiko Gion Matsuri', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Mashiko Gion Matsuri' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tanabe Matsuri', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, 'Tōkei Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E7%94%B0%E8%BE%BA%E7%A5%AD%28%E9%AC%AA%E9%9B%9E%E7%A5%9E%E7%A4%BE%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tanabe Matsuri' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '덴진마쓰리', '체험', '일본에서 열리는 축제', '2026-07-25', NULL, 'Ōsaka Tenmangū', 'http://commons.wikimedia.org/wiki/Special:FilePath/120725%20Osaka%20Tenjinmatsuri%20Japan08bs.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '덴진마쓰리' AND start_date = '2026-07-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kibune Matsuri', '체험', '일본에서 열리는 축제', '2026-07-28', NULL, '마나즈루정', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E8%A5%BF%E5%B0%8F%E6%97%A9%E8%88%B9%E3%83%BB%E8%B2%B4%E5%AE%AE%E4%B8%B8%20%E6%B5%B7%E4%B8%8A%E6%B8%A1%E5%BE%A1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kibune Matsuri' AND start_date = '2026-07-28');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hokkaido Belly Button Festival', '체험', '일본에서 열리는 축제', '2026-07-29', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hokkaido Belly Button Festival' AND start_date = '2026-07-29');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Namerikawa Nebuta Nagashi', '체험', '일본에서 열리는 축제', '2026-07-31', NULL, '나메리카와시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Namerikawa Nebuta Nagashi' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sumiyoshi Matsuri', '체험', '일본에서 열리는 축제', '2026-07-31', NULL, '스미요시타이샤', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sumiyoshi%20Matsuri%20%2804%29%20IMG%203224-2%2020140801.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sumiyoshi Matsuri' AND start_date = '2026-07-31');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nishinippon Ohori Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-01', NULL, '오호리 공원', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nishi-Nippon%20Ohori%20Fireworks%20Festival%202009.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nishinippon Ohori Fireworks Festival' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Okiyo Festival', '체험', '일본에서 열리는 축제', '2026-08-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Okiyo Festival' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'PL Art of Fireworks', '예술품', '일본에서 열리는 축제', '2026-08-01', NULL, '돈다바야시시', 'http://commons.wikimedia.org/wiki/Special:FilePath/PL%20Fireworks2010-5.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'PL Art of Fireworks' AND start_date = '2026-08-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hagi Summer Festival', '체험', '일본에서 열리는 축제', '2026-08-02', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hagi Summer Festival' AND start_date = '2026-08-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Rice Planting Festival at Kibitsuhiko Shrine', '체험', '일본에서 열리는 축제', '2026-08-02', NULL, 'Kibitsuhiko Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Rice Planting Festival at Kibitsuhiko Shrine' AND start_date = '2026-08-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Morioka Sansa Odori', '체험', '일본에서 열리는 축제', '2026-08-04', NULL, 'Central Avenue', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sansa%20Odori%202.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Morioka Sansa Odori' AND start_date = '2026-08-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Chikugo River Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-05', NULL, '지쿠고강', 'http://commons.wikimedia.org/wiki/Special:FilePath/ColorfulFireworks.png'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Chikugo River Fireworks Festival' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sendai Tanabata Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-05', NULL, 'Nishi Park', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sendai%20Tanabata%20Fireworks%20Festival%202009.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sendai Tanabata Fireworks Festival' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '아키타 간토 축제', '체험', '일본 아키타현 아키타시에서 열리는 축제', '2026-08-05', NULL, '아키타시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Akita%20Kanto%20Festival%202017.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '아키타 간토 축제' AND start_date = '2026-08-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Aomori Fireworks Display', '건물', '일본에서 열리는 축제', '2026-08-07', NULL, 'Port of Aomori', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Aomori Fireworks Display' AND start_date = '2026-08-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Naruto Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-07', NULL, 'Naruto Bunka Kaikan', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Naruto Fireworks Festival' AND start_date = '2026-08-07');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sendai Tanabata', '체험', '일본에서 열리는 축제', '2026-08-08', NULL, '센다이시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sendai%20Tanabata%202023.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sendai Tanabata' AND start_date = '2026-08-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Anjin Festival Sea Fireworks', '건물', '일본에서 열리는 축제', '2026-08-10', NULL, '이토시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Anjin Festival Sea Fireworks' AND start_date = '2026-08-10');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Mikuni Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-11', NULL, 'Mikuni Sunset Beach', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mikuni%20fireworks%202013.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Mikuni Fireworks Festival' AND start_date = '2026-08-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Ubagami Daijingū Togyosai', '체험', '일본에서 열리는 축제', '2026-08-11', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E7%A5%9E%E8%BC%BF%E6%B8%A1%E5%BE%A11.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Ubagami Daijingū Togyosai' AND start_date = '2026-08-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Yosakoi Matsuri', '체험', '일본에서 열리는 축제', '2026-08-12', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Yosakoi%20Performers%20at%20Kochi%20Yosakoi%20Matsuri%202005%2065.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Yosakoi Matsuri' AND start_date = '2026-08-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kachimai Fireworks', '건물', '일본에서 열리는 축제', '2026-08-13', NULL, '오비히로시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kachimai Fireworks' AND start_date = '2026-08-13');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sanuki Takamatsu Festival', '체험', '일본에서 열리는 축제', '2026-08-14', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Japan%20-%20Takamatsu%20Awa%20Odori%20Bon%20Festival%2003.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sanuki Takamatsu Festival' AND start_date = '2026-08-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Fukagawa Matsuri', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, 'Tomioka Hachiman Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tomioka%20hachimangu4.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Fukagawa Matsuri' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kujirabune Festival', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, 'Toride Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kujirabune01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kujirabune Festival' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Lake Suwa Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-15', NULL, '스와호', 'http://commons.wikimedia.org/wiki/Special:FilePath/Suwa-ko%20firework%2020080815%2002.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Lake Suwa Fireworks Festival' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nanbu Fire Festival', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, '난부정', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nanbu Fire Festival' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tomida Ishidori Matsuri', '체험', '일본에서 열리는 축제', '2026-08-15', NULL, 'Tomida', 'http://commons.wikimedia.org/wiki/Special:FilePath/Tomida%20ishidori.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tomida Ishidori Matsuri' AND start_date = '2026-08-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Gozan no Okuribi', '체험', '일본에서 열리는 축제', '2026-08-16', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Gozanokuribi%20Daimonji2.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Gozan no Okuribi' AND start_date = '2026-08-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kumano Great Fireworks Festival', '건물', '일본에서 열리는 축제', '2026-08-17', NULL, 'Shichirimi Beach', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kumano Great Fireworks Festival' AND start_date = '2026-08-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hanawa Bayashi', '체험', '일본에서 열리는 축제', '2026-08-20', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hanawabayashi%202012.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hanawa Bayashi' AND start_date = '2026-08-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Yokkabui', '체험', '일본에서 열리는 축제', '2026-08-22', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Yokkabui' AND start_date = '2026-08-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Shinomiya Matsuri', '체험', '일본에서 열리는 축제', '2026-08-23', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Shinomiya Matsuri' AND start_date = '2026-08-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Shinjō Matsuri', '체험', '일본에서 열리는 축제', '2026-08-25', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/ShinjoMatsuriNight.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Shinjō Matsuri' AND start_date = '2026-08-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Yoshida Fire Festival', '체험', '일본에서 열리는 축제', '2026-08-26', NULL, 'Kitaguchi Hongū Fuji Sengen Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Torches%20burning%20Yoshida%20Fire%20Festival%20A.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Yoshida Fire Festival' AND start_date = '2026-08-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nihyakutōka', '체험', '일본에서 열리는 축제', '2026-09-01', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nihyakutōka' AND start_date = '2026-09-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kaze no bon', '체험', '일본에서 열리는 축제', '2026-09-02', NULL, '도야마시', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kazenobon01.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kaze no bon' AND start_date = '2026-09-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tsuruga Matsuri', '체험', '일본에서 열리는 축제', '2026-09-04', NULL, 'Q28691812', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tsuruga Matsuri' AND start_date = '2026-09-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kakunodate Festival', '체험', '일본에서 열리는 축제', '2026-09-08', NULL, 'Kakunodate', 'http://commons.wikimedia.org/wiki/Special:FilePath/Kakunodate%20maturi%202008a.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kakunodate Festival' AND start_date = '2026-09-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nihyakuhatsuka', '체험', '일본에서 열리는 축제', '2026-09-11', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nihyakuhatsuka' AND start_date = '2026-09-11');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Iwashimizu-sai', '체험', '일본에서 열리는 축제', '2026-09-15', NULL, '이와시미즈 하치만궁', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Iwashimizu-sai' AND start_date = '2026-09-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Okuma Kabuto Festival', '체험', '일본에서 열리는 축제', '2026-09-20', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Okuma Kabuto Festival' AND start_date = '2026-09-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Tsukurimon Festival', '체험', '일본에서 열리는 축제', '2026-09-23', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/National%20Museum%20of%20Ethnology%2C%20Osaka%20-%20Ranry%C3%B4-%C3%B4%20statue%20made%20of%20vegetables%20-%20Festival%20%22Tsukurimon-matsuri%22%20-%20Takaoka%2C%20Toyama%20pref.%20-%20Collected%20in%202012.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Tsukurimon Festival' AND start_date = '2026-09-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sada Shin Noh', '체험', '일본에서 열리는 축제', '2026-09-25', NULL, 'Sada Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sada Shin Noh' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Miare Festival', '체험', '일본에서 열리는 축제', '2026-10-01', NULL, '겐카이나다', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%AE%97%E5%83%8F%E5%A4%A7%E7%A4%BE%E3%81%BF%E3%81%82%E3%82%8C%E7%A5%AD.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Miare Festival' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Yahagi Shrine Autumn Grand Festival', '체험', '일본에서 열리는 축제', '2026-10-01', NULL, 'Yahagi Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Yahagi-Jinja-1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Yahagi Shrine Autumn Grand Festival' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Sana Shrine', '체험', '일본에서 열리는 축제', '2026-10-08', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sana%20Shrine.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Sana Shrine' AND start_date = '2026-10-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nagasaki Kunchi', '체험', '일본에서 열리는 축제', '2026-10-09', NULL, '스와 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Jaodori%20of%20Nagasaki%20Kunchi.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nagasaki Kunchi' AND start_date = '2026-10-09');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kumano Hayatama Matsuri', '체험', '일본에서 열리는 축제', '2026-10-15', NULL, 'Kumano Hayatama Taisha', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%BE%A1%E8%88%B9%E7%A5%AD%20%E6%97%A9%E8%88%B9.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kumano Hayatama Matsuri' AND start_date = '2026-10-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Nada no kenka matsuri', '체험', '일본에서 열리는 축제', '2026-10-15', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Nada%20no%20Kenka%20matsuri%2004.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Nada no kenka matsuri' AND start_date = '2026-10-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Inbe Shrine (Matsue City)', '체험', '일본에서 열리는 축제', '2026-10-19', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E5%BF%8C%E9%83%A8%E7%A5%9E%E7%A4%BE.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Inbe Shrine (Matsue City)' AND start_date = '2026-10-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Katsuyama Festival', '체험', '일본에서 열리는 축제', '2026-10-20', NULL, '마니와시', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Katsuyama Festival' AND start_date = '2026-10-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kurama Himatsuri', '체험', '일본에서 열리는 축제', '2026-10-22', NULL, 'Yuki Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/%E9%9E%8D%E9%A6%AC%E3%81%AE%E7%81%AB%E7%A5%AD4.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kurama Himatsuri' AND start_date = '2026-10-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '지다이마쓰리', '공연', '일본에서 열리는 축제', '2026-10-22', NULL, '교토시', 'http://commons.wikimedia.org/wiki/Special:FilePath/JidaiMatsuri%20Gohouren.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '지다이마쓰리' AND start_date = '2026-10-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Hakata Okunchi', '체험', '일본에서 열리는 축제', '2026-10-24', NULL, '구시다 신사', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Hakata Okunchi' AND start_date = '2026-10-24');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Ueno Tenjin Festival', '체험', '일본에서 열리는 축제', '2026-10-25', NULL, 'Sugawara Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/Iga%20City%20Danjiri%20Kaikan%20ac.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Ueno Tenjin Festival' AND start_date = '2026-10-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Miyazaki Shrine Grand Festival', '체험', '일본에서 열리는 축제', '2026-10-26', NULL, '미야자키 신궁', 'http://commons.wikimedia.org/wiki/Special:FilePath/Miyazaki%20Shrine%20Grand%20Festival%20in%202008%20Gohouren%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Miyazaki Shrine Grand Festival' AND start_date = '2026-10-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '가라쓰쿤치', '체험', '일본에서 열리는 축제', '2026-11-04', NULL, '가라쓰 신사', 'http://commons.wikimedia.org/wiki/Special:FilePath/Hikiyama.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '가라쓰쿤치' AND start_date = '2026-11-04');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Fujinomiya Festival', '체험', '일본에서 열리는 축제', '2026-11-05', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Fujinomiya Festival' AND start_date = '2026-11-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', '시치고산', '체험', '일본의 연중 행사', '2026-11-15', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Brooklyn%20Museum%20-%20Dressing%20a%20Boy%20on%20the%20Occasion%20of%20His%20First%20Letting%20His%20Hair%20Grow%20-%20Kitagawa%20Utamaro%20-%20overall.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = '시치고산' AND start_date = '2026-11-15');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Mita festival', '체험', '일본에서 열리는 축제', '2026-11-23', NULL, '미나토구', 'http://commons.wikimedia.org/wiki/Special:FilePath/Mita%20Festival%2C%20Keio%20University%20-%20Nov%2025%2C%202007%20%281%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Mita festival' AND start_date = '2026-11-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Yatsushiro Myōken Festival', '체험', '일본에서 열리는 축제', '2026-11-23', NULL, 'Yatsushiro Shrine', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Yatsushiro Myōken Festival' AND start_date = '2026-11-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Chichibu Night Festival', '체험', '일본에서 열리는 축제', '2026-12-02', NULL, 'Chichibu Shrine', 'http://commons.wikimedia.org/wiki/Special:FilePath/ChichibuFes1.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Chichibu Night Festival' AND start_date = '2026-12-02');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Oku-noto no Aenokoto', '체험', '일본에서 열리는 축제', '2026-12-05', NULL, '오쿠노토', 'http://commons.wikimedia.org/wiki/Special:FilePath/Oku-noto%20no%20Aenokoto%2C%20offering%20meals%20to%20the%20deities.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Oku-noto no Aenokoto' AND start_date = '2026-12-05');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kanji Day', '체험', '일본에서 열리는 축제', '2026-12-12', NULL, '기요미즈데라', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kanji Day' AND start_date = '2026-12-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Akō Gishi Festival', '체험', '일본에서 열리는 축제', '2026-12-14', NULL, '일본', 'http://commons.wikimedia.org/wiki/Special:FilePath/Ako%20Gishisai%20De09%2013.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Akō Gishi Festival' AND start_date = '2026-12-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Gishi-sai', '체험', '일본에서 열리는 축제', '2026-12-14', NULL, '센가쿠지', 'http://commons.wikimedia.org/wiki/Special:FilePath/Sengakuji%20Gishisai%20191214e.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Gishi-sai' AND start_date = '2026-12-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Kasuga Wakamiya On-Matsuri Festival', '체험', '일본에서 열리는 축제', '2026-12-17', NULL, '가스가타이샤', 'http://commons.wikimedia.org/wiki/Special:FilePath/Motonobu%20Nakagawa%2C%20Mayor%20of%20Nara.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Kasuga Wakamiya On-Matsuri Festival' AND start_date = '2026-12-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '일본', 'Emperor Taishō Festival', '체험', '일본에서 열리는 축제', '2026-12-25', NULL, '일본', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '일본' AND title = 'Emperor Taishō Festival' AND start_date = '2026-12-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '조지아', 'Didgoroba', '체험', '조지아에서 열리는 축제', '2026-08-12', NULL, '조지아', 'http://commons.wikimedia.org/wiki/Special:FilePath/Didgoroba%202012%20%282%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '조지아' AND title = 'Didgoroba' AND start_date = '2026-08-12');

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
SELECT '중국', 'Chinese Doctor\'s Day', '체험', '중국에서 열리는 축제', '2026-08-19', NULL, '중국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = 'Chinese Doctor\'s Day' AND start_date = '2026-08-19');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', '중추절', '체험', '중국에서 열리는 축제', '2026-09-25', NULL, '중국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Montreal%20JBotanique1%20tango7174.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = '중추절' AND start_date = '2026-09-25');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '중국', 'hanyijie', '체험', '중국에서 열리는 축제', '2026-10-01', NULL, '중국', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '중국' AND title = 'hanyijie' AND start_date = '2026-10-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '케냐', 'Jamhuri Day', '체험', '케냐에서 열리는 축제', '2026-12-12', NULL, 'Kenya (1963–1964)', 'http://commons.wikimedia.org/wiki/Special:FilePath/March%20Defenders%20of%20Ukraine%20on%20Independence%20Day%20in%20Kyiv%2C%202021%20106.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '케냐' AND title = 'Jamhuri Day' AND start_date = '2026-12-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '크로아티아', 'Croatian Book Day', '체험', '크로아티아에서 열리는 축제', '2026-04-22', NULL, '크로아티아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '크로아티아' AND title = 'Croatian Book Day' AND start_date = '2026-04-22');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '크로아티아', 'Book Night', '체험', '크로아티아에서 열리는 축제', '2026-04-23', NULL, '크로아티아', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '크로아티아' AND title = 'Book Night' AND start_date = '2026-04-23');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '터키', 'Cabotage Day', '체험', '터키에서 열리는 축제', '2026-07-01', NULL, '터키', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '터키' AND title = 'Cabotage Day' AND start_date = '2026-07-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '파나마', 'Panamanian Carnival', '공연', '파나마에서 열리는 축제', '2026-02-16', NULL, '파나마시티', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '파나마' AND title = 'Panamanian Carnival' AND start_date = '2026-02-16');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', 'Carnival of Lazarim', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '포르투갈', 'http://commons.wikimedia.org/wiki/Special:FilePath/Caretos.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = 'Carnival of Lazarim' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', 'Carnival of Madeira', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '푼샬', 'http://commons.wikimedia.org/wiki/Special:FilePath/Funchal%2C%20Cortejo%20Aleg%C3%B3rico%20de%20Carnaval%20%282026%29%2006.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = 'Carnival of Madeira' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', 'Carnival of Podence', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, 'Podence', 'http://commons.wikimedia.org/wiki/Special:FilePath/Carnaval%20de%20Podence%202011%2001.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = 'Carnival of Podence' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '포르투갈', 'Torres Vedras\' Carnival', '공연', '포르투갈에서 열리는 축제', '2026-02-17', NULL, '포르투갈', 'http://commons.wikimedia.org/wiki/Special:FilePath/The%20Shinning%20Portuguese%20Mardi%20Gr%C3%A1s%20%28120757887%29.jpeg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '포르투갈' AND title = 'Torres Vedras\' Carnival' AND start_date = '2026-02-17');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', 'Festival du Grand Fauconnier', '체험', '프랑스에서 열리는 축제', '2026-07-14', NULL, '프랑스', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = 'Festival du Grand Fauconnier' AND start_date = '2026-07-14');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '프랑스', 'Festival of Lights', '체험', '프랑스에서 열리는 축제', '2026-12-08', NULL, '리옹', 'http://commons.wikimedia.org/wiki/Special:FilePath/F%C3%AAte%20des%20Lumi%C3%A8res%20%28Lyon%2C%202024%2C%20place%20Bellecour%29.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '프랑스' AND title = 'Festival of Lights' AND start_date = '2026-12-08');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '핀란드', 'Helsinki Day', '체험', '핀란드에서 열리는 축제', '2026-06-12', NULL, '핀란드', 'http://commons.wikimedia.org/wiki/Special:FilePath/Aerial%20photograph%20of%20Helsinki%20downtown.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '핀란드' AND title = 'Helsinki Day' AND start_date = '2026-06-12');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '땅끝해넘이해맞이축제', '체험', '대한민국 전라남도 해남군 땅끝마을에서 매년 열리는 해넘이·해맞이 축제', '2026-01-01', NULL, '땅끝마을', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '땅끝해넘이해맞이축제' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '한국', '완도 해맞이 행사', '체험', '한국에서 열리는 축제', '2026-01-01', NULL, '한국', 'http://commons.wikimedia.org/wiki/Special:FilePath/Wando%20sunrise.jpg'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '한국' AND title = '완도 해맞이 행사' AND start_date = '2026-01-01');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '헝가리', 'Debrecen Flower Carnival', '공연', '헝가리에서 열리는 축제', '2026-08-20', NULL, '헝가리', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '헝가리' AND title = 'Debrecen Flower Carnival' AND start_date = '2026-08-20');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', 'Australia Day Live Concert', '공연', '호주에서 열리는 축제', '2026-01-26', NULL, '호주', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = 'Australia Day Live Concert' AND start_date = '2026-01-26');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', 'Oranje Fest', '체험', '호주에서 열리는 축제', '2026-04-27', NULL, '호주', 'http://commons.wikimedia.org/wiki/Special:FilePath/Netherlands-Australia%2002.JPG'
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = 'Oranje Fest' AND start_date = '2026-04-27');

INSERT INTO festival (country_name, title, category, description, start_date, start_time, location, image_url)
SELECT '호주', 'NYE In The Park', '공연', '호주에서 열리는 축제', '2026-12-31', NULL, 'Victoria Park', NULL
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM festival WHERE country_name = '호주' AND title = 'NYE In The Park' AND start_date = '2026-12-31');
