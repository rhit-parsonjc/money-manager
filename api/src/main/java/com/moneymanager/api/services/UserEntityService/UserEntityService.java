package com.moneymanager.api.services.UserEntityService;

import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.requests.RegisterRequest;

import java.util.Optional;

public interface UserEntityService {
    void createUser(RegisterRequest registerRequest);
    Optional<UserEntity> getAuthenticatedUser();
    UserEntity getAuthenticatedUserOrThrow();
}
