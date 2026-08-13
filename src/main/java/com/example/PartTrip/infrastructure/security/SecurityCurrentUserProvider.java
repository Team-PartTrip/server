package com.example.PartTrip.infrastructure.security;

import com.example.PartTrip.domain.photo.service.CurrentUserProvider;

import com.example.PartTrip.domain.signup.entity.UserEntity;
import com.example.PartTrip.domain.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityCurrentUserProvider implements CurrentUserProvider {

    private final UserRepository userRepository;

    @Override
    public UserEntity getCurrentUser() {
        return userRepository.findByUserId(getCurrentUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    @Override
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return authentication.getName();
    }
}
