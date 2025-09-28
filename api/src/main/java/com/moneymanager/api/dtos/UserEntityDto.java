package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntityDto {
    private String email;
    private String firstName;
    private String lastName;
    private String username;
}
