package com.moneymanager.api.requests;

import lombok.Data;

@Data
public class BankRecordRequest {
    private Integer year;
    private Integer month;
    private Integer day;
    private Double amount;
    private String name;
}
