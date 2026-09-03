package com.example.PartTrip.planner.service;

import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PlannerScheduleLockService {

    private final UserRepository userRepository;

    public void lockUser(String userId) {
        lockUsers(List.of(userId));
    }

    public void lockUsers(Collection<String> userIds) {
        List<String> orderedUserIds = userIds.stream()
                .distinct()
                .sorted()
                .toList();
        if (orderedUserIds.isEmpty()) {
            throw new IllegalArgumentException("잠글 플래너 멤버가 없습니다.");
        }

        int lockedUserCount = userRepository
                .findAllByUserIdInForUpdate(orderedUserIds)
                .size();
        if (lockedUserCount != orderedUserIds.size()) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }
    }
}
