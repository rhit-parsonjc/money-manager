package com.moneymanager.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DataOrErrorResponse {
    private boolean success;
    private Object data;
}
