package com.moneymanager.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DataOrErrorResponse represents a successful or unsuccessful response
 */

@Data
@AllArgsConstructor
public class DataOrErrorResponse {
    private boolean success;
    private Object data;
}
