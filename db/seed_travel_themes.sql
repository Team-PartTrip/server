-- 여행 타입 시드 데이터 (Func-007-01)
-- theme_code 가 이미 존재하면 건너뛰므로 여러 번 실행해도 안전합니다.
--
-- ※ 계획형 모험가 / 여행을 계획하고 기록하는 사람 두 건은 디자인(앱 E1 · 웹 Func-007-01)에
--   실제로 쓰인 문구입니다. 나머지는 축(계획 성향 × 활동 성향)을 채우기 위한 초안이므로
--   기획에서 문구와 개수를 확정한 뒤 교체해 주세요.

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'PLANNER_ADVENTURER', '계획형 모험가', '여행을 계획하고 기록하는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'PLANNER_ADVENTURER');

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'PLANNER_RESTER', '계획형 휴양가', '동선을 미리 짜두고 여유롭게 쉬는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'PLANNER_RESTER');

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'FREE_ADVENTURER', '즉흥형 모험가', '발길 닿는 대로 새로운 곳을 찾는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'FREE_ADVENTURER');

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'FREE_RESTER', '즉흥형 휴양가', '계획 없이 마음 가는 대로 쉬는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'FREE_RESTER');

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'FOODIE', '미식 탐험가', '그 지역의 맛으로 여행을 기억하는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'FOODIE');

INSERT INTO travel_theme (theme_code, theme_name, description, image_url, create_date)
SELECT 'RECORDER', '기록 수집가', '사진과 메모로 순간을 남기는 사람', NULL, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM travel_theme WHERE theme_code = 'RECORDER');
