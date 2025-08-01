package com.moneymanager.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateAmountCreateRequest {
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
}
