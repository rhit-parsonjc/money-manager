package com.moneymanager.api.requests;

import lombok.Data;

@Data
public class BankRecordRequest {
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;
}
