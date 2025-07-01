package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DateAmountDto is returned in place of DateAmount
 */

@Data
@AllArgsConstructor
public class DateAmountDto {
    private Long id;
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
}
