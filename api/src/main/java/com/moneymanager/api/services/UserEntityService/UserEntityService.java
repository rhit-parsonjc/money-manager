package com.moneymanager.api.services.UserEntityService;

import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.requests.RegisterRequest;
import com.moneymanager.api.requests.UserUpdateRequest;

import java.util.Optional;

public interface UserEntityService {
    void createUser(RegisterRequest registerRequest);
    Optional<UserEntity> getAuthenticatedUser();
    UserEntity getAuthenticatedUserOrThrow();
    UserEntity updateUser(UserUpdateRequest userUpdateRequest);
    void deleteUser();
}
