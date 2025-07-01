package com.moneymanager.api.services.UserEntityService;

import java.util.Optional;

import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.requests.RegisterRequest;

public interface UserEntityService {
    void createUser(RegisterRequest registerRequest);
    Optional<UserEntity> getAuthenticatedUser();
    UserEntity getAuthenticatedUserOrThrow();
}
