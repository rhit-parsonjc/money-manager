package com.moneymanager.api.services.MapperService;

import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.*;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.FinancialTransactionRequest;

import java.util.List;

public interface MapperService {
    BankRecordDto mapBankRecordToDto(BankRecord bankRecord);
    BankRecordDetailsDto mapBankRecordToDetailsDto(BankRecord bankRecord);
    List<BankRecordDto> mapBankRecordsToDtos(List<BankRecord> bankRecords);
    BankRecord mapBankRecordRequestToRecord(Account account, BankRecordRequest bankRecordRequest);

    DateAmountDto mapDateAmountToDto(DateAmount dateAmount);
    List<DateAmountDto> mapDateAmountsToDtos(List<DateAmount> dateAmounts);
    DateAmount mapDateAmountRequestToAmount(Account account, DateAmountCreateRequest dateAmountCreateRequest);

    FinancialTransactionDto mapFinancialTransactionToDto(FinancialTransaction financialTransaction);
    FinancialTransactionDetailsDto mapFinancialTransactionToDetailsDto(FinancialTransaction financialTransaction);
    List<FinancialTransactionDto> mapFinancialTransactionsToDtos(List<FinancialTransaction> financialTransactions);
    FinancialTransaction mapFinancialTransactionRequestToTransaction(Account account, FinancialTransactionRequest financialTransactionRequest);

    FileAttachmentDto mapFileAttachmentToDto(FileAttachment fileAttachment);
    FileAttachmentDetailsDto mapFileAttachmentToDetailsDto(FileAttachment fileAttachment);
    List<FileAttachmentDto> mapFileAttachmentsToDtos(List<FileAttachment> fileAttachments);

    AccountDto mapAccountToDto(Account account);
    List<AccountDto> mapAccountsToDtos(List<Account> accounts);
    Account mapAccountRequestToAccount(UserEntity user, AccountRequest accountRequest);

    UserEntityDto mapUserEntityToDto(UserEntity userEntity);
}
