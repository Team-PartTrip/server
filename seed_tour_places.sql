-- OpenStreetMap(Overpass API) 공개 데이터로 자동 생성된 tour_place 시드 데이터
-- 출처: © OpenStreetMap contributors, ODbL 라이선스
-- 같은 나라/도시/장소명이 이미 있으면 건너뛰므로 여러 번 실행해도 안전합니다.
-- rating, image_url 은 OSM 에 없는 값이라 NULL 입니다.

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '카니도라쿠',
       'RESTAURANT', '오사카의 맛집 · seafood 요리',
       '오사카', NULL, NULL,
       34.6688634, 135.5015017
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '카니도라쿠'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '카트리나',
       'RESTAURANT', '오사카의 맛집',
       '오사카', NULL, NULL,
       34.664621, 135.503933
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '카트리나'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Saizeriya',
       'RESTAURANT', '오사카의 맛집 · italian 요리',
       '大阪府 大阪市 中央区 17', NULL, NULL,
       34.6667786, 135.5035016
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Saizeriya'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Yukari',
       'RESTAURANT', '오사카의 맛집 · japanese 요리',
       '오사카', NULL, NULL,
       34.7009231, 135.5004866
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Yukari'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Gyoza no Ohsho',
       'RESTAURANT', '오사카의 맛집 · chinese 요리',
       '오사카', NULL, NULL,
       34.7028109, 135.502441
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Gyoza no Ohsho'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Gankozushi',
       'RESTAURANT', '오사카의 맛집 · sushi 요리',
       '오사카', NULL, NULL,
       34.6685903, 135.5019452
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Gankozushi'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'SATO',
       'RESTAURANT', '오사카의 맛집 · japanese 요리',
       '오사카', NULL, NULL,
       34.6544726, 135.5327073
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'SATO'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'CoCo ICHIBANYA',
       'RESTAURANT', '오사카의 맛집 · curry 요리',
       '오사카', NULL, NULL,
       34.7057401, 135.497523
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'CoCo ICHIBANYA'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hakurien',
       'RESTAURANT', '오사카의 맛집 · chinese 요리',
       '오사카', NULL, NULL,
       34.688944, 135.5064265
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hakurien'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Osaka Ohsho',
       'RESTAURANT', '오사카의 맛집 · japanese 요리',
       '오사카', NULL, NULL,
       34.7096465, 135.5105862
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Osaka Ohsho'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Yayoiken',
       'RESTAURANT', '오사카의 맛집 · japanese 요리',
       '오사카', NULL, NULL,
       34.6673923, 135.5062872
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Yayoiken'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hanamarūdon',
       'RESTAURANT', '오사카의 맛집 · japanese 요리',
       '오사카', NULL, NULL,
       34.7003851, 135.5115054
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hanamarūdon'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '오사카성',
       'ATTRACTION', '오사카의 명소',
       '大阪府 大阪市 中央区 1', NULL, NULL,
       34.6873727, 135.5258547
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '오사카성'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '오사카 역사박물관',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6826183, 135.5208131
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '오사카 역사박물관'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '오사카 코리아타운',
       'ATTRACTION', '오사카의 명소',
       '大阪市生野区 生野区', NULL, NULL,
       34.6605981, 135.537049
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '오사카 코리아타운'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '도톤보리 글리코 사인',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.668926, 135.5010546
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '도톤보리 글리코 사인'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '아메리카무라',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6720061, 135.4988014
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '아메리카무라'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '신세카이',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6520901, 135.5061908
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '신세카이'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Abeno Harukas Art Museum',
       'ATTRACTION', '오사카의 명소',
       '大阪府 大阪市 阿倍野区 24', NULL, NULL,
       34.6458431, 135.5131689
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Abeno Harukas Art Museum'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Mint Museum',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6958942, 135.5216842
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Mint Museum'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Osaka Castle Museum',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6874875, 135.5258873
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Osaka Castle Museum'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Kusuri Doshucho Museum',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6886356, 135.5059954
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Kusuri Doshucho Museum'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Osaka Contemporary Art Center',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6839942, 135.5180899
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Osaka Contemporary Art Center'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Osaka Prefectural Museum of Kamigata Comedy and Performing Arts',
       'ATTRACTION', '오사카의 명소',
       '오사카', NULL, NULL,
       34.6646523, 135.5031573
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Osaka Prefectural Museum of Kamigata Comedy and Performing Arts'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Osaka Marriott Miyako',
       'ACCOMMODATION', '오사카의 숙소 · 5성급',
       '大阪府 大阪市 阿倍野区 43', NULL, NULL,
       34.6461111, 135.5132439
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Osaka Marriott Miyako'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hotel Granvia Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.7016138, 135.4959249
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hotel Granvia Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Sheraton Miyako Hotel Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.6657616, 135.5205572
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Sheraton Miyako Hotel Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'DoubleTree by Hilton Osaka Castle',
       'ACCOMMODATION', '오사카의 숙소 · 4성급',
       '大阪府 大阪市 Chuo 1', NULL, NULL,
       34.6906022, 135.5220421
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'DoubleTree by Hilton Osaka Castle'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hotel Hankyu Gran Respire Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.7016577, 135.491778
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hotel Hankyu Gran Respire Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'The Ritz Carlton Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.6983885, 135.4925555
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'The Ritz Carlton Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hotel Hankyu International',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.7086708, 135.4984014
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hotel Hankyu International'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'InterContinental Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.7065249, 135.494557
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'InterContinental Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Comfort Hotel Osaka Sinsaibashi',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.6721369, 135.5035552
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Comfort Hotel Osaka Sinsaibashi'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'The Westin Osaka',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.7048821, 135.4895414
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'The Westin Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Conrad Osaka',
       'ACCOMMODATION', '오사카의 숙소 · 5성급',
       '大阪府 大阪市 北区 4', NULL, NULL,
       34.6935934, 135.4954682
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Conrad Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'セントレジスホテル大阪',
       'ACCOMMODATION', '오사카의 숙소',
       '오사카', NULL, NULL,
       34.6832856, 135.5012755
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'セントレジスホテル大阪'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '우에시마커피(한큐3번가 남관 지하1층)',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '오사카', NULL, NULL,
       34.70542, 135.4984652
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '우에시마커피(한큐3번가 남관 지하1층)'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '우에시마커피(신사이바시점)',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '오사카', NULL, NULL,
       34.6777078, 135.5015568
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '우에시마커피(신사이바시점)'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Starbucks',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '오사카', NULL, NULL,
       34.6685354, 135.5014079
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Starbucks'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'PRONTO',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '大阪市中央区城見2-1-6', NULL, NULL,
       34.6925439, 135.5316856
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'PRONTO'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Doutor Coffee Shop',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '大阪府大阪市 中央区心斎橋筋１丁目 ２−１６', NULL, NULL,
       34.6745611, 135.5014604
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Doutor Coffee Shop'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Salon de AmanTo',
       'CAFE', '오사카의 카페',
       '大阪市 北区 26', NULL, NULL,
       34.7080618, 135.5043734
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Salon de AmanTo'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Gloria Jean\'s Coffees',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '大阪市中央区城見1-3-7 城見通', NULL, NULL,
       34.6917585, 135.5305354
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Gloria Jean\'s Coffees'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Good Smile × Animate Cafe',
       'CAFE', '오사카의 카페',
       '大阪府 大阪市 浪速区 17', NULL, NULL,
       34.6607641, 135.5052031
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Good Smile × Animate Cafe'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Komeda\'s Coffee',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '오사카', NULL, NULL,
       34.7087246, 135.5110006
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Komeda\'s Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Junkissa American',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '大阪府 大阪市 中央区', NULL, NULL,
       34.6684, 135.503062
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Junkissa American'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Seattle\'s Best Coffee',
       'CAFE', '오사카의 카페',
       '大阪市中央区城見1-4-27 城見通', NULL, NULL,
       34.6919691, 135.5335301
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Seattle\'s Best Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Tully\'s Coffee',
       'CAFE', '오사카의 카페 · coffee shop 요리',
       '오사카', NULL, NULL,
       34.682449, 135.506656
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Tully\'s Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '오사카 성 공원',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6865762, 135.5272524
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '오사카 성 공원'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '나카노시마 공원',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6924226, 135.5077718
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '나카노시마 공원'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Kids Plaza Osaka',
       'ACTIVITY', '오사카의 액티비티',
       '大阪府 大阪市 北区 7', NULL, NULL,
       34.7045253, 135.5102709
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Kids Plaza Osaka'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Tennoji Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6508412, 135.5098665
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Tennoji Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Minami-Temma Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.691977, 135.5146066
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Minami-Temma Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Naniwa Palace Site',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.679808, 135.5228386
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Naniwa Palace Site'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Shimo-fukushima Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.690485, 135.4827236
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Shimo-fukushima Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Ōgimachi Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.7041306, 135.5091485
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Ōgimachi Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Sanadayama Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6699428, 135.5285788
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Sanadayama Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Nishi-Umeda Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6975469, 135.4911199
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Nishi-Umeda Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '浦江公園',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.7012755, 135.4835202
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '浦江公園'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Horie Park',
       'ACTIVITY', '오사카의 액티비티',
       '오사카', NULL, NULL,
       34.6722106, 135.4947408
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Horie Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '한큐 백화점 우메다 본점',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.7027128, 135.4984322
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '한큐 백화점 우메다 본점'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '케이한 시티 몰',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6900304, 135.5167195
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '케이한 시티 몰'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Takashimaya',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6646286, 135.5017961
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Takashimaya'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Daimaru',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.7019921, 135.4967271
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Daimaru'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Hankyu San Bangai South',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.7048749, 135.4984706
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Hankyu San Bangai South'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Crysta Nagahori',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6751603, 135.5027884
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Crysta Nagahori'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Namba City',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6639037, 135.5015067
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Namba City'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '京阪モール',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6972438, 135.5325453
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '京阪モール'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '上本町YUFURA',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6647838, 135.5194154
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '上本町YUFURA'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Namba Marui',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6655694, 135.5010595
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Namba Marui'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', 'Kyobashi Coms Garden',
       'SHOPPING', '오사카의 쇼핑',
       '오사카', NULL, NULL,
       34.6975428, 135.5310626
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = 'Kyobashi Coms Garden'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '일본', '오사카', '近鉄百貨店',
       'SHOPPING', '오사카의 쇼핑',
       '大阪府 大阪市 阿倍野区 43', NULL, NULL,
       34.6458383, 135.5136242
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '일본' AND city_name = '오사카' AND place_name = '近鉄百貨店'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Sirocco',
       'RESTAURANT', '방콕의 맛집 · mediterranean 요리',
       '방콕 Silom Road 1055', NULL, NULL,
       13.7212917, 100.5168726
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Sirocco'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Le Normandie',
       'RESTAURANT', '방콕의 맛집 · french 요리 · 2성급',
       '방콕 Oriental Avenue 48', NULL, NULL,
       13.7235075, 100.51395
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Le Normandie'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '팁사마이 팟타이',
       'RESTAURANT', '방콕의 맛집 · thai 요리',
       '방콕', NULL, NULL,
       13.7527631, 100.5048237
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '팁사마이 팟타이'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Savoey Restaurant',
       'RESTAURANT', '방콕의 맛집 · thai 요리',
       'กรุงเทพมหานคร 방콕 Ploenchit Rd 540', NULL, NULL,
       13.7436559, 100.5441361
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Savoey Restaurant'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'MK Restaurants',
       'RESTAURANT', '방콕의 맛집 · thai 요리',
       '방콕', NULL, NULL,
       13.7437059, 100.5015832
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'MK Restaurants'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Karim Roti-Mataba',
       'RESTAURANT', '방콕의 맛집',
       '방콕', NULL, NULL,
       13.7636489, 100.4957444
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Karim Roti-Mataba'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Fuji',
       'RESTAURANT', '방콕의 맛집 · japanese 요리',
       '방콕', NULL, NULL,
       13.779768, 100.5450323
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Fuji'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Eat Me Restaurant',
       'RESTAURANT', '방콕의 맛집 · international 요리',
       'กรุงเทพมหานคร 방콕 Soi Phipat 2', NULL, NULL,
       13.7254794, 100.5338981
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Eat Me Restaurant'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Vertigo Moon Bar',
       'RESTAURANT', '방콕의 맛집',
       '방콕', NULL, NULL,
       13.7234481, 100.5396902
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Vertigo Moon Bar'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Spaghetti Factory',
       'RESTAURANT', '방콕의 맛집 · pasta, pizza 요리',
       '방콕', NULL, NULL,
       13.7590474, 100.5660152
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Spaghetti Factory'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Madame Masur',
       'RESTAURANT', '방콕의 맛집',
       '방콕', NULL, NULL,
       13.7630083, 100.4992744
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Madame Masur'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Krua Khun Kung',
       'RESTAURANT', '방콕의 맛집 · thai 요리',
       '방콕 Thanon Maharat 77', NULL, NULL,
       13.7521124, 100.488419
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Krua Khun Kung'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '방콕 왕궁',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7495708, 100.4917924
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '방콕 왕궁'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '전승기념탑',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7649304, 100.5382855
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '전승기념탑'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '민주기념탑',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7566864, 100.5018398
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '민주기념탑'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Devavesm Palace',
       'ATTRACTION', '방콕의 명소',
       'กรุงเทพมหานคร 방콕 พระนคร', NULL, NULL,
       13.7698042, 100.5002903
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Devavesm Palace'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Wang Derm Palace',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.742463, 100.4897263
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Wang Derm Palace'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Ananta Samakhom Throne Hall',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7717072, 100.5131023
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Ananta Samakhom Throne Hall'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Golden Mount',
       'ATTRACTION', '방콕의 명소',
       'กรุงเทพมหานคร 방콕 ป้อมปราบศัตรูพ่าย ถนนบริพัตร 344', NULL, NULL,
       13.7538574, 100.5066911
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Golden Mount'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Bank of Thailand Museum',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7682842, 100.4995365
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Bank of Thailand Museum'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Suan Pakkad Palace',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7568308, 100.537096
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Suan Pakkad Palace'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Dusit Maha Prasat Hall',
       'ATTRACTION', '방콕의 명소',
       'กรุงเทพมหานคร 방콕 พระนคร', NULL, NULL,
       13.7501601, 100.4905116
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Dusit Maha Prasat Hall'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Ban Chao Phraya',
       'ATTRACTION', '방콕의 명소',
       'กรุงเทพมหานคร 방콕 พระนคร ถนนพระอาทิตย์ 49/1', NULL, NULL,
       13.7634543, 100.4948943
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Ban Chao Phraya'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Chitralada Royal Villa',
       'ATTRACTION', '방콕의 명소',
       '방콕', NULL, NULL,
       13.7683267, 100.5206063
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Chitralada Royal Villa'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Centara Grand',
       'ACCOMMODATION', '방콕의 숙소',
       '방콕', NULL, NULL,
       13.7475234, 100.538361
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Centara Grand'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'lebua at State Tower',
       'ACCOMMODATION', '방콕의 숙소',
       'แขวงสุริยวงศ์ ถนนสีลม 1055 State Tower', NULL, NULL,
       13.7215136, 100.516885
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'lebua at State Tower'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '반얀트리호텔 방콕',
       'ACCOMMODATION', '방콕의 숙소',
       '방콕 Thanon Sathon Tai 21', NULL, NULL,
       13.7235907, 100.5397921
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '반얀트리호텔 방콕'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Conrad Bangkok Residences',
       'ACCOMMODATION', '방콕의 숙소 · 5성급',
       'กรุงเทพมหานคร 방콕 ปทุมวัน ถนนวิทยุ 87/3', NULL, NULL,
       13.7387706, 100.5486207
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Conrad Bangkok Residences'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'The Okura Prestige Bangkok Hotel',
       'ACCOMMODATION', '방콕의 숙소 · 5성급',
       '방콕', NULL, NULL,
       13.7426085, 100.5476474
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'The Okura Prestige Bangkok Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Hilton Garden Inn Bangkok Riverside',
       'ACCOMMODATION', '방콕의 숙소',
       'กรุงเทพมหานคร 방콕 คลองสาน ถนนเจริญนคร 168', NULL, NULL,
       13.7263392, 100.5086736
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Hilton Garden Inn Bangkok Riverside'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '터미널 21',
       'ACCOMMODATION', '방콕의 숙소',
       '방콕', NULL, NULL,
       13.738186, 100.5606839
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '터미널 21'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Hyatt Regency Bangkok Sukhumvit',
       'ACCOMMODATION', '방콕의 숙소 · 5성급',
       '방콕 Soi Sukumvit 13 1', NULL, NULL,
       13.7399072, 100.5571624
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Hyatt Regency Bangkok Sukhumvit'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Top High Airport Link Hotel',
       'ACCOMMODATION', '방콕의 숙소 · 3성급',
       '방콕 Phetchaburi 11 33', NULL, NULL,
       13.7560029, 100.5365196
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Top High Airport Link Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Ruamchitt Plaza Hotel',
       'ACCOMMODATION', '방콕의 숙소 · 3성급',
       '방콕', NULL, NULL,
       13.739002, 100.5579797
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Ruamchitt Plaza Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'The Krungkasem Srikrung Hotel',
       'ACCOMMODATION', '방콕의 숙소',
       '방콕', NULL, NULL,
       13.7389422, 100.5153219
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'The Krungkasem Srikrung Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Tara place',
       'ACCOMMODATION', '방콕의 숙소',
       '방콕 113,117 Samsen road , phranakorn district Bangkok 10200', NULL, NULL,
       13.7644516, 100.4994273
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Tara place'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'BKK Bagel Bakery',
       'CAFE', '방콕의 카페 · bagel 요리',
       '방콕', NULL, NULL,
       13.7438912, 100.5420395
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'BKK Bagel Bakery'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Starbucks Coffee',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕 Sukhumvit 24 88', NULL, NULL,
       13.7219075, 100.5662462
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Starbucks Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Starbucks',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕', NULL, NULL,
       13.7295953, 100.5691937
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Starbucks'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Café Amazon',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕', NULL, NULL,
       13.7702933, 100.5045164
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Café Amazon'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Pablo Cheese Tarts!!',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕', NULL, NULL,
       13.7463316, 100.5341278
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Pablo Cheese Tarts!!'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'So Heng Thai cafe',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕', NULL, NULL,
       13.7336284, 100.5119772
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'So Heng Thai cafe'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Ariya',
       'CAFE', '방콕의 카페',
       '방콕 2 flour', NULL, NULL,
       13.7256165, 100.5445383
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Ariya'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'machi machi',
       'CAFE', '방콕의 카페 · bubble tea 요리',
       '방콕 ถนนพิษณุโลก 3/5', NULL, NULL,
       13.7706821, 100.5050652
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'machi machi'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Gallery Drip Coffee',
       'CAFE', '방콕의 카페 · coffee shop 요리',
       '방콕', NULL, NULL,
       13.7466157, 100.5303194
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Gallery Drip Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Floral Cafe',
       'CAFE', '방콕의 카페 · tea 요리',
       'กรุงเทพมหานคร 방콕 พระนคร Chakkraphet Rd, 67', NULL, NULL,
       13.742236, 100.496746
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Floral Cafe'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Carousel Coffee',
       'CAFE', '방콕의 카페 · coffee 요리',
       '방콕', NULL, NULL,
       13.7267402, 100.4938058
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Carousel Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Coffee Beans by Dao',
       'CAFE', '방콕의 카페 · thai 요리',
       '방콕', NULL, NULL,
       13.7466547, 100.5345033
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Coffee Beans by Dao'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '산티차이프라칸 공원',
       'ACTIVITY', '방콕의 액티비티',
       'กรุงเทพมหานคร 방콕 พระนคร ถนนพระอาทิตย์', NULL, NULL,
       13.7640964, 100.4954852
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '산티차이프라칸 공원'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '벤짜키티 공원',
       'ACTIVITY', '방콕의 액티비티',
       '방콕 ถนนรัชดาภิเษก', NULL, NULL,
       13.7293437, 100.5586599
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '벤짜키티 공원'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '청논시운하 공원',
       'ACTIVITY', '방콕의 액티비티',
       '방콕', NULL, NULL,
       13.7206303, 100.5308825
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '청논시운하 공원'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Saranrom Park',
       'ACTIVITY', '방콕의 액티비티',
       'กรุงเทพมหานคร 방콕 พระนคร ถนนสนามไชย', NULL, NULL,
       13.7483473, 100.4952191
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Saranrom Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Santi Phap Park',
       'ACTIVITY', '방콕의 액티비티',
       'กรุงเทพมหานคร 방콕 ราชเทวี ถนนราชวิถี', NULL, NULL,
       13.7616096, 100.5407085
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Santi Phap Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Lumphini Park',
       'ACTIVITY', '방콕의 액티비티',
       'แขวงลุมพินี ถนนวิทยุ 192', NULL, NULL,
       13.7304754, 100.5416508
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Lumphini Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Suan Luang Rama VIII Park',
       'ACTIVITY', '방콕의 액티비티',
       '방콕', NULL, NULL,
       13.7684419, 100.4948375
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Suan Luang Rama VIII Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Benchakitti Forest Park',
       'ACTIVITY', '방콕의 액티비티',
       'กรุงเทพมหานคร 방콕 คลองเตย ถนนรัชดาภิเษก', NULL, NULL,
       13.7295625, 100.5546105
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Benchakitti Forest Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Sanam Luang',
       'ACTIVITY', '방콕의 액티비티',
       '방콕', NULL, NULL,
       13.7551616, 100.4930693
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Sanam Luang'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Pathumwananurak Park',
       'ACTIVITY', '방콕의 액티비티',
       'แขวงปทุมวัน ถนนราชดำริ 5/1-5/40', NULL, NULL,
       13.7486777, 100.5386889
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Pathumwananurak Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Chulalongkorn University Centennial Park',
       'ACTIVITY', '방콕의 액티비티',
       '방콕', NULL, NULL,
       13.739626, 100.5238071
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Chulalongkorn University Centennial Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Maha Chetsadabodin Pavilion Court',
       'ACTIVITY', '방콕의 액티비티',
       '방콕', NULL, NULL,
       13.75556, 100.5047102
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Maha Chetsadabodin Pavilion Court'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Tokyu',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7458135, 100.5298492
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Tokyu'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', '쑥싸얌',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7262661, 100.5100157
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = '쑥싸얌'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Market Place Dusit',
       'SHOPPING', '방콕의 쇼핑',
       'แขวงวชิรพยาบาล ถนนราชวิถี 157 6', NULL, NULL,
       13.7790061, 100.5076603
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Market Place Dusit'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Life Center',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7256246, 100.544357
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Life Center'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Central Silom Complex',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7280144, 100.5352007
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Central Silom Complex'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Lam Zin Thai',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7414079, 100.5022804
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Lam Zin Thai'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Rain Hill',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7276907, 100.5741047
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Rain Hill'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Montien Mall',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7303351, 100.531636
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Montien Mall'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'MUJI',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7477393, 100.539592
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'MUJI'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Robinson',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.759384, 100.566171
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Robinson'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Groove @ CentralWorld',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.745438, 100.5382485
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Groove @ CentralWorld'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '태국', '방콕', 'Hug Thai Zone',
       'SHOPPING', '방콕의 쇼핑',
       '방콕', NULL, NULL,
       13.7469256, 100.538543
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '태국' AND city_name = '방콕' AND place_name = 'Hug Thai Zone'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '미꽝-쌀국수',
       'RESTAURANT', '다낭의 맛집 · vietnamese 요리',
       '다낭 Hải Phòng', NULL, NULL,
       16.0723814, 108.2191279
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '미꽝-쌀국수'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '반쎄오 바두엉',
       'RESTAURANT', '다낭의 맛집 · vietnamese 요리',
       '다낭 Kiệt 280 Hoàng Diệu 23', NULL, NULL,
       16.0588034, 108.2160349
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '반쎄오 바두엉'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '베안',
       'RESTAURANT', '다낭의 맛집 · seafood 요리',
       '다낭 Lô 14-15 Hồ Nghinh, Phước Mỹ, Sơn Trà, Đà Nẵng, 베트남', NULL, NULL,
       16.0692319, 108.2429913
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '베안'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '다낭 타워 스테이크 하우스',
       'RESTAURANT', '다낭의 맛집 · steak house 요리',
       '다낭 Đường Trần Hưng Đạo', NULL, NULL,
       16.0709643, 108.2292556
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '다낭 타워 스테이크 하우스'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '분짜까 109',
       'RESTAURANT', '다낭의 맛집 · vietnamese 요리',
       '다낭', NULL, NULL,
       16.0743887, 108.2207958
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '분짜까 109'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '째비엣',
       'RESTAURANT', '다낭의 맛집',
       '다낭', NULL, NULL,
       16.0662328, 108.2244753
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '째비엣'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '포 29',
       'RESTAURANT', '다낭의 맛집',
       '다낭', NULL, NULL,
       16.066153, 108.2227599
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '포 29'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Bikini Bottom',
       'RESTAURANT', '다낭의 맛집 · breakfast 요리',
       'Danang Đà Nẵng Ngũ Hành Sơn An Thuong 2 45-47', NULL, NULL,
       16.0486661, 108.2475717
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Bikini Bottom'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'KURUMI - Healthy Vegan Food & Desserts',
       'RESTAURANT', '다낭의 맛집 · breakfast 요리',
       'Da Nang Ngũ Hành Sơn Đường Mỹ Đa Đông 12 17/22', NULL, NULL,
       16.0457151, 108.2478678
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'KURUMI - Healthy Vegan Food & Desserts'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '람비엔',
       'RESTAURANT', '다낭의 맛집 · vietnamese 요리',
       '다낭', NULL, NULL,
       16.0422313, 108.2466636
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '람비엔'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'NEM 씨푸드 식당',
       'RESTAURANT', '다낭의 맛집 · seafood 요리',
       'Đà Nẵng Đà Nẵng Sơn Trà Võ Nguyên Giáp 216', NULL, NULL,
       16.0675122, 108.2449556
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'NEM 씨푸드 식당'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Thìa Gỗ Danang-style restaurant',
       'RESTAURANT', '다낭의 맛집 · vietnamese 요리',
       'Đà Nẵng Hải Châu Đường Ba Đình 37', NULL, NULL,
       16.0760522, 108.2183879
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Thìa Gỗ Danang-style restaurant'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '야시장',
       'ATTRACTION', '다낭의 명소',
       '다낭 Đường 2 Tháng 9', NULL, NULL,
       16.0370944, 108.2248186
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '야시장'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Dragon\'s Head',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0612476, 108.2296554
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Dragon\'s Head'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Tran Thi Ly Bridge',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.050179, 108.2293814
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Tran Thi Ly Bridge'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Tien Son Bridge',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0352694, 108.2352722
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Tien Son Bridge'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Han River Bridge',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0721398, 108.2268106
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Han River Bridge'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Old River Crossing Bridge',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0510888, 108.2292466
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Old River Crossing Bridge'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Fresco Village Da Nang',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0607097, 108.2200254
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Fresco Village Da Nang'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Queen Cobra',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0432987, 108.22599
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Queen Cobra'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Đài Tưởng niệm Liệt sỹ thành phố Đà Nẵng',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0409714, 108.2243231
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Đài Tưởng niệm Liệt sỹ thành phố Đà Nẵng'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Pont main',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0520775, 108.2156357
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Pont main'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Dragon\'s Tail',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.06108, 108.2248167
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Dragon\'s Tail'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Tượng Bồ Câu',
       'ATTRACTION', '다낭의 명소',
       '다낭', NULL, NULL,
       16.0823325, 108.2231946
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Tượng Bồ Câu'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '퓨전 스위트 다낭비치',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Đường Võ Nguyên Giáp 88', NULL, NULL,
       16.0810779, 108.24697
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '퓨전 스위트 다낭비치'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Tam House Villa Hotel',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 K209 Nguyễn Văn Thoại 27', NULL, NULL,
       16.057376, 108.2430631
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Tam House Villa Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Crystal Hotel',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Đường Hồ Nghinh 204-206', NULL, NULL,
       16.0668574, 108.2430295
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Crystal Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '4Seasons Hotel',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Nguyễn Hữu Thọ 194', NULL, NULL,
       16.0428944, 108.2100876
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '4Seasons Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Hilton Garden Inn Da Nang',
       'ACCOMMODATION', '다낭의 숙소',
       'Da Nang Son Tra Đường Võ Nguyên Giáp 96', NULL, NULL,
       16.0801031, 108.2464178
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Hilton Garden Inn Da Nang'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Nghe Home - Homestay Da Nang',
       'ACCOMMODATION', '다낭의 숙소',
       'Danang Đà Nẵng Ngu Hanh Son Đường An Thượng 4 14', NULL, NULL,
       16.0488836, 108.2463256
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Nghe Home - Homestay Da Nang'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Alani Da Nang Hotel',
       'ACCOMMODATION', '다낭의 숙소',
       'Hoi An Da Nang Ngu Hanh Son Đường Trần Bạch Đằng 134 -136', NULL, NULL,
       16.0543887, 108.2461266
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Alani Da Nang Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Cents Da Nang Hotel',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Cao Bá Quát 108', NULL, NULL,
       16.0628484, 108.2306558
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Cents Da Nang Hotel'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'RHM Luxury Hotel And Suite',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Đường An Tư Công Chúa 02', NULL, NULL,
       16.0370478, 108.2397901
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'RHM Luxury Hotel And Suite'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Salmalia Boutique Hotel & Spa',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Đường Lâm Hoành 52', NULL, NULL,
       16.0608098, 108.2452974
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Salmalia Boutique Hotel & Spa'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Park view',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭 Đường Phước Trường 15 38', NULL, NULL,
       16.0759788, 108.2418354
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Park view'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'SAMDI HOTEL',
       'ACCOMMODATION', '다낭의 숙소',
       '다낭', NULL, NULL,
       16.0593521, 108.2084405
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'SAMDI HOTEL'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Highlands Coffee',
       'CAFE', '다낭의 카페 · coffee shop 요리',
       '다낭', NULL, NULL,
       16.0700231, 108.2249183
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Highlands Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', '아지트',
       'CAFE', '다낭의 카페',
       '다낭', NULL, NULL,
       16.0792023, 108.2215011
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = '아지트'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Trung Nguyên Coffee',
       'CAFE', '다낭의 카페 · coffee shop 요리',
       '다낭', NULL, NULL,
       16.0311905, 108.2242256
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Trung Nguyên Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Trung Nguyen Coffee',
       'CAFE', '다낭의 카페 · coffee shop 요리',
       '다낭', NULL, NULL,
       16.0723003, 108.218556
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Trung Nguyen Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Long Coffee',
       'CAFE', '다낭의 카페 · coffee shop 요리',
       '다낭 Lê Lợi 123', NULL, NULL,
       16.0749597, 108.219974
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Long Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Trung Nguyên Legend',
       'CAFE', '다낭의 카페',
       '다낭', NULL, NULL,
       16.0649241, 108.2224339
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Trung Nguyên Legend'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Homie Coffee',
       'CAFE', '다낭의 카페',
       'Đà Nẵng Thanh Khê Hải Phòng 279', NULL, NULL,
       16.0707542, 108.2092543
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Homie Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'NhatHa Coffee Ice Cream',
       'CAFE', '다낭의 카페',
       '다낭 Đường Trưng Nữ Vương 130', NULL, NULL,
       16.058217, 108.2207447
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'NhatHa Coffee Ice Cream'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'CaPhe Huy',
       'CAFE', '다낭의 카페',
       '다낭 Đường Ông Ích Khiêm 80', NULL, NULL,
       16.0768325, 108.2120316
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'CaPhe Huy'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'CaPhe Giai Khat Xiromi',
       'CAFE', '다낭의 카페',
       '다낭 Đường Phạm Ngọc Thạch 81', NULL, NULL,
       16.0895782, 108.2169896
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'CaPhe Giai Khat Xiromi'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'PhoSao Coffee',
       'CAFE', '다낭의 카페',
       '다낭', NULL, NULL,
       16.0452824, 108.2227242
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'PhoSao Coffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'TigonCoffee',
       'CAFE', '다낭의 카페',
       '다낭', NULL, NULL,
       16.0463238, 108.2386833
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'TigonCoffee'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'East Sea Park',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0681469, 108.2459283
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'East Sea Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Trung tâm Giải trí Phức hợp Helio Center',
       'ACTIVITY', '다낭의 액티비티',
       'Đà Nẵng Hải Châu Đường 2 Tháng 9', NULL, NULL,
       16.0364559, 108.2246715
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Trung tâm Giải trí Phức hợp Helio Center'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'My Khe Beach Park',
       'ACTIVITY', '다낭의 액티비티',
       'Danang Đà Nẵng Ngũ Hành Sơn Đường Võ Nguyên Giáp 53', NULL, NULL,
       16.0505945, 108.2489008
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'My Khe Beach Park'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên Nguyễn Văn Linh',
       'ACTIVITY', '다낭의 액티비티',
       'Đà Nẵng Hải Châu Đường Nguyễn Văn Linh', NULL, NULL,
       16.0613021, 108.2204316
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên Nguyễn Văn Linh'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0683797, 108.2218507
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công Viên APEC',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0585958, 108.2232732
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công Viên APEC'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên Đầm Rong',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0835868, 108.2178648
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên Đầm Rong'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên Thanh Bình',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0794713, 108.2118706
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên Thanh Bình'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên Hà Thân',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0649082, 108.2318255
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên Hà Thân'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên An Hải',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0815021, 108.2303846
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên An Hải'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên An Nhơn',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0688547, 108.2330704
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên An Nhơn'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Công viên Hồ Nghinh',
       'ACTIVITY', '다낭의 액티비티',
       '다낭', NULL, NULL,
       16.0762001, 108.2424022
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Công viên Hồ Nghinh'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'Minimart',
       'SHOPPING', '다낭의 쇼핑',
       '다낭 Hà Bổng 64', NULL, NULL,
       16.0662104, 108.2446508
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'Minimart'
);

INSERT INTO tour_place
  (country_name, city_name, place_name, category, description,
   address, rating, image_url, latitude, longitude)
SELECT '베트남', '다낭', 'tattoo bon',
       'SHOPPING', '다낭의 쇼핑',
       '다낭', NULL, NULL,
       16.067829, 108.2015993
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM tour_place
  WHERE country_name = '베트남' AND city_name = '다낭' AND place_name = 'tattoo bon'
);
