package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * BankRecordDto is returned in place of BankRecord
 * It does not contain any linked items
 */

@Data
@AllArgsConstructor
public class BankRecordDto {
    private Long id;
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
}
