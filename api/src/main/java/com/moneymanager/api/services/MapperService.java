package com.moneymanager.api.services;

import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.FinancialTransactionRequest;

import java.util.List;

public interface MapperService {
    public BankRecordDto mapBankRecordToDto(BankRecord bankRecord);
    public BankRecordDetailsDto mapBankRecordToDetailsDto(BankRecord bankRecord);
    public List<BankRecordDto> mapBankRecordsToDtos(List<BankRecord> bankRecords);
    public BankRecord mapBankRecordRequestToRecord(BankRecordRequest bankRecordRequest);

    public DateAmountDto mapDateAmountToDto(DateAmount dateAmount);
    public List<DateAmountDto> mapDateAmountsToDtos(List<DateAmount> dateAmounts);
    public DateAmount mapDateAmountRequestToAmount(DateAmountCreateRequest dateAmountCreateRequest);

    public FinancialTransactionDto mapFinancialTransactionToDto(FinancialTransaction financialTransaction);
    public FinancialTransactionDetailsDto mapFinancialTransactionToDetailsDto(FinancialTransaction financialTransaction);
    public List<FinancialTransactionDto> mapFinancialTransactionsToDtos(List<FinancialTransaction> financialTransactions);
    public FinancialTransaction mapFinancialTransactionRequestToTransaction(FinancialTransactionRequest financialTransactionRequest);

    public FileAttachmentDto mapFileAttachmentToDto(FileAttachment fileAttachment);
    public FileAttachmentDetailsDto mapFileAttachmentToDetailsDto(FileAttachment fileAttachment);
    public List<FileAttachmentDto> mapFileAttachmentsToDtos(List<FileAttachment> fileAttachments);
}
