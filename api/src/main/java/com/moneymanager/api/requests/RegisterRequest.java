package com.moneymanager.api.requests;

import lombok.Data;

/**
 * A RegisterRequest is used to create an account
 */

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
