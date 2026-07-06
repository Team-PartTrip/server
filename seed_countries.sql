-- 공개 국가 데이터(mledoze/countries + flagcdn.com)로 자동 생성된 시드 데이터
-- 이미 country_name이 존재하는 행은 건너뛰므로 여러 번 실행해도 안전합니다.

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아루바', 'Oranjestad', 'https://flagcdn.com/w320/aw.png', '아루바은(는) Caribbean에 위치한 나라로, 수도는 Oranjestad이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아루바');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아프가니스탄', 'Kabul', 'https://flagcdn.com/w320/af.png', '아프가니스탄은(는) Southern Asia에 위치한 나라로, 수도는 Kabul이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아프가니스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '앙골라', 'Luanda', 'https://flagcdn.com/w320/ao.png', '앙골라은(는) Middle Africa에 위치한 나라로, 수도는 Luanda이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '앙골라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '앵귈라', 'The Valley', 'https://flagcdn.com/w320/ai.png', '앵귈라은(는) Caribbean에 위치한 나라로, 수도는 The Valley이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '앵귈라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '올란드 제도', 'Mariehamn', 'https://flagcdn.com/w320/ax.png', '올란드 제도은(는) Northern Europe에 위치한 나라로, 수도는 Mariehamn이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '올란드 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '알바니아', 'Tirana', 'https://flagcdn.com/w320/al.png', '알바니아은(는) Southeast Europe에 위치한 나라로, 수도는 Tirana이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '알바니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '안도라', 'Andorra la Vella', 'https://flagcdn.com/w320/ad.png', '안도라은(는) Southern Europe에 위치한 나라로, 수도는 Andorra la Vella이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '안도라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아랍에미리트', 'Abu Dhabi', 'https://flagcdn.com/w320/ae.png', '아랍에미리트은(는) Western Asia에 위치한 나라로, 수도는 Abu Dhabi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아랍에미리트');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아르헨티나', 'Buenos Aires', 'https://flagcdn.com/w320/ar.png', '아르헨티나은(는) South America에 위치한 나라로, 수도는 Buenos Aires이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아르헨티나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아르메니아', 'Yerevan', 'https://flagcdn.com/w320/am.png', '아르메니아은(는) Western Asia에 위치한 나라로, 수도는 Yerevan이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아르메니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아메리칸사모아', 'Pago Pago', 'https://flagcdn.com/w320/as.png', '아메리칸사모아은(는) Polynesia에 위치한 나라로, 수도는 Pago Pago이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아메리칸사모아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '남극', '남극', 'https://flagcdn.com/w320/aq.png', '남극은(는) Antarctic에 위치한 나라로, 수도는 남극이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '남극');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '프랑스령 남부와 남극 지역', 'Port-aux-Français', 'https://flagcdn.com/w320/tf.png', '프랑스령 남부와 남극 지역은(는) Antarctic에 위치한 나라로, 수도는 Port-aux-Français이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '프랑스령 남부와 남극 지역');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '앤티가 바부다', 'Saint John\'s', 'https://flagcdn.com/w320/ag.png', '앤티가 바부다은(는) Caribbean에 위치한 나라로, 수도는 Saint John\'s이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '앤티가 바부다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '호주', 'Canberra', 'https://flagcdn.com/w320/au.png', '호주은(는) Australia and New Zealand에 위치한 나라로, 수도는 Canberra이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '호주');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '오스트리아', 'Vienna', 'https://flagcdn.com/w320/at.png', '오스트리아은(는) Central Europe에 위치한 나라로, 수도는 Vienna이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '오스트리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아제르바이잔', 'Baku', 'https://flagcdn.com/w320/az.png', '아제르바이잔은(는) Western Asia에 위치한 나라로, 수도는 Baku이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아제르바이잔');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '부룬디', 'Gitega', 'https://flagcdn.com/w320/bi.png', '부룬디은(는) Eastern Africa에 위치한 나라로, 수도는 Gitega이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '부룬디');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '벨기에', 'Brussels', 'https://flagcdn.com/w320/be.png', '벨기에은(는) Western Europe에 위치한 나라로, 수도는 Brussels이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '벨기에');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '베냉', 'Porto-Novo', 'https://flagcdn.com/w320/bj.png', '베냉은(는) Western Africa에 위치한 나라로, 수도는 Porto-Novo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '베냉');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '부르키나파소', 'Ouagadougou', 'https://flagcdn.com/w320/bf.png', '부르키나파소은(는) Western Africa에 위치한 나라로, 수도는 Ouagadougou이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '부르키나파소');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '방글라데시', 'Dhaka', 'https://flagcdn.com/w320/bd.png', '방글라데시은(는) Southern Asia에 위치한 나라로, 수도는 Dhaka이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '방글라데시');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '불가리아', 'Sofia', 'https://flagcdn.com/w320/bg.png', '불가리아은(는) Southeast Europe에 위치한 나라로, 수도는 Sofia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '불가리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '바레인', 'Manama', 'https://flagcdn.com/w320/bh.png', '바레인은(는) Western Asia에 위치한 나라로, 수도는 Manama이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '바레인');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '바하마', 'Nassau', 'https://flagcdn.com/w320/bs.png', '바하마은(는) Caribbean에 위치한 나라로, 수도는 Nassau이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '바하마');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '보스니아 헤르체고비나', 'Sarajevo', 'https://flagcdn.com/w320/ba.png', '보스니아 헤르체고비나은(는) Southeast Europe에 위치한 나라로, 수도는 Sarajevo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '보스니아 헤르체고비나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '생바르텔레미', 'Gustavia', 'https://flagcdn.com/w320/bl.png', '생바르텔레미은(는) Caribbean에 위치한 나라로, 수도는 Gustavia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '생바르텔레미');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세인트헬레나', 'Jamestown', 'https://flagcdn.com/w320/sh.png', '세인트헬레나은(는) Western Africa에 위치한 나라로, 수도는 Jamestown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세인트헬레나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '벨라루스', 'Minsk', 'https://flagcdn.com/w320/by.png', '벨라루스은(는) Eastern Europe에 위치한 나라로, 수도는 Minsk이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '벨라루스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '벨리즈', 'Belmopan', 'https://flagcdn.com/w320/bz.png', '벨리즈은(는) Central America에 위치한 나라로, 수도는 Belmopan이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '벨리즈');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '버뮤다', 'Hamilton', 'https://flagcdn.com/w320/bm.png', '버뮤다은(는) North America에 위치한 나라로, 수도는 Hamilton이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '버뮤다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '볼리비아', 'Sucre', 'https://flagcdn.com/w320/bo.png', '볼리비아은(는) South America에 위치한 나라로, 수도는 Sucre이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '볼리비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '카리브 네덜란드', 'Kralendijk', 'https://flagcdn.com/w320/bq.png', '카리브 네덜란드은(는) Caribbean에 위치한 나라로, 수도는 Kralendijk이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '카리브 네덜란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '브라질', 'Brasília', 'https://flagcdn.com/w320/br.png', '브라질은(는) South America에 위치한 나라로, 수도는 Brasília이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '브라질');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '바베이도스', 'Bridgetown', 'https://flagcdn.com/w320/bb.png', '바베이도스은(는) Caribbean에 위치한 나라로, 수도는 Bridgetown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '바베이도스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '브루나이', 'Bandar Seri Begawan', 'https://flagcdn.com/w320/bn.png', '브루나이은(는) South-Eastern Asia에 위치한 나라로, 수도는 Bandar Seri Begawan이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '브루나이');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '부탄', 'Thimphu', 'https://flagcdn.com/w320/bt.png', '부탄은(는) Southern Asia에 위치한 나라로, 수도는 Thimphu이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '부탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '부베 섬', '부베 섬', 'https://flagcdn.com/w320/bv.png', '부베 섬은(는) Antarctic에 위치한 나라로, 수도는 부베 섬이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '부베 섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '보츠와나', 'Gaborone', 'https://flagcdn.com/w320/bw.png', '보츠와나은(는) Southern Africa에 위치한 나라로, 수도는 Gaborone이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '보츠와나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '중앙아프리카 공화국', 'Bangui', 'https://flagcdn.com/w320/cf.png', '중앙아프리카 공화국은(는) Middle Africa에 위치한 나라로, 수도는 Bangui이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '중앙아프리카 공화국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '캐나다', 'Ottawa', 'https://flagcdn.com/w320/ca.png', '캐나다은(는) North America에 위치한 나라로, 수도는 Ottawa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '캐나다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '코코스 제도', 'West Island', 'https://flagcdn.com/w320/cc.png', '코코스 제도은(는) Australia and New Zealand에 위치한 나라로, 수도는 West Island이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '코코스 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '스위스', 'Bern', 'https://flagcdn.com/w320/ch.png', '스위스은(는) Western Europe에 위치한 나라로, 수도는 Bern이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '스위스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '칠레', 'Santiago', 'https://flagcdn.com/w320/cl.png', '칠레은(는) South America에 위치한 나라로, 수도는 Santiago이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '칠레');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '중국', 'Beijing', 'https://flagcdn.com/w320/cn.png', '중국은(는) Eastern Asia에 위치한 나라로, 수도는 Beijing이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '중국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '코트디부아르', 'Yamoussoukro', 'https://flagcdn.com/w320/ci.png', '코트디부아르은(는) Western Africa에 위치한 나라로, 수도는 Yamoussoukro이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '코트디부아르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '카메룬', 'Yaoundé', 'https://flagcdn.com/w320/cm.png', '카메룬은(는) Middle Africa에 위치한 나라로, 수도는 Yaoundé이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '카메룬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '콩고 민주 공화국', 'Kinshasa', 'https://flagcdn.com/w320/cd.png', '콩고 민주 공화국은(는) Middle Africa에 위치한 나라로, 수도는 Kinshasa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '콩고 민주 공화국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '콩고', 'Brazzaville', 'https://flagcdn.com/w320/cg.png', '콩고은(는) Middle Africa에 위치한 나라로, 수도는 Brazzaville이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '콩고');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '쿡 제도', 'Avarua', 'https://flagcdn.com/w320/ck.png', '쿡 제도은(는) Polynesia에 위치한 나라로, 수도는 Avarua이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '쿡 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '콜롬비아', 'Bogotá', 'https://flagcdn.com/w320/co.png', '콜롬비아은(는) South America에 위치한 나라로, 수도는 Bogotá이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '콜롬비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '코모로', 'Moroni', 'https://flagcdn.com/w320/km.png', '코모로은(는) Eastern Africa에 위치한 나라로, 수도는 Moroni이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '코모로');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '카보베르데', 'Praia', 'https://flagcdn.com/w320/cv.png', '카보베르데은(는) Western Africa에 위치한 나라로, 수도는 Praia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '카보베르데');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '코스타리카', 'San José', 'https://flagcdn.com/w320/cr.png', '코스타리카은(는) Central America에 위치한 나라로, 수도는 San José이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '코스타리카');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '쿠바', 'Havana', 'https://flagcdn.com/w320/cu.png', '쿠바은(는) Caribbean에 위치한 나라로, 수도는 Havana이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '쿠바');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '퀴라소', 'Willemstad', 'https://flagcdn.com/w320/cw.png', '퀴라소은(는) Caribbean에 위치한 나라로, 수도는 Willemstad이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '퀴라소');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '크리스마스 섬', 'Flying Fish Cove', 'https://flagcdn.com/w320/cx.png', '크리스마스 섬은(는) Australia and New Zealand에 위치한 나라로, 수도는 Flying Fish Cove이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '크리스마스 섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '케이맨 제도', 'George Town', 'https://flagcdn.com/w320/ky.png', '케이맨 제도은(는) Caribbean에 위치한 나라로, 수도는 George Town이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '케이맨 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '키프로스', 'Nicosia', 'https://flagcdn.com/w320/cy.png', '키프로스은(는) Southern Europe에 위치한 나라로, 수도는 Nicosia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '키프로스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '체코', 'Prague', 'https://flagcdn.com/w320/cz.png', '체코은(는) Central Europe에 위치한 나라로, 수도는 Prague이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '체코');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '독일', 'Berlin', 'https://flagcdn.com/w320/de.png', '독일은(는) Western Europe에 위치한 나라로, 수도는 Berlin이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '독일');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '지부티', 'Djibouti', 'https://flagcdn.com/w320/dj.png', '지부티은(는) Eastern Africa에 위치한 나라로, 수도는 Djibouti이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '지부티');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '도미니카 공화국', 'Roseau', 'https://flagcdn.com/w320/dm.png', '도미니카 공화국은(는) Caribbean에 위치한 나라로, 수도는 Roseau이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '도미니카 공화국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '덴마크', 'Copenhagen', 'https://flagcdn.com/w320/dk.png', '덴마크은(는) Northern Europe에 위치한 나라로, 수도는 Copenhagen이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '덴마크');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '도미니카 공화국', 'Santo Domingo', 'https://flagcdn.com/w320/do.png', '도미니카 공화국은(는) Caribbean에 위치한 나라로, 수도는 Santo Domingo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '도미니카 공화국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '알제리', 'Algiers', 'https://flagcdn.com/w320/dz.png', '알제리은(는) Northern Africa에 위치한 나라로, 수도는 Algiers이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '알제리');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '에콰도르', 'Quito', 'https://flagcdn.com/w320/ec.png', '에콰도르은(는) South America에 위치한 나라로, 수도는 Quito이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '에콰도르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '이집트', 'Cairo', 'https://flagcdn.com/w320/eg.png', '이집트은(는) Northern Africa에 위치한 나라로, 수도는 Cairo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '이집트');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '에리트레아', 'Asmara', 'https://flagcdn.com/w320/er.png', '에리트레아은(는) Eastern Africa에 위치한 나라로, 수도는 Asmara이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '에리트레아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '서사하라', 'El Aaiún', 'https://flagcdn.com/w320/eh.png', '서사하라은(는) Northern Africa에 위치한 나라로, 수도는 El Aaiún이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '서사하라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '스페인', 'Madrid', 'https://flagcdn.com/w320/es.png', '스페인은(는) Southern Europe에 위치한 나라로, 수도는 Madrid이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '스페인');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '에스토니아', 'Tallinn', 'https://flagcdn.com/w320/ee.png', '에스토니아은(는) Northern Europe에 위치한 나라로, 수도는 Tallinn이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '에스토니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '에티오피아', 'Addis Ababa', 'https://flagcdn.com/w320/et.png', '에티오피아은(는) Eastern Africa에 위치한 나라로, 수도는 Addis Ababa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '에티오피아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '핀란드', 'Helsinki', 'https://flagcdn.com/w320/fi.png', '핀란드은(는) Northern Europe에 위치한 나라로, 수도는 Helsinki이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '핀란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '피지', 'Suva', 'https://flagcdn.com/w320/fj.png', '피지은(는) Melanesia에 위치한 나라로, 수도는 Suva이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '피지');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '포클랜드 제도', 'Stanley', 'https://flagcdn.com/w320/fk.png', '포클랜드 제도은(는) South America에 위치한 나라로, 수도는 Stanley이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '포클랜드 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '프랑스', 'Paris', 'https://flagcdn.com/w320/fr.png', '프랑스은(는) Western Europe에 위치한 나라로, 수도는 Paris이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '프랑스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '페로 제도', 'Tórshavn', 'https://flagcdn.com/w320/fo.png', '페로 제도은(는) Northern Europe에 위치한 나라로, 수도는 Tórshavn이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '페로 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '미크로네시아', 'Palikir', 'https://flagcdn.com/w320/fm.png', '미크로네시아은(는) Micronesia에 위치한 나라로, 수도는 Palikir이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '미크로네시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '가봉', 'Libreville', 'https://flagcdn.com/w320/ga.png', '가봉은(는) Middle Africa에 위치한 나라로, 수도는 Libreville이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '가봉');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '영국', 'London', 'https://flagcdn.com/w320/gb.png', '영국은(는) Northern Europe에 위치한 나라로, 수도는 London이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '영국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '조지아', 'Tbilisi', 'https://flagcdn.com/w320/ge.png', '조지아은(는) Western Asia에 위치한 나라로, 수도는 Tbilisi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '조지아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '건지 섬', 'St. Peter Port', 'https://flagcdn.com/w320/gg.png', '건지 섬은(는) Northern Europe에 위치한 나라로, 수도는 St. Peter Port이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '건지 섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '가나', 'Accra', 'https://flagcdn.com/w320/gh.png', '가나은(는) Western Africa에 위치한 나라로, 수도는 Accra이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '가나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '지브롤터', 'Gibraltar', 'https://flagcdn.com/w320/gi.png', '지브롤터은(는) Southern Europe에 위치한 나라로, 수도는 Gibraltar이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '지브롤터');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '기니', 'Conakry', 'https://flagcdn.com/w320/gn.png', '기니은(는) Western Africa에 위치한 나라로, 수도는 Conakry이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '기니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '과들루프', 'Basse-Terre', 'https://flagcdn.com/w320/gp.png', '과들루프은(는) Caribbean에 위치한 나라로, 수도는 Basse-Terre이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '과들루프');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '감비아', 'Banjul', 'https://flagcdn.com/w320/gm.png', '감비아은(는) Western Africa에 위치한 나라로, 수도는 Banjul이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '감비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '기니비사우', 'Bissau', 'https://flagcdn.com/w320/gw.png', '기니비사우은(는) Western Africa에 위치한 나라로, 수도는 Bissau이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '기니비사우');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '적도 기니', 'Malabo', 'https://flagcdn.com/w320/gq.png', '적도 기니은(는) Middle Africa에 위치한 나라로, 수도는 Malabo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '적도 기니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '그리스', 'Athens', 'https://flagcdn.com/w320/gr.png', '그리스은(는) Southern Europe에 위치한 나라로, 수도는 Athens이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '그리스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '그레나다', 'St. George\'s', 'https://flagcdn.com/w320/gd.png', '그레나다은(는) Caribbean에 위치한 나라로, 수도는 St. George\'s이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '그레나다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '그린란드', 'Nuuk', 'https://flagcdn.com/w320/gl.png', '그린란드은(는) North America에 위치한 나라로, 수도는 Nuuk이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '그린란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '과테말라', 'Guatemala City', 'https://flagcdn.com/w320/gt.png', '과테말라은(는) Central America에 위치한 나라로, 수도는 Guatemala City이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '과테말라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '프랑스령 기아나', 'Cayenne', 'https://flagcdn.com/w320/gf.png', '프랑스령 기아나은(는) South America에 위치한 나라로, 수도는 Cayenne이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '프랑스령 기아나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '괌', 'Hagåtña', 'https://flagcdn.com/w320/gu.png', '괌은(는) Micronesia에 위치한 나라로, 수도는 Hagåtña이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '괌');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '가이아나', 'Georgetown', 'https://flagcdn.com/w320/gy.png', '가이아나은(는) South America에 위치한 나라로, 수도는 Georgetown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '가이아나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '홍콩', 'City of Victoria', 'https://flagcdn.com/w320/hk.png', '홍콩은(는) Eastern Asia에 위치한 나라로, 수도는 City of Victoria이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '홍콩');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '허드 맥도널드 제도', '허드 맥도널드 제도', 'https://flagcdn.com/w320/hm.png', '허드 맥도널드 제도은(는) Antarctic에 위치한 나라로, 수도는 허드 맥도널드 제도이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '허드 맥도널드 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '온두라스', 'Tegucigalpa', 'https://flagcdn.com/w320/hn.png', '온두라스은(는) Central America에 위치한 나라로, 수도는 Tegucigalpa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '온두라스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '크로아티아', 'Zagreb', 'https://flagcdn.com/w320/hr.png', '크로아티아은(는) Southeast Europe에 위치한 나라로, 수도는 Zagreb이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '크로아티아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아이티', 'Port-au-Prince', 'https://flagcdn.com/w320/ht.png', '아이티은(는) Caribbean에 위치한 나라로, 수도는 Port-au-Prince이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아이티');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '헝가리', 'Budapest', 'https://flagcdn.com/w320/hu.png', '헝가리은(는) Central Europe에 위치한 나라로, 수도는 Budapest이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '헝가리');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '인도네시아', 'Jakarta', 'https://flagcdn.com/w320/id.png', '인도네시아은(는) South-Eastern Asia에 위치한 나라로, 수도는 Jakarta이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '인도네시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '맨섬', 'Douglas', 'https://flagcdn.com/w320/im.png', '맨섬은(는) Northern Europe에 위치한 나라로, 수도는 Douglas이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '맨섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '인도', 'New Delhi', 'https://flagcdn.com/w320/in.png', '인도은(는) Southern Asia에 위치한 나라로, 수도는 New Delhi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '인도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '인도', 'Diego Garcia', 'https://flagcdn.com/w320/io.png', '인도은(는) Eastern Africa에 위치한 나라로, 수도는 Diego Garcia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '인도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아일랜드', 'Dublin', 'https://flagcdn.com/w320/ie.png', '아일랜드은(는) Northern Europe에 위치한 나라로, 수도는 Dublin이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아일랜드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '이란', 'Tehran', 'https://flagcdn.com/w320/ir.png', '이란은(는) Southern Asia에 위치한 나라로, 수도는 Tehran이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '이란');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '이라크', 'Baghdad', 'https://flagcdn.com/w320/iq.png', '이라크은(는) Western Asia에 위치한 나라로, 수도는 Baghdad이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '이라크');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '아이슬란드', 'Reykjavik', 'https://flagcdn.com/w320/is.png', '아이슬란드은(는) Northern Europe에 위치한 나라로, 수도는 Reykjavik이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '아이슬란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '이스라엘', 'Jerusalem', 'https://flagcdn.com/w320/il.png', '이스라엘은(는) Western Asia에 위치한 나라로, 수도는 Jerusalem이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '이스라엘');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '이탈리아', 'Rome', 'https://flagcdn.com/w320/it.png', '이탈리아은(는) Southern Europe에 위치한 나라로, 수도는 Rome이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '이탈리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '자메이카', 'Kingston', 'https://flagcdn.com/w320/jm.png', '자메이카은(는) Caribbean에 위치한 나라로, 수도는 Kingston이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '자메이카');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '저지 섬', 'Saint Helier', 'https://flagcdn.com/w320/je.png', '저지 섬은(는) Northern Europe에 위치한 나라로, 수도는 Saint Helier이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '저지 섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '요르단', 'Amman', 'https://flagcdn.com/w320/jo.png', '요르단은(는) Western Asia에 위치한 나라로, 수도는 Amman이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '요르단');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '일본', 'Tokyo', 'https://flagcdn.com/w320/jp.png', '일본은(는) Eastern Asia에 위치한 나라로, 수도는 Tokyo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '일본');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '카자흐스탄', 'Astana', 'https://flagcdn.com/w320/kz.png', '카자흐스탄은(는) Central Asia에 위치한 나라로, 수도는 Astana이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '카자흐스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '케냐', 'Nairobi', 'https://flagcdn.com/w320/ke.png', '케냐은(는) Eastern Africa에 위치한 나라로, 수도는 Nairobi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '케냐');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '키르기스스탄', 'Bishkek', 'https://flagcdn.com/w320/kg.png', '키르기스스탄은(는) Central Asia에 위치한 나라로, 수도는 Bishkek이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '키르기스스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '캄보디아', 'Phnom Penh', 'https://flagcdn.com/w320/kh.png', '캄보디아은(는) South-Eastern Asia에 위치한 나라로, 수도는 Phnom Penh이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '캄보디아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '키리바시', 'South Tarawa', 'https://flagcdn.com/w320/ki.png', '키리바시은(는) Micronesia에 위치한 나라로, 수도는 South Tarawa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '키리바시');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세인트키츠 네비스', 'Basseterre', 'https://flagcdn.com/w320/kn.png', '세인트키츠 네비스은(는) Caribbean에 위치한 나라로, 수도는 Basseterre이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세인트키츠 네비스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '한국', 'Seoul', 'https://flagcdn.com/w320/kr.png', '한국은(는) Eastern Asia에 위치한 나라로, 수도는 Seoul이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '한국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '코소보', 'Pristina', 'https://flagcdn.com/w320/xk.png', '코소보은(는) Southeast Europe에 위치한 나라로, 수도는 Pristina이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '코소보');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '쿠웨이트', 'Kuwait City', 'https://flagcdn.com/w320/kw.png', '쿠웨이트은(는) Western Asia에 위치한 나라로, 수도는 Kuwait City이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '쿠웨이트');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '라오스', 'Vientiane', 'https://flagcdn.com/w320/la.png', '라오스은(는) South-Eastern Asia에 위치한 나라로, 수도는 Vientiane이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '라오스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '레바논', 'Beirut', 'https://flagcdn.com/w320/lb.png', '레바논은(는) Western Asia에 위치한 나라로, 수도는 Beirut이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '레바논');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '라이베리아', 'Monrovia', 'https://flagcdn.com/w320/lr.png', '라이베리아은(는) Western Africa에 위치한 나라로, 수도는 Monrovia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '라이베리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '리비아', 'Tripoli', 'https://flagcdn.com/w320/ly.png', '리비아은(는) Northern Africa에 위치한 나라로, 수도는 Tripoli이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '리비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세인트루시아', 'Castries', 'https://flagcdn.com/w320/lc.png', '세인트루시아은(는) Caribbean에 위치한 나라로, 수도는 Castries이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세인트루시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '리히텐슈타인', 'Vaduz', 'https://flagcdn.com/w320/li.png', '리히텐슈타인은(는) Western Europe에 위치한 나라로, 수도는 Vaduz이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '리히텐슈타인');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '스리랑카', 'Colombo', 'https://flagcdn.com/w320/lk.png', '스리랑카은(는) Southern Asia에 위치한 나라로, 수도는 Colombo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '스리랑카');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '레소토', 'Maseru', 'https://flagcdn.com/w320/ls.png', '레소토은(는) Southern Africa에 위치한 나라로, 수도는 Maseru이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '레소토');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '리투아니아', 'Vilnius', 'https://flagcdn.com/w320/lt.png', '리투아니아은(는) Northern Europe에 위치한 나라로, 수도는 Vilnius이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '리투아니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '룩셈부르크', 'Luxembourg', 'https://flagcdn.com/w320/lu.png', '룩셈부르크은(는) Western Europe에 위치한 나라로, 수도는 Luxembourg이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '룩셈부르크');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '라트비아', 'Riga', 'https://flagcdn.com/w320/lv.png', '라트비아은(는) Northern Europe에 위치한 나라로, 수도는 Riga이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '라트비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '마카오', '마카오', 'https://flagcdn.com/w320/mo.png', '마카오은(는) Eastern Asia에 위치한 나라로, 수도는 마카오이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '마카오');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '생마르탱', 'Marigot', 'https://flagcdn.com/w320/mf.png', '생마르탱은(는) Caribbean에 위치한 나라로, 수도는 Marigot이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '생마르탱');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '모로코', 'Rabat', 'https://flagcdn.com/w320/ma.png', '모로코은(는) Northern Africa에 위치한 나라로, 수도는 Rabat이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '모로코');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '모나코', 'Monaco', 'https://flagcdn.com/w320/mc.png', '모나코은(는) Western Europe에 위치한 나라로, 수도는 Monaco이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '모나코');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몰도바', 'Chișinău', 'https://flagcdn.com/w320/md.png', '몰도바은(는) Eastern Europe에 위치한 나라로, 수도는 Chișinău이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몰도바');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '마다가스카르', 'Antananarivo', 'https://flagcdn.com/w320/mg.png', '마다가스카르은(는) Eastern Africa에 위치한 나라로, 수도는 Antananarivo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '마다가스카르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몰디브', 'Malé', 'https://flagcdn.com/w320/mv.png', '몰디브은(는) Southern Asia에 위치한 나라로, 수도는 Malé이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몰디브');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '멕시코', 'Mexico City', 'https://flagcdn.com/w320/mx.png', '멕시코은(는) North America에 위치한 나라로, 수도는 Mexico City이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '멕시코');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '마셜 제도', 'Majuro', 'https://flagcdn.com/w320/mh.png', '마셜 제도은(는) Micronesia에 위치한 나라로, 수도는 Majuro이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '마셜 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '북마케도니아', 'Skopje', 'https://flagcdn.com/w320/mk.png', '북마케도니아은(는) Southeast Europe에 위치한 나라로, 수도는 Skopje이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '북마케도니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '말리', 'Bamako', 'https://flagcdn.com/w320/ml.png', '말리은(는) Western Africa에 위치한 나라로, 수도는 Bamako이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '말리');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몰타', 'Valletta', 'https://flagcdn.com/w320/mt.png', '몰타은(는) Southern Europe에 위치한 나라로, 수도는 Valletta이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몰타');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '미얀마', 'Naypyidaw', 'https://flagcdn.com/w320/mm.png', '미얀마은(는) South-Eastern Asia에 위치한 나라로, 수도는 Naypyidaw이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '미얀마');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몬테네그로', 'Podgorica', 'https://flagcdn.com/w320/me.png', '몬테네그로은(는) Southeast Europe에 위치한 나라로, 수도는 Podgorica이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몬테네그로');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몽골국', 'Ulan Bator', 'https://flagcdn.com/w320/mn.png', '몽골국은(는) Eastern Asia에 위치한 나라로, 수도는 Ulan Bator이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몽골국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '북마리아나 제도', 'Saipan', 'https://flagcdn.com/w320/mp.png', '북마리아나 제도은(는) Micronesia에 위치한 나라로, 수도는 Saipan이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '북마리아나 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '모잠비크', 'Maputo', 'https://flagcdn.com/w320/mz.png', '모잠비크은(는) Eastern Africa에 위치한 나라로, 수도는 Maputo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '모잠비크');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '모리타니', 'Nouakchott', 'https://flagcdn.com/w320/mr.png', '모리타니은(는) Western Africa에 위치한 나라로, 수도는 Nouakchott이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '모리타니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '몬트세랫', 'Plymouth', 'https://flagcdn.com/w320/ms.png', '몬트세랫은(는) Caribbean에 위치한 나라로, 수도는 Plymouth이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '몬트세랫');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '마르티니크', 'Fort-de-France', 'https://flagcdn.com/w320/mq.png', '마르티니크은(는) Caribbean에 위치한 나라로, 수도는 Fort-de-France이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '마르티니크');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '모리셔스', 'Port Louis', 'https://flagcdn.com/w320/mu.png', '모리셔스은(는) Eastern Africa에 위치한 나라로, 수도는 Port Louis이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '모리셔스');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '말라위', 'Lilongwe', 'https://flagcdn.com/w320/mw.png', '말라위은(는) Eastern Africa에 위치한 나라로, 수도는 Lilongwe이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '말라위');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '말레이시아', 'Kuala Lumpur', 'https://flagcdn.com/w320/my.png', '말레이시아은(는) South-Eastern Asia에 위치한 나라로, 수도는 Kuala Lumpur이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '말레이시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '마요트', 'Mamoudzou', 'https://flagcdn.com/w320/yt.png', '마요트은(는) Eastern Africa에 위치한 나라로, 수도는 Mamoudzou이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '마요트');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '나미비아', 'Windhoek', 'https://flagcdn.com/w320/na.png', '나미비아은(는) Southern Africa에 위치한 나라로, 수도는 Windhoek이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '나미비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '누벨칼레도니', 'Nouméa', 'https://flagcdn.com/w320/nc.png', '누벨칼레도니은(는) Melanesia에 위치한 나라로, 수도는 Nouméa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '누벨칼레도니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '니제르', 'Niamey', 'https://flagcdn.com/w320/ne.png', '니제르은(는) Western Africa에 위치한 나라로, 수도는 Niamey이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '니제르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '노퍽 섬', 'Kingston', 'https://flagcdn.com/w320/nf.png', '노퍽 섬은(는) Australia and New Zealand에 위치한 나라로, 수도는 Kingston이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '노퍽 섬');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '나이지리아', 'Abuja', 'https://flagcdn.com/w320/ng.png', '나이지리아은(는) Western Africa에 위치한 나라로, 수도는 Abuja이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '나이지리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '니카라과', 'Managua', 'https://flagcdn.com/w320/ni.png', '니카라과은(는) Central America에 위치한 나라로, 수도는 Managua이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '니카라과');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '니우에', 'Alofi', 'https://flagcdn.com/w320/nu.png', '니우에은(는) Polynesia에 위치한 나라로, 수도는 Alofi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '니우에');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '네덜란드', 'Amsterdam', 'https://flagcdn.com/w320/nl.png', '네덜란드은(는) Western Europe에 위치한 나라로, 수도는 Amsterdam이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '네덜란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '노르웨이', 'Oslo', 'https://flagcdn.com/w320/no.png', '노르웨이은(는) Northern Europe에 위치한 나라로, 수도는 Oslo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '노르웨이');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '네팔', 'Kathmandu', 'https://flagcdn.com/w320/np.png', '네팔은(는) Southern Asia에 위치한 나라로, 수도는 Kathmandu이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '네팔');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '나우루', 'Yaren', 'https://flagcdn.com/w320/nr.png', '나우루은(는) Micronesia에 위치한 나라로, 수도는 Yaren이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '나우루');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '뉴질랜드', 'Wellington', 'https://flagcdn.com/w320/nz.png', '뉴질랜드은(는) Australia and New Zealand에 위치한 나라로, 수도는 Wellington이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '뉴질랜드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '오만', 'Muscat', 'https://flagcdn.com/w320/om.png', '오만은(는) Western Asia에 위치한 나라로, 수도는 Muscat이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '오만');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '파키스탄', 'Islamabad', 'https://flagcdn.com/w320/pk.png', '파키스탄은(는) Southern Asia에 위치한 나라로, 수도는 Islamabad이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '파키스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '파나마', 'Panama City', 'https://flagcdn.com/w320/pa.png', '파나마은(는) Central America에 위치한 나라로, 수도는 Panama City이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '파나마');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '핏케언 제도', 'Adamstown', 'https://flagcdn.com/w320/pn.png', '핏케언 제도은(는) Polynesia에 위치한 나라로, 수도는 Adamstown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '핏케언 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '페루', 'Lima', 'https://flagcdn.com/w320/pe.png', '페루은(는) South America에 위치한 나라로, 수도는 Lima이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '페루');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '필리핀', 'Manila', 'https://flagcdn.com/w320/ph.png', '필리핀은(는) South-Eastern Asia에 위치한 나라로, 수도는 Manila이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '필리핀');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '팔라우', 'Ngerulmud', 'https://flagcdn.com/w320/pw.png', '팔라우은(는) Micronesia에 위치한 나라로, 수도는 Ngerulmud이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '팔라우');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '파푸아뉴기니', 'Port Moresby', 'https://flagcdn.com/w320/pg.png', '파푸아뉴기니은(는) Melanesia에 위치한 나라로, 수도는 Port Moresby이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '파푸아뉴기니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '폴란드', 'Warsaw', 'https://flagcdn.com/w320/pl.png', '폴란드은(는) Central Europe에 위치한 나라로, 수도는 Warsaw이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '폴란드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '푸에르토리코', 'San Juan', 'https://flagcdn.com/w320/pr.png', '푸에르토리코은(는) Caribbean에 위치한 나라로, 수도는 San Juan이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '푸에르토리코');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '조선', 'Pyongyang', 'https://flagcdn.com/w320/kp.png', '조선은(는) Eastern Asia에 위치한 나라로, 수도는 Pyongyang이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '조선');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '포르투갈', 'Lisbon', 'https://flagcdn.com/w320/pt.png', '포르투갈은(는) Southern Europe에 위치한 나라로, 수도는 Lisbon이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '포르투갈');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '파라과이', 'Asunción', 'https://flagcdn.com/w320/py.png', '파라과이은(는) South America에 위치한 나라로, 수도는 Asunción이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '파라과이');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '팔레스타인', 'Ramallah', 'https://flagcdn.com/w320/ps.png', '팔레스타인은(는) Western Asia에 위치한 나라로, 수도는 Ramallah이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '팔레스타인');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '프랑스령 폴리네시아', 'Papeetē', 'https://flagcdn.com/w320/pf.png', '프랑스령 폴리네시아은(는) Polynesia에 위치한 나라로, 수도는 Papeetē이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '프랑스령 폴리네시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '카타르', 'Doha', 'https://flagcdn.com/w320/qa.png', '카타르은(는) Western Asia에 위치한 나라로, 수도는 Doha이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '카타르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '레위니옹', 'Saint-Denis', 'https://flagcdn.com/w320/re.png', '레위니옹은(는) Eastern Africa에 위치한 나라로, 수도는 Saint-Denis이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '레위니옹');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '루마니아', 'Bucharest', 'https://flagcdn.com/w320/ro.png', '루마니아은(는) Southeast Europe에 위치한 나라로, 수도는 Bucharest이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '루마니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '러시아', 'Moscow', 'https://flagcdn.com/w320/ru.png', '러시아은(는) Eastern Europe에 위치한 나라로, 수도는 Moscow이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '러시아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '르완다', 'Kigali', 'https://flagcdn.com/w320/rw.png', '르완다은(는) Eastern Africa에 위치한 나라로, 수도는 Kigali이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '르완다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '사우디아라비아', 'Riyadh', 'https://flagcdn.com/w320/sa.png', '사우디아라비아은(는) Western Asia에 위치한 나라로, 수도는 Riyadh이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '사우디아라비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '수단', 'Khartoum', 'https://flagcdn.com/w320/sd.png', '수단은(는) Northern Africa에 위치한 나라로, 수도는 Khartoum이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '수단');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세네갈', 'Dakar', 'https://flagcdn.com/w320/sn.png', '세네갈은(는) Western Africa에 위치한 나라로, 수도는 Dakar이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세네갈');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '싱가포르', 'Singapore', 'https://flagcdn.com/w320/sg.png', '싱가포르은(는) South-Eastern Asia에 위치한 나라로, 수도는 Singapore이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '싱가포르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '조지아', 'King Edward Point', 'https://flagcdn.com/w320/gs.png', '조지아은(는) Antarctic에 위치한 나라로, 수도는 King Edward Point이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '조지아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '스발바르 얀마옌 제도', 'Longyearbyen', 'https://flagcdn.com/w320/sj.png', '스발바르 얀마옌 제도은(는) Northern Europe에 위치한 나라로, 수도는 Longyearbyen이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '스발바르 얀마옌 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '솔로몬 제도', 'Honiara', 'https://flagcdn.com/w320/sb.png', '솔로몬 제도은(는) Melanesia에 위치한 나라로, 수도는 Honiara이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '솔로몬 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '시에라리온', 'Freetown', 'https://flagcdn.com/w320/sl.png', '시에라리온은(는) Western Africa에 위치한 나라로, 수도는 Freetown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '시에라리온');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '엘살바도르', 'San Salvador', 'https://flagcdn.com/w320/sv.png', '엘살바도르은(는) Central America에 위치한 나라로, 수도는 San Salvador이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '엘살바도르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '산마리노', 'City of San Marino', 'https://flagcdn.com/w320/sm.png', '산마리노은(는) Southern Europe에 위치한 나라로, 수도는 City of San Marino이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '산마리노');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '소말리아', 'Mogadishu', 'https://flagcdn.com/w320/so.png', '소말리아은(는) Eastern Africa에 위치한 나라로, 수도는 Mogadishu이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '소말리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '생피에르 미클롱', 'Saint-Pierre', 'https://flagcdn.com/w320/pm.png', '생피에르 미클롱은(는) North America에 위치한 나라로, 수도는 Saint-Pierre이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '생피에르 미클롱');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세르비아', 'Belgrade', 'https://flagcdn.com/w320/rs.png', '세르비아은(는) Southeast Europe에 위치한 나라로, 수도는 Belgrade이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세르비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '남수단', 'Juba', 'https://flagcdn.com/w320/ss.png', '남수단은(는) Middle Africa에 위치한 나라로, 수도는 Juba이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '남수단');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '상투메 프린시페', 'São Tomé', 'https://flagcdn.com/w320/st.png', '상투메 프린시페은(는) Middle Africa에 위치한 나라로, 수도는 São Tomé이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '상투메 프린시페');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '수리남', 'Paramaribo', 'https://flagcdn.com/w320/sr.png', '수리남은(는) South America에 위치한 나라로, 수도는 Paramaribo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '수리남');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '슬로바키아', 'Bratislava', 'https://flagcdn.com/w320/sk.png', '슬로바키아은(는) Central Europe에 위치한 나라로, 수도는 Bratislava이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '슬로바키아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '슬로베니아', 'Ljubljana', 'https://flagcdn.com/w320/si.png', '슬로베니아은(는) Central Europe에 위치한 나라로, 수도는 Ljubljana이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '슬로베니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '스웨덴', 'Stockholm', 'https://flagcdn.com/w320/se.png', '스웨덴은(는) Northern Europe에 위치한 나라로, 수도는 Stockholm이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '스웨덴');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '에스와티니', 'Lobamba', 'https://flagcdn.com/w320/sz.png', '에스와티니은(는) Southern Africa에 위치한 나라로, 수도는 Lobamba이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '에스와티니');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '신트마르턴', 'Philipsburg', 'https://flagcdn.com/w320/sx.png', '신트마르턴은(는) Caribbean에 위치한 나라로, 수도는 Philipsburg이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '신트마르턴');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세이셸', 'Victoria', 'https://flagcdn.com/w320/sc.png', '세이셸은(는) Eastern Africa에 위치한 나라로, 수도는 Victoria이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세이셸');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '시리아', 'Damascus', 'https://flagcdn.com/w320/sy.png', '시리아은(는) Western Asia에 위치한 나라로, 수도는 Damascus이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '시리아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '터크스 케이커스 제도', 'Cockburn Town', 'https://flagcdn.com/w320/tc.png', '터크스 케이커스 제도은(는) Caribbean에 위치한 나라로, 수도는 Cockburn Town이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '터크스 케이커스 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '차드', 'N\'Djamena', 'https://flagcdn.com/w320/td.png', '차드은(는) Middle Africa에 위치한 나라로, 수도는 N\'Djamena이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '차드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '토고', 'Lomé', 'https://flagcdn.com/w320/tg.png', '토고은(는) Western Africa에 위치한 나라로, 수도는 Lomé이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '토고');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '태국', 'Bangkok', 'https://flagcdn.com/w320/th.png', '태국은(는) South-Eastern Asia에 위치한 나라로, 수도는 Bangkok이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '태국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '타지키스탄', 'Dushanbe', 'https://flagcdn.com/w320/tj.png', '타지키스탄은(는) Central Asia에 위치한 나라로, 수도는 Dushanbe이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '타지키스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '토켈라우', 'Fakaofo', 'https://flagcdn.com/w320/tk.png', '토켈라우은(는) Polynesia에 위치한 나라로, 수도는 Fakaofo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '토켈라우');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '투르크메니스탄', 'Ashgabat', 'https://flagcdn.com/w320/tm.png', '투르크메니스탄은(는) Central Asia에 위치한 나라로, 수도는 Ashgabat이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '투르크메니스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '동티모르', 'Dili', 'https://flagcdn.com/w320/tl.png', '동티모르은(는) South-Eastern Asia에 위치한 나라로, 수도는 Dili이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '동티모르');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '통가', 'Nuku\'alofa', 'https://flagcdn.com/w320/to.png', '통가은(는) Polynesia에 위치한 나라로, 수도는 Nuku\'alofa이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '통가');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '트리니다드 토바고', 'Port of Spain', 'https://flagcdn.com/w320/tt.png', '트리니다드 토바고은(는) Caribbean에 위치한 나라로, 수도는 Port of Spain이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '트리니다드 토바고');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '튀니지', 'Tunis', 'https://flagcdn.com/w320/tn.png', '튀니지은(는) Northern Africa에 위치한 나라로, 수도는 Tunis이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '튀니지');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '터키', 'Ankara', 'https://flagcdn.com/w320/tr.png', '터키은(는) Western Asia에 위치한 나라로, 수도는 Ankara이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '터키');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '투발루', 'Funafuti', 'https://flagcdn.com/w320/tv.png', '투발루은(는) Polynesia에 위치한 나라로, 수도는 Funafuti이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '투발루');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '대만', 'Taipei', 'https://flagcdn.com/w320/tw.png', '대만은(는) Eastern Asia에 위치한 나라로, 수도는 Taipei이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '대만');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '탄자니아', 'Dodoma', 'https://flagcdn.com/w320/tz.png', '탄자니아은(는) Eastern Africa에 위치한 나라로, 수도는 Dodoma이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '탄자니아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '우간다', 'Kampala', 'https://flagcdn.com/w320/ug.png', '우간다은(는) Eastern Africa에 위치한 나라로, 수도는 Kampala이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '우간다');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '우크라이나', 'Kyiv', 'https://flagcdn.com/w320/ua.png', '우크라이나은(는) Eastern Europe에 위치한 나라로, 수도는 Kyiv이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '우크라이나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '미국령 군소 제도', '미국령 군소 제도', 'https://flagcdn.com/w320/um.png', '미국령 군소 제도은(는) North America에 위치한 나라로, 수도는 미국령 군소 제도이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '미국령 군소 제도');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '우루과이', 'Montevideo', 'https://flagcdn.com/w320/uy.png', '우루과이은(는) South America에 위치한 나라로, 수도는 Montevideo이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '우루과이');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '미국', 'Washington D.C.', 'https://flagcdn.com/w320/us.png', '미국은(는) North America에 위치한 나라로, 수도는 Washington D.C.이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '미국');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '우즈베키스탄', 'Tashkent', 'https://flagcdn.com/w320/uz.png', '우즈베키스탄은(는) Central Asia에 위치한 나라로, 수도는 Tashkent이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '우즈베키스탄');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '바티칸', 'Vatican City', 'https://flagcdn.com/w320/va.png', '바티칸은(는) Southern Europe에 위치한 나라로, 수도는 Vatican City이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '바티칸');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '세인트빈센트 그레나딘', 'Kingstown', 'https://flagcdn.com/w320/vc.png', '세인트빈센트 그레나딘은(는) Caribbean에 위치한 나라로, 수도는 Kingstown이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '세인트빈센트 그레나딘');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '베네수엘라', 'Caracas', 'https://flagcdn.com/w320/ve.png', '베네수엘라은(는) South America에 위치한 나라로, 수도는 Caracas이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '베네수엘라');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '영국령 버진아일랜드', 'Road Town', 'https://flagcdn.com/w320/vg.png', '영국령 버진아일랜드은(는) Caribbean에 위치한 나라로, 수도는 Road Town이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '영국령 버진아일랜드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '미국령 버진아일랜드', 'Charlotte Amalie', 'https://flagcdn.com/w320/vi.png', '미국령 버진아일랜드은(는) Caribbean에 위치한 나라로, 수도는 Charlotte Amalie이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '미국령 버진아일랜드');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '베트남', 'Hanoi', 'https://flagcdn.com/w320/vn.png', '베트남은(는) South-Eastern Asia에 위치한 나라로, 수도는 Hanoi이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '베트남');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '바누아투', 'Port Vila', 'https://flagcdn.com/w320/vu.png', '바누아투은(는) Melanesia에 위치한 나라로, 수도는 Port Vila이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '바누아투');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '왈리스 퓌튀나', 'Mata-Utu', 'https://flagcdn.com/w320/wf.png', '왈리스 퓌튀나은(는) Polynesia에 위치한 나라로, 수도는 Mata-Utu이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '왈리스 퓌튀나');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '사모아', 'Apia', 'https://flagcdn.com/w320/ws.png', '사모아은(는) Polynesia에 위치한 나라로, 수도는 Apia이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '사모아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '예멘', 'Sana\'a', 'https://flagcdn.com/w320/ye.png', '예멘은(는) Western Asia에 위치한 나라로, 수도는 Sana\'a이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '예멘');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '남아프리카', 'Pretoria', 'https://flagcdn.com/w320/za.png', '남아프리카은(는) Southern Africa에 위치한 나라로, 수도는 Pretoria이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '남아프리카');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '잠비아', 'Lusaka', 'https://flagcdn.com/w320/zm.png', '잠비아은(는) Eastern Africa에 위치한 나라로, 수도는 Lusaka이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '잠비아');

INSERT INTO country_info (country_name, city_name, image_url, summary)
SELECT '짐바브웨', 'Harare', 'https://flagcdn.com/w320/zw.png', '짐바브웨은(는) Eastern Africa에 위치한 나라로, 수도는 Harare이며 인구는 약 정보 없음명입니다.'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM country_info WHERE country_name = '짐바브웨');
