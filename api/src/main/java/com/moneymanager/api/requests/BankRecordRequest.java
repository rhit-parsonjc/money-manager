package com.moneymanager.api.requests;

import lombok.Data;

/**
 * A BankRecordRequest is used to create a BankRecord
 */

@Data
public class BankRecordRequest {
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
}
