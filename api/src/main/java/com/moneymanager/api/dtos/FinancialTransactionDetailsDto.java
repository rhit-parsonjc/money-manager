package com.moneymanager.api.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FinancialTransactionDetailsDto is returned in place of FinancialTransactionDetails
 * It contains linked bank records and file attachments
 */

@Data
@AllArgsConstructor
public class FinancialTransactionDetailsDto {
    private Long id;
    private Short year;
    private Byte month;
    private Byte day;
    private Long amount;
    private String name;
    private List<BankRecordDto> bankRecords;
    private List<FileAttachmentDto> fileAttachments;
}
