-- 명세서에서 빠진 기능의 테이블 정리
--
-- ddl-auto=update 는 엔티티를 지워도 테이블을 드롭하지 않으므로 수동으로 실행한다.
-- 되돌릴 수 없으니 실행 전에 반드시 데이터를 확인할 것:
--
--   SELECT 'board', COUNT(*) FROM board
--   UNION ALL SELECT 'comment', COUNT(*) FROM comment
--   UNION ALL SELECT 'post_like', COUNT(*) FROM post_like
--   UNION ALL SELECT 'post_image', COUNT(*) FROM post_image
--   UNION ALL SELECT 'review', COUNT(*) FROM review
--   UNION ALL SELECT 'trip', COUNT(*) FROM trip
--   UNION ALL SELECT 'trip_place', COUNT(*) FROM trip_place
--   UNION ALL SELECT 'mission_db', COUNT(*) FROM mission_db;
--
-- 남겨둘 데이터가 있으면 먼저 백업한다:
--   mysqldump -u root PartTrip board comment post_like post_image review trip trip_place mission_db > backup.sql
--
-- 적용:
--   mysql -u root --default-character-set=utf8mb4 PartTrip < drop_out_of_spec_tables.sql

-- 커뮤니티
DROP TABLE IF EXISTS post_like;
DROP TABLE IF EXISTS post_image;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS trip_place;
DROP TABLE IF EXISTS trip;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS board;

-- 미션
DROP TABLE IF EXISTS mission_db;

-- 업로드된 커뮤니티 이미지 디렉토리도 함께 정리한다:
--   rm -rf uploads/community/
