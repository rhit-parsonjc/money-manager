package com.moneymanager.api.requests;

import lombok.Data;

@Data
public class FinancialTransactionRequest {
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;
}
