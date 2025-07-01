package com.moneymanager.api.requests;

import lombok.Data;

/**
 * A DateAmountCreate request is used to create a DateAmount
 */

@Data
public class DateAmountCreateRequest {
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
}
