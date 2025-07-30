package com.moneymanager.api.models.test;

import com.moneymanager.api.models.Role;

public class TestRole extends Role {
    public TestRole(Long id, String name) {
        super(name);
        this.id = id;
    }
}
