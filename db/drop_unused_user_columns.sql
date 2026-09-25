-- 회원 테이블의 쓰지 않는 칸을 지운다 (#185)
--
-- 로그인을 OAuth 만 남기면서 비밀번호를, 국내 여행만 다루면서 국가를,
-- 여행 타입을 빼면서 theme_id 를 더 쓰지 않는다. 전화번호는 받은 적이 없다.
-- ddl-auto=update 는 칸을 지우지 않는다. 새 서버를 올리자마자 바로 실행한다.
-- 새 서버는 user_pwd(NOT NULL)를 채우지 않아서, 이 SQL 을 돌리기 전까지 새 가입이 실패한다.
-- 먼저 돌리면 반대로 예전 서버가 없는 칸에 쓰려다 실패한다.

ALTER TABLE user_manage DROP COLUMN IF EXISTS user_pwd;
ALTER TABLE user_manage DROP COLUMN IF EXISTS phone_number;
ALTER TABLE user_manage DROP COLUMN IF EXISTS my_country;
ALTER TABLE user_manage DROP COLUMN IF EXISTS theme_id;

DROP TABLE IF EXISTS travel_theme;

-- 확인: 빈 결과가 나와야 한다
-- SELECT column_name FROM information_schema.columns
--  WHERE table_name = 'user_manage'
--    AND column_name IN ('user_pwd', 'phone_number', 'my_country', 'theme_id');
