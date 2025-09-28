package com.moneymanager.api.models.test;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;

import java.util.Set;

public class TestUserEntity extends UserEntity {
    public TestUserEntity(Long id, String email, String firstName, String lastName, String username, String password, Set<Role> roles, Set<Account> accounts) {
        super(email, firstName, lastName, username, password, roles);
        this.id = id;
        this.accounts = accounts;
    }
}
