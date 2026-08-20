package com.example.PartTrip.photo.service;

import com.example.PartTrip.signup.entity.UserEntity;

public interface CurrentUserProvider {
    UserEntity getCurrentUser();

    String getCurrentUserId();
}
