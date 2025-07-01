package com.moneymanager.api.services.MapperService;

import java.util.List;
import org.springframework.stereotype.Service;

import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.*;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.FinancialTransactionRequest;

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
    public BankRecord mapBankRecordRequestToRecord(Account account, BankRecordRequest bankRecordRequest) {
        return new BankRecord(
                account,
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
    public DateAmount mapDateAmountRequestToAmount(Account account, DateAmountCreateRequest dateAmountCreateRequest) {
        return new DateAmount(
                account,
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
    public FinancialTransaction mapFinancialTransactionRequestToTransaction(Account account, FinancialTransactionRequest financialTransactionRequest) {
        return new FinancialTransaction(
                account,
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

    @Override
    public AccountDto mapAccountToDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getName()
        );
    }

    @Override
    public List<AccountDto> mapAccountsToDtos(List<Account> accounts) {
        return accounts.stream().map(this::mapAccountToDto).toList();
    }

    @Override
    public Account mapAccountRequestToAccount(UserEntity user, AccountRequest accountRequest) {
        return new Account(
                user,
                accountRequest.getName()
        );
    }
}
