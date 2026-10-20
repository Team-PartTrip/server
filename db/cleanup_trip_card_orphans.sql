-- 여행카드를 지울 때 남은 사진 · 장소 행을 지운다 (#187)
--
-- 고치기 전에는 카드 행만 지워서 자식 행이 남았다. 사진 파일은 이미 지워졌으므로
-- 남은 행은 없는 파일을 가리킨다. 여러 번 돌려도 된다.

DELETE FROM trip_card_photo p
 WHERE NOT EXISTS (SELECT 1 FROM trip_card c WHERE c.trip_card_id = p.trip_card_id);

DELETE FROM trip_card_place p
 WHERE NOT EXISTS (SELECT 1 FROM trip_card c WHERE c.trip_card_id = p.trip_card_id);
