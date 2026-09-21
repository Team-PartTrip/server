-- 투표 기능 삭제 (#161)
--
-- ddl-auto=update 는 테이블을 지우지 않는다. 머지하고 한 번 직접 실행한다.
-- 머지 전 서버를 다시 켜면 JPA 가 테이블을 다시 만든다.
--
-- 투표 중이던 플래너는 PLANNING 으로 돌린다. VOTING 이면 여행지 수정 ·
-- 참여가 막힌다 (PlannerTravelPlanService · PlannerMemberService).

UPDATE travel_group SET status = 'PLANNING' WHERE status = 'VOTING';

DROP TABLE IF EXISTS vote_record;
DROP TABLE IF EXISTS vote_option;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS planner_category_count;

-- 확인: 0 과 빈 결과가 나와야 한다
-- SELECT COUNT(*) FROM travel_group WHERE status = 'VOTING';
-- SELECT table_name FROM information_schema.tables
--  WHERE table_name IN ('vote_record', 'vote_option', 'vote', 'planner_category_count');
