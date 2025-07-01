package com.moneymanager.api.requests;

import lombok.Data;

/**
 * A LoginRequest is used to log in to an account
 */

@Data
public class LoginRequest {
    private String username;
    private String password;
}
