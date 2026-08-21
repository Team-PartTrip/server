package com.example.PartTrip.signup.support;

import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

// 가입 시 사용할 기본 닉네임을 만든다.
//
// 이전에는 "사용자 " + (count() + 1) 을 썼는데 두 가지로 깨졌다.
//   1. 탈퇴가 생기면 count 가 줄어 이미 쓰인 번호가 다시 나온다
//   2. 동시에 가입하면 같은 count 를 읽어 같은 닉네임이 만들어진다
// 닉네임은 프로필 수정에서 중복 검사를 하므로 유일해야 한다.
@Component
@RequiredArgsConstructor
public class NickNameGenerator {

    // 헷갈리는 문자(0/O, 1/l/I)는 뺀다
    private static final char[] ALPHABET = "23456789abcdefghjkmnpqrstuvwxyz".toCharArray();
    private static final int SUFFIX_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 10;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;

    // 예: "여행자_a3f9k2"
    public String generate() {
        return generate("여행자");
    }

    // 구글 로그인처럼 쓰고 싶은 이름이 이미 있는 경우
    // 그대로 쓸 수 있으면 쓰고, 겹치면 접미사를 붙인다
    public String generateFrom(String preferred) {
        String base = sanitize(preferred);

        if (base.isEmpty()) {
            return generate();
        }

        if (!userRepository.existsByNickName(base)) {
            return base;
        }

        return generate(base);
    }

    private String generate(String base) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String candidate = base + "_" + randomSuffix();
            if (!userRepository.existsByNickName(candidate)) {
                return candidate;
            }
        }

        // 31^6 (약 8.9억) 조합에서 10번 연속 충돌은 사실상 일어나지 않는다.
        // 그래도 무한 루프 대신 명시적으로 실패시킨다.
        throw new IllegalStateException("닉네임 생성에 실패했습니다. 다시 시도해주세요.");
    }

    private String randomSuffix() {
        StringBuilder sb = new StringBuilder(SUFFIX_LENGTH);
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            sb.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        }
        return sb.toString();
    }

    // 닉네임 컬럼 길이(255)와 앞뒤 공백만 정리한다
    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() > 200 ? trimmed.substring(0, 200) : trimmed;
    }
}
