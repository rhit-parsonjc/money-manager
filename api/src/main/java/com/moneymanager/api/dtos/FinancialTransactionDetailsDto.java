package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FinancialTransactionDetailsDto {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private Double amount;
    private String name;
    private List<BankRecordDto> bankRecords;
    private List<FileAttachmentDto> fileAttachments;
}
