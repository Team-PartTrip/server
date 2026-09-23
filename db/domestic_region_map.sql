-- 세계지도 → 대한민국 지도 (#162)
--
-- ddl-auto=update 는 컬럼을 지우지도, NOT NULL 을 풀지도 않는다.
-- region_code 는 알아서 생기지만 country_name 은 NOT NULL 로 남아서,
-- 이 스크립트를 돌리기 전까지 플래너 · 여행카드 저장이 전부 실패한다.
-- 머지하고 한 번 직접 실행한다.

-- 먼저 컬럼을 만든다. 서버를 아직 안 올렸다면 ddl-auto 가 만들어 둔다
ALTER TABLE trip_card          ADD COLUMN IF NOT EXISTS region_code CHAR(2);
ALTER TABLE planner_city       ADD COLUMN IF NOT EXISTS region_code CHAR(2);
ALTER TABLE group_travel_plan  ADD COLUMN IF NOT EXISTS region_code CHAR(2);

-- 국내 기록만 지도에 올린다. 해외 기록은 region_code 가 비어서 지도에서 빠지고,
-- 여행카드 자체는 그대로 남는다. 다녀온 기록을 서버가 지울 일은 아니다.
-- (시·도를 알 수 없으므로 자동으로 채워 줄 수 있는 값이 없다)

-- 방문 지역은 따로 저장하지 않는다. 여행카드를 시·도로 묶어 센다
DROP TABLE IF EXISTS visited_country;

ALTER TABLE trip_card          DROP COLUMN country_name;
ALTER TABLE planner_city       DROP COLUMN country_name;
ALTER TABLE group_travel_plan  DROP COLUMN country_name;

-- 확인: 빈 결과가 나와야 한다
-- SELECT table_name, column_name FROM information_schema.columns
--  WHERE column_name = 'country_name'
--    AND table_name IN ('trip_card', 'planner_city', 'group_travel_plan');
-- SELECT table_name FROM information_schema.tables WHERE table_name = 'visited_country';
