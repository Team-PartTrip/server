-- #127 한 카테고리에서 여러 곳에 투표한다
--
-- vote_record 의 유니크가 (vote_id, user_id) 에서 (option_id, user_id) 로 바뀌었다.
-- ddl-auto=update 는 새 제약은 만들지만 옛 제약은 지우지 않는다.
-- 옛 제약이 남아 있으면 두 번째 장소에 투표할 때 제약 위반으로 막힌다.
--
-- 새 서버를 배포하기 전에 한 번 돌린다. 여러 번 돌려도 된다.

ALTER TABLE vote_record DROP CONSTRAINT IF EXISTS uk_vote_record_vote_user;

-- 제약이 아니라 유니크 인덱스로 만들어진 경우를 위해
DROP INDEX IF EXISTS uk_vote_record_vote_user;
