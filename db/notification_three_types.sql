-- 알림을 세 종류로 줄인다 (#143)
--
-- 머지하고 한 번 직접 실행한다. 두 번 돌려도 안전하다.

-- Hibernate 가 notification 을 처음 만들 때 그때 있던 알림 종류만 받는 CHECK 를
-- 붙였고, ddl-auto=update 는 이미 만들어진 CHECK 를 고치지 않는다. 그대로 두면
-- 새로 추가한 TRIP_DAY_BEFORE · TODAY_SCHEDULE 이 저장되지 않아 알림이 아예 안 나간다.
-- 종류는 서버 enum 이 이미 검사하므로 DB 조건은 없앤다 (#195 에서 한 번 터졌다).
--
-- db/domestic_region_map.sql 1부에 같은 줄이 있다. 그 파일이 운영에 돌아갔는지
-- 확인할 방법이 없어 여기 한 번 더 둔다. IF EXISTS 라 이미 없으면 아무 일도 안 한다.
ALTER TABLE notification DROP CONSTRAINT IF EXISTS notification_type_check;

-- notification_category_check 는 그대로 둔다. 새 알림을 기존 VOTE 에 넣어
-- 카테고리 값이 늘지 않으므로 걸릴 일이 없다. 탭을 바꾸게 되면 그때 같이 지운다.


-- 확인: 빈 결과여야 한다
-- SELECT conname FROM pg_constraint
--  WHERE conrelid = 'notification'::regclass AND conname = 'notification_type_check';
