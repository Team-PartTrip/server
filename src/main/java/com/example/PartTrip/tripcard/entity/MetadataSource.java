package com.example.PartTrip.tripcard.entity;

// 사진의 좌표·촬영 시각이 어디서 온 값인지 (Func-003-07)
//
// EXIF 는 사진 파일이 들고 있던 값이라 사용자가 고치지 못한다. 기록을 바꾸는 일이기 때문이다.
// 비어 있던 자리를 사용자가 채우면 MANUAL 이고, 이건 기한 없이 몇 번이든 다시 고를 수 있다.
// 잘못 찍은 장소를 나중에 알아차려도 고칠 수 있어야 한다.
public enum MetadataSource {
    EXIF,
    MANUAL
}
