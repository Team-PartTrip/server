-- 세계지도 → 대한민국 지도 (#162)
--
-- ddl-auto=update 는 컬럼을 지우지도, NOT NULL 을 풀지도 않는다.
-- region_code 는 알아서 생기지만 country_name 은 NOT NULL 로 남아서,
-- 이 스크립트를 돌리기 전까지 플래너 · 여행카드 저장이 전부 실패한다.
-- 머지하고 한 번 직접 실행한다.
--
-- 포스트그레스 기준이다. ADD COLUMN IF NOT EXISTS 는 MySQL 8.0 에 없다.
-- db/kakao_login.sql 의 ALTER COLUMN ... DROP NOT NULL,
-- db/vote_record_multi_vote.sql 의 DROP CONSTRAINT IF EXISTS 도 마찬가지라
-- 이 레포의 db/*.sql 은 전부 포스트그레스로 돌린다.

-- 먼저 컬럼을 만든다. 서버를 아직 안 올렸다면 ddl-auto 가 만들어 둔다
ALTER TABLE trip_card          ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);
ALTER TABLE planner_city       ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);
ALTER TABLE group_travel_plan  ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);

-- NOT NULL 은 걸지 않는다. 엔티티는 planner_city · group_travel_plan 을
-- NOT NULL 로 선언하지만, 지도로 바꾸기 전에 만든 행은 시·도가 비어 있어서
-- SET NOT NULL 이 그 행들에 걸려 실패한다. 예전 플래너를 다 정리한 뒤에
-- 아래를 직접 실행하면 된다.
--   ALTER TABLE planner_city      ALTER COLUMN region_code SET NOT NULL;
--   ALTER TABLE group_travel_plan ALTER COLUMN region_code SET NOT NULL;

-- 국내 기록만 지도에 올린다. 해외 기록은 region_code 가 비어서 지도에서 빠지고,
-- 여행카드 자체는 그대로 남는다. 다녀온 기록을 서버가 지울 일은 아니다.
-- (시·도를 알 수 없으므로 자동으로 채워 줄 수 있는 값이 없다)

-- 방문 지역은 따로 저장하지 않는다. 여행카드를 시·도로 묶어 센다
DROP TABLE IF EXISTS visited_country;

-- "새 지역 방문" 알림이 곧 그 지역을 알렸다는 기록이다. 같은 순간에 플래너 둘을
-- 확정해도 지역당 한 번만 나가도록 DB 가 막는다 (RecordNotificationListener)
CREATE UNIQUE INDEX IF NOT EXISTS uk_notification_region_visited
    ON notification (user_id, link_id)
 WHERE type = 'REGION_VISITED';

ALTER TABLE trip_card          DROP COLUMN country_name;
ALTER TABLE planner_city       DROP COLUMN country_name;
ALTER TABLE group_travel_plan  DROP COLUMN country_name;

-- 확인: 빈 결과가 나와야 한다
-- SELECT table_name, column_name FROM information_schema.columns
--  WHERE column_name = 'country_name'
--    AND table_name IN ('trip_card', 'planner_city', 'group_travel_plan');
-- SELECT table_name FROM information_schema.tables WHERE table_name = 'visited_country';
