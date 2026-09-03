package com.example.PartTrip.planner.service;

import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlannerScheduleLockServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private PlannerScheduleLockService service;

    @Test
    void locksDistinctUsersInStableOrder() {
        UserEntity first = user("a");
        UserEntity second = user("b");
        given(userRepository.findAllByUserIdInForUpdate(List.of("a", "b")))
                .willReturn(List.of(first, second));

        service.lockUsers(List.of("b", "a", "b"));

        verify(userRepository).findAllByUserIdInForUpdate(List.of("a", "b"));
    }

    @Test
    void rejectsWhenAnyUserCannotBeLocked() {
        given(userRepository.findAllByUserIdInForUpdate(List.of("missing")))
                .willReturn(List.of());

        assertThatThrownBy(() -> service.lockUser("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 정보를 찾을 수 없습니다.");
    }

    private UserEntity user(String userId) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        return user;
    }
}
