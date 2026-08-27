package com.example.PartTrip.global.security;

import com.example.PartTrip.signup.entity.UserEntity;

public interface CurrentUserProvider {
    UserEntity getCurrentUser();

    String getCurrentUserId();
}
