package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FinancialTransactionDto is returned in place of FinancialTransaction
 * It does not contain any linked items
 */

@Data
@AllArgsConstructor
public class FinancialTransactionDto {
    private Long id;
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
}
