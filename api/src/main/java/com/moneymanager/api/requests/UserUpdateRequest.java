package com.moneymanager.api.requests;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
