package com.moneymanager.api.requests;

import lombok.Data;

/**
 * A FinancialTransactionRequest is used to create a FinancialTransaction
 */

@Data
public class FinancialTransactionRequest {
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
}
