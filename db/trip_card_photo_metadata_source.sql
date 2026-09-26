-- 사진 촬영 정보 직접 지정 (#147)
--
-- EXIF 에서 읽은 값과 사용자가 지도에서 고른 값을 구분하는 컬럼을 추가한다.
-- EXIF 유래는 영구 잠금, 비어 있던 값만 채우거나 다시 고르게 한다.
--
-- 실행 순서: 배포 전에 한 번, 배포 직후에 한 번 더. UPDATE 는 멱등해서 여러 번 돌려도 된다.
--  - 배포 전에 컬럼을 만들어 두면 Hibernate 가 enum CHECK 조건을 붙이지 않는다.
--  - 배포가 먼저 됐으면 아래 DROP 두 줄이 그 조건을 걷어낸다.
--    ddl-auto=update 는 한 번 만들어진 CHECK 를 고치지 않아서, 나중에 출처 값을
--    하나라도 늘리면 알림 때(#195)와 똑같이 INSERT 가 조용히 막힌다.
--  - 배포 후에 한 번 더 돌리는 이유는, 그 사이에 올라온 사진이 EXIF 표시 없이
--    남으면 남의 진짜 GPS 좌표를 사용자가 고칠 수 있게 되기 때문이다.
--
-- 로컬 DB 로 개발하는 사람도 각자 한 번 실행해야 한다.

ALTER TABLE trip_card_photo ADD COLUMN IF NOT EXISTS location_source varchar(16);
ALTER TABLE trip_card_photo ADD COLUMN IF NOT EXISTS taken_at_source varchar(16);
ALTER TABLE trip_card_photo ADD COLUMN IF NOT EXISTS place_name      varchar(255);

ALTER TABLE trip_card_photo DROP CONSTRAINT IF EXISTS trip_card_photo_location_source_check;
ALTER TABLE trip_card_photo DROP CONSTRAINT IF EXISTS trip_card_photo_taken_at_source_check;

-- 기존 행은 전부 EXIF 유래다. 지금까지 좌표와 촬영 시각을 넣는 경로는
-- 업로드 시 EXIF 읽기(TripCardEntryServiceImpl.addEntry)뿐이었다.
UPDATE trip_card_photo SET location_source = 'EXIF'
 WHERE latitude IS NOT NULL AND longitude IS NOT NULL AND location_source IS NULL;

UPDATE trip_card_photo SET taken_at_source = 'EXIF'
 WHERE taken_at IS NOT NULL AND taken_at_source IS NULL;

-- 확인: 빈 결과와 0 이 나와야 한다
-- SELECT conname FROM pg_constraint
--  WHERE conrelid = 'trip_card_photo'::regclass AND contype = 'c';
-- SELECT COUNT(*) FROM trip_card_photo
--  WHERE (latitude IS NOT NULL AND location_source IS NULL)
--     OR (taken_at IS NOT NULL AND taken_at_source IS NULL);
