package com.example.PartTrip.domain.photo.service;

import com.example.PartTrip.domain.signup.entity.UserEntity;

public interface CurrentUserProvider {
    UserEntity getCurrentUser();

    String getCurrentUserId();
}
