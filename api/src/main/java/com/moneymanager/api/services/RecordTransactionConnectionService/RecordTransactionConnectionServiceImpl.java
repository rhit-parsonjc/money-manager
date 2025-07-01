package com.moneymanager.api.services.RecordTransactionConnectionService;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.*;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@Service
@RequiredArgsConstructor
public class RecordTransactionConnectionServiceImpl implements RecordTransactionConnectionService {
    private final AccountRepository accountRepository;
    private final UserEntityService userEntityService;
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    private BankRecord getBankRecord(UserEntity userEntity, Long recordId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        return bankRecordOptional.get();
    }

    private Long getAccountId(UserEntity userEntity, BankRecord bankRecord) {
        Account bankRecordAccount = bankRecord.getAccount();
        if (!Objects.equals(bankRecordAccount.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return bankRecordAccount.getId();
    }

    private FinancialTransaction getFinancialTransaction(UserEntity userEntity, Long transactionId) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        return financialTransactionOptional.get();
    }

    private Long getAccountId(UserEntity userEntity, FinancialTransaction financialTransaction) {
        Account financialTransactionAccount = financialTransaction.getAccount();
        if (!Objects.equals(financialTransactionAccount.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return financialTransactionAccount.getId();
    }

    @Override
    public void createConnection(Long recordId, Long transactionId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        Long bankRecordAccountId = getAccountId(userEntity, bankRecord);
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        Long financialTransactionAccountId = getAccountId(userEntity, financialTransaction);
        if (!Objects.equals(bankRecordAccountId, financialTransactionAccountId))
            throw new InvalidRequestException(InvalidRequestException.DIFFERENT_ACCOUNTS);
        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (bankRecordHasFinancialTransaction && financialTransactionHasBankRecord) {
            throw new AlreadyExistsException(AlreadyExistsException.CONNECTION_MESSAGE);
        } else if (bankRecordHasFinancialTransaction || financialTransactionHasBankRecord) {
            throw new InvalidStateException(InvalidStateException.ASYMMETRIC_CONNECTION);
        } else {
            bankRecord.getFinancialTransactions().add(financialTransaction);
            financialTransaction.getBankRecords().add(bankRecord);
            bankRecordRepository.save(bankRecord);
            financialTransactionRepository.save(financialTransaction);
        }
    }

    @Override
    public void deleteConnection(Long recordId, Long transactionId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        Long bankRecordAccountId = getAccountId(userEntity, bankRecord);
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        Long financialTransactionAccountId = getAccountId(userEntity, financialTransaction);
        if (!Objects.equals(bankRecordAccountId, financialTransactionAccountId))
            throw new InvalidRequestException(InvalidRequestException.DIFFERENT_ACCOUNTS);
        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (!bankRecordHasFinancialTransaction && !financialTransactionHasBankRecord) {
            throw new ResourceNotFoundException(ResourceNotFoundException.CONNECTION_MESSAGE);
        } else if (!bankRecordHasFinancialTransaction || !financialTransactionHasBankRecord) {
            throw new InvalidStateException(InvalidStateException.ASYMMETRIC_CONNECTION);
        } else {
            bankRecord.getFinancialTransactions().remove(financialTransaction);
            financialTransaction.getBankRecords().remove(bankRecord);
            bankRecordRepository.save(bankRecord);
            financialTransactionRepository.save(financialTransaction);
        }
    }
}
