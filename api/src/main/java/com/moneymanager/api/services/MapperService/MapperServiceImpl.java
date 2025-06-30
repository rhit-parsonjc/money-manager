package com.moneymanager.api.services.MapperService;

import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperServiceImpl implements MapperService {
    @Override
    public BankRecordDto mapBankRecordToDto(BankRecord bankRecord) {
        return new BankRecordDto(
                bankRecord.getId(),
                bankRecord.getYear(),
                bankRecord.getMonth(),
                bankRecord.getDay(),
                bankRecord.getAmount(),
                bankRecord.getName()
        );
    }

    @Override
    public BankRecordDetailsDto mapBankRecordToDetailsDto(BankRecord bankRecord) {
        return new BankRecordDetailsDto(
                bankRecord.getId(),
                bankRecord.getYear(),
                bankRecord.getMonth(),
                bankRecord.getDay(),
                bankRecord.getAmount(),
                bankRecord.getName(),
                mapFinancialTransactionsToDtos(bankRecord.getFinancialTransactions().stream().toList()),
                mapFileAttachmentsToDtos(bankRecord.getFileAttachments().stream().toList())
        );
    }

    @Override
    public List<BankRecordDto> mapBankRecordsToDtos(List<BankRecord> bankRecords) {
        return bankRecords.stream().map(this::mapBankRecordToDto).toList();
    }

    @Override
    public BankRecord mapBankRecordRequestToRecord(BankRecordRequest bankRecordRequest) {
        return new BankRecord(
                bankRecordRequest.getYear(),
                bankRecordRequest.getMonth(),
                bankRecordRequest.getDay(),
                bankRecordRequest.getAmount(),
                bankRecordRequest.getName()
        );
    }

    @Override
    public DateAmountDto mapDateAmountToDto(DateAmount dateAmount) {
        return new DateAmountDto(
                dateAmount.getId(),
                dateAmount.getYear(),
                dateAmount.getMonth(),
                dateAmount.getDay(),
                dateAmount.getAmount()
        );
    }

    @Override
    public List<DateAmountDto> mapDateAmountsToDtos(List<DateAmount> dateAmounts) {
        return dateAmounts.stream().map(this::mapDateAmountToDto).toList();
    }

    @Override
    public DateAmount mapDateAmountRequestToAmount(DateAmountCreateRequest dateAmountCreateRequest) {
        return new DateAmount(
                dateAmountCreateRequest.getYear(),
                dateAmountCreateRequest.getMonth(),
                dateAmountCreateRequest.getDay(),
                dateAmountCreateRequest.getAmount()
        );
    }

    @Override
    public FinancialTransactionDto mapFinancialTransactionToDto(FinancialTransaction financialTransaction) {
        return new FinancialTransactionDto(
                financialTransaction.getId(),
                financialTransaction.getYear(),
                financialTransaction.getMonth(),
                financialTransaction.getDay(),
                financialTransaction.getAmount(),
                financialTransaction.getName()
        );
    }

    @Override
    public FinancialTransactionDetailsDto mapFinancialTransactionToDetailsDto(FinancialTransaction financialTransaction) {
        return new FinancialTransactionDetailsDto(
                financialTransaction.getId(),
                financialTransaction.getYear(),
                financialTransaction.getMonth(),
                financialTransaction.getDay(),
                financialTransaction.getAmount(),
                financialTransaction.getName(),
                mapBankRecordsToDtos(financialTransaction.getBankRecords().stream().toList()),
                mapFileAttachmentsToDtos(financialTransaction.getFileAttachments().stream().toList())
        );
    }

    @Override
    public List<FinancialTransactionDto> mapFinancialTransactionsToDtos(List<FinancialTransaction> financialTransactions) {
        return financialTransactions.stream().map(this::mapFinancialTransactionToDto).toList();
    }

    @Override
    public FinancialTransaction mapFinancialTransactionRequestToTransaction(FinancialTransactionRequest financialTransactionRequest) {
        return new FinancialTransaction(
                financialTransactionRequest.getYear(),
                financialTransactionRequest.getMonth(),
                financialTransactionRequest.getDay(),
                financialTransactionRequest.getAmount(),
                financialTransactionRequest.getName()
        );
    }

    @Override
    public FileAttachmentDto mapFileAttachmentToDto(FileAttachment fileAttachment) {
        return new FileAttachmentDto(
                fileAttachment.getId(),
                fileAttachment.getName(),
                fileAttachment.getType(),
                fileAttachment.getSize()
        );
    }

    @Override
    public FileAttachmentDetailsDto mapFileAttachmentToDetailsDto(FileAttachment fileAttachment) {
        return new FileAttachmentDetailsDto(
                fileAttachment.getId(),
                fileAttachment.getName(),
                fileAttachment.getType(),
                fileAttachment.getSize(),
                fileAttachment.getContents()
        );
    }

    @Override
    public List<FileAttachmentDto> mapFileAttachmentsToDtos(List<FileAttachment> fileAttachments) {
        return fileAttachments.stream().map(this::mapFileAttachmentToDto).toList();
    }
}
