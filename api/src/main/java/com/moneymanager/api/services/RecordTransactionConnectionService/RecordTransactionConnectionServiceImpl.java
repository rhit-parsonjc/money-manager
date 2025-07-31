package com.moneymanager.api.services.RecordTransactionConnectionService;

import java.util.Optional;

import com.moneymanager.api.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.*;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@Service
@RequiredArgsConstructor
public class RecordTransactionConnectionServiceImpl implements RecordTransactionConnectionService {
    private final UserEntityService userEntityService;
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    private BankRecord getBankRecord(UserEntity userEntity, Long recordId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        return bankRecordOptional.get();
    }

    private FinancialTransaction getFinancialTransaction(UserEntity userEntity, Long transactionId) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        return financialTransactionOptional.get();
    }

    private Long getAccountId(UserEntity userEntity, Item item) {
        Account itemAccount = item.getAccount();
        if (itemAccount.getUserEntity().getId().longValue() != userEntity.getId().longValue())
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return itemAccount.getId();
    }

    @Override
    public void createConnection(Long recordId, Long transactionId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        Long bankRecordAccountId = getAccountId(userEntity, bankRecord);
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        Long financialTransactionAccountId = getAccountId(userEntity, financialTransaction);
        if (bankRecordAccountId.longValue() != financialTransactionAccountId.longValue())
            throw new InvalidRequestException(InvalidRequestException.DIFFERENT_ACCOUNTS);
        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (bankRecordHasFinancialTransaction && financialTransactionHasBankRecord) {
            throw new AlreadyExistsException(AlreadyExistsException.CONNECTION_MESSAGE);
        } else if (bankRecordHasFinancialTransaction || financialTransactionHasBankRecord) {
            throw new InvalidStateException(InvalidStateException.ASYMMETRIC_CONNECTION);
        } else {
            bankRecord.getFinancialTransactions().add(financialTransaction);
            bankRecordRepository.save(bankRecord);
        }
    }

    @Override
    public void deleteConnection(Long recordId, Long transactionId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        Long bankRecordAccountId = getAccountId(userEntity, bankRecord);
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        Long financialTransactionAccountId = getAccountId(userEntity, financialTransaction);
        if (bankRecordAccountId.longValue() != financialTransactionAccountId.longValue())
            throw new InvalidRequestException(InvalidRequestException.DIFFERENT_ACCOUNTS);
        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (!bankRecordHasFinancialTransaction && !financialTransactionHasBankRecord) {
            throw new ResourceNotFoundException(ResourceNotFoundException.CONNECTION_MESSAGE);
        } else if (!bankRecordHasFinancialTransaction || !financialTransactionHasBankRecord) {
            throw new InvalidStateException(InvalidStateException.ASYMMETRIC_CONNECTION);
        } else {
            bankRecord.getFinancialTransactions().remove(financialTransaction);
            bankRecordRepository.save(bankRecord);
        }
    }
}
