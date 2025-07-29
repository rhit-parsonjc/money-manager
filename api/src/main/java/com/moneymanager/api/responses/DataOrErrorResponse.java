package com.moneymanager.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataOrErrorResponse {
    private boolean success;
    private Object data;
}
