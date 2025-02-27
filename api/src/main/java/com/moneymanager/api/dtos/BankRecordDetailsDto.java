package com.moneymanager.api.dtos;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BankRecordDetailsDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Double amount;
    private String name;
    private Set<FinancialTransactionDto> financialTransactions = new HashSet<>();
}
