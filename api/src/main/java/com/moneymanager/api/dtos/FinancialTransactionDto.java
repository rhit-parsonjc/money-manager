package com.moneymanager.api.dtos;

import lombok.Data;

@Data
public class FinancialTransactionDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Double amount;
    private String name;
}
