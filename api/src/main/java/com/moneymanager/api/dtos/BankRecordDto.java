package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankRecordDto {
    private Long id;
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;
}
