package com.moneymanager.api.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
