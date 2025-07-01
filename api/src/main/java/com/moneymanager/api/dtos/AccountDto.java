package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AccountDto is returned in place of Account
 */

@Data
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
}
