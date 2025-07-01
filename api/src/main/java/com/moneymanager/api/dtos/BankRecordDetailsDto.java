package com.moneymanager.api.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * BankRecordDetailsDto is returned in place of BankRecord
 * It contains linked financial transactions and file attachments
 */

@Data
@AllArgsConstructor
public class BankRecordDetailsDto {
    private Long id;
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
    private List<FinancialTransactionDto> financialTransactions;
    private List<FileAttachmentDto> fileAttachments;
}
