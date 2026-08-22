-- 명세서 · 디자인에 없는 기능의 테이블 정리 (2차)
--
-- 1차(drop_out_of_spec_tables.sql)에서 커뮤니티 · 미션을 정리했고,
-- ERD 를 명세서 기준으로 다시 만들면서 추가로 걸린 것들이다.
--
-- ddl-auto=update 는 엔티티를 지워도 테이블과 컬럼을 드롭하지 않으므로 수동 실행한다.
-- 되돌릴 수 없으니 실행 전에 반드시 확인할 것:
--
--   SELECT 'food_info', COUNT(*) FROM food_info
--   UNION ALL SELECT 'population_info', COUNT(*) FROM population_info
--   UNION ALL SELECT 'today_phrase', COUNT(*) FROM today_phrase
--   UNION ALL SELECT 'character_info', COUNT(*) FROM character_info
--   UNION ALL SELECT 'guide_camera_mission', COUNT(*) FROM guide_camera_mission;
--
-- 백업:
--   mysqldump -u root PartTrip food_info population_info today_phrase \
--     character_info guide_camera_mission > backup_2.sql
--
-- 적용:
--   mysql -u root --default-character-set=utf8mb4 PartTrip < drop_out_of_spec_tables_2.sql

DROP TABLE IF EXISTS guide_camera_mission;
DROP TABLE IF EXISTS character_info;
DROP TABLE IF EXISTS food_info;
DROP TABLE IF EXISTS population_info;
DROP TABLE IF EXISTS today_phrase;

-- user_manage 의 캐릭터 · 설문 컬럼
-- 캐릭터 기능과 설문(#54 에서 제거)이 사라져 더는 쓰이지 않는다.
ALTER TABLE user_manage
    DROP COLUMN character_id,
    DROP COLUMN user_level,
    DROP COLUMN character_point,
    DROP COLUMN survey_completed;
