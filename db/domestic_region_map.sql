-- 세계지도 → 대한민국 지도 (#162)
--
-- 포스트그레스 기준이다. db/kakao_login.sql 의 ALTER COLUMN ... DROP NOT NULL,
-- db/vote_record_multi_vote.sql 의 DROP CONSTRAINT IF EXISTS 와 같다.
--
-- 두 번에 나눠 돌린다. 한 번에 돌리면 배포와 시점을 맞춰야 하는데, 어느 쪽을
-- 먼저 해도 그 사이에 저장이 실패한다. SQL 이 먼저면 아직 떠 있는 예전 서버가
-- 없어진 country_name 에 쓰려다 실패하고, 배포가 먼저면 새 서버가 NOT NULL 인
-- country_name 을 안 채워서 실패한다. 나누면 그 구간이 사라진다.


-- ── 1부: 배포 전에 미리 돌린다 ────────────────────────────────────────
-- 예전 서버는 country_name 을 계속 쓰고 새 컬럼은 무시하므로 안 깨진다.

ALTER TABLE trip_card          ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);
ALTER TABLE planner_city       ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);
ALTER TABLE group_travel_plan  ADD COLUMN IF NOT EXISTS region_code VARCHAR(2);

-- 이게 1부의 핵심이다. NOT NULL 을 풀어 두면 새 서버가 country_name 을
-- 안 채워도 저장이 된다. 배포 시점을 SQL 에 맞출 필요가 없어진다.
ALTER TABLE trip_card          ALTER COLUMN country_name DROP NOT NULL;
ALTER TABLE planner_city       ALTER COLUMN country_name DROP NOT NULL;
ALTER TABLE group_travel_plan  ALTER COLUMN country_name DROP NOT NULL;

-- "새 지역 방문" 알림이 곧 그 지역을 알렸다는 기록이다. 같은 순간에 플래너 둘을
-- 확정해도 지역당 한 번만 나가도록 DB 가 막는다 (RecordNotificationListener)
CREATE UNIQUE INDEX IF NOT EXISTS uk_notification_region_visited
    ON notification (user_id, link_id)
 WHERE type = 'REGION_VISITED';


-- ── 2부: 새 서버를 올린 뒤에 돌린다. 며칠 뒤여도 된다 ─────────────────
-- 새 서버는 country_name 을 아예 읽지 않으므로 급할 것이 없다.
--
-- 국내 기록만 지도에 올린다. 해외 기록은 region_code 가 비어서 지도에서 빠지고,
-- 여행카드 자체는 그대로 남는다. 다녀온 기록을 서버가 지울 일은 아니다.
-- (시·도를 알 수 없으므로 자동으로 채워 줄 수 있는 값이 없다)
--
-- NOT NULL 은 끝까지 걸지 않는다. 엔티티는 planner_city · group_travel_plan 을
-- NOT NULL 로 선언하지만, 지도로 바꾸기 전에 만든 행은 시·도가 비어 있어서
-- SET NOT NULL 이 그 행들에 걸려 실패한다. 예전 플래너를 다 정리한 뒤에
-- 아래를 직접 실행하면 된다.
--   ALTER TABLE planner_city      ALTER COLUMN region_code SET NOT NULL;
--   ALTER TABLE group_travel_plan ALTER COLUMN region_code SET NOT NULL;

ALTER TABLE trip_card          DROP COLUMN IF EXISTS country_name;
ALTER TABLE planner_city       DROP COLUMN IF EXISTS country_name;
ALTER TABLE group_travel_plan  DROP COLUMN IF EXISTS country_name;

-- 방문 지역은 따로 저장하지 않는다. 여행카드를 시·도로 묶어 센다
DROP TABLE IF EXISTS visited_country;


-- 확인: 둘 다 빈 결과여야 한다
-- SELECT table_name, column_name FROM information_schema.columns
--  WHERE column_name = 'country_name'
--    AND table_name IN ('trip_card', 'planner_city', 'group_travel_plan');
-- SELECT table_name FROM information_schema.tables WHERE table_name = 'visited_country';
