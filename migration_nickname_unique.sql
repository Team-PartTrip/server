-- nick_name UNIQUE 제약 추가
--
-- 프로필 수정(ProfileService)은 existsByNickNameAndUserIdNot 으로 닉네임 중복을
-- 막고 있어 닉네임이 유일하다는 전제가 있다. 그런데 DB 에는 제약이 없었다.
-- ddl-auto=update 는 이미 존재하는 컬럼에 UNIQUE 를 붙여주지 않으므로 수동 적용한다.
--
-- 적용 전 반드시 중복을 확인할 것:
--   SELECT nick_name, COUNT(*) FROM user_manage GROUP BY nick_name HAVING COUNT(*) > 1;
--
-- 중복이 있으면 먼저 정리한 뒤 실행해야 한다.

ALTER TABLE user_manage ADD CONSTRAINT uk_user_manage_nick_name UNIQUE (nick_name);
