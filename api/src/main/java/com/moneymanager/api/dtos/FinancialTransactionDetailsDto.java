package com.moneymanager.api.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinancialTransactionDetailsDto {
    private Long id;
    private Short yearValue;
    private Byte monthValue;
    private Byte dayValue;
    private Long amount;
    private String name;
    private List<BankRecordDto> bankRecords;
    private List<FileAttachmentDto> fileAttachments;
}
