package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BankRecordDetailsDto {
    private Long id;
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;
    private List<FinancialTransactionDto> financialTransactions;
    private List<FileAttachmentDto> fileAttachments;
}
