-- 카카오 로그인 (#142)
--
-- 카카오는 이메일 동의가 없으면 이메일을 주지 않는다. 그래서 user_mail 을
-- 비워 둘 수 있어야 한다. ddl-auto=update 는 이미 걸린 NOT NULL 을 지우지
-- 않으므로 한 번 직접 실행한다.
--
-- unique 제약은 그대로 둔다. 포스트그레스는 NULL 을 여러 행에 허용해서
-- 이메일 없는 카카오 계정이 여러 개여도 부딪히지 않는다.

ALTER TABLE user_manage ALTER COLUMN user_mail DROP NOT NULL;

-- 확인: 이 줄이 is_nullable = YES 로 나와야 한다
-- SELECT column_name, is_nullable FROM information_schema.columns
--  WHERE table_name = 'user_manage' AND column_name = 'user_mail';
