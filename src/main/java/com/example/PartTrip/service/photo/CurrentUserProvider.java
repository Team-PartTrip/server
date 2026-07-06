package com.example.PartTrip.service.photo;

import com.example.PartTrip.entity.signup.UserEntity;

public interface CurrentUserProvider {
    UserEntity getCurrentUser();

    String getCurrentUserId();
}
