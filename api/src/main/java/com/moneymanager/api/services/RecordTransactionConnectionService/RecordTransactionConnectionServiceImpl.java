package com.moneymanager.api.services.RecordTransactionConnectionService;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordTransactionConnectionServiceImpl implements RecordTransactionConnectionService {
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    @Override
    public void createConnection(Long recordId, Long transactionId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException("Bank record not found");
        BankRecord bankRecord = bankRecordOptional.get();

        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException("Financial transaction not found");
        FinancialTransaction financialTransaction = financialTransactionOptional.get();

        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (bankRecordHasFinancialTransaction && financialTransactionHasBankRecord) {
            throw new AlreadyExistsException(AlreadyExistsException.CONNECTION_MESSAGE);
        } else if (bankRecordHasFinancialTransaction || financialTransactionHasBankRecord) {
            throw new RuntimeException("Invalid state");
        } else {
            bankRecord.getFinancialTransactions().add(financialTransaction);
            financialTransaction.getBankRecords().add(bankRecord);
            bankRecordRepository.save(bankRecord);
            financialTransactionRepository.save(financialTransaction);
        }
    }

    @Override
    public void deleteConnection(Long recordId, Long transactionId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException("Bank record not found");
        BankRecord bankRecord = bankRecordOptional.get();

        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException("Financial transaction not found");
        FinancialTransaction financialTransaction = financialTransactionOptional.get();

        boolean bankRecordHasFinancialTransaction = bankRecord.getFinancialTransactions().contains(financialTransaction);
        boolean financialTransactionHasBankRecord = financialTransaction.getBankRecords().contains(bankRecord);
        if (!bankRecordHasFinancialTransaction && !financialTransactionHasBankRecord) {
            throw new ResourceNotFoundException(ResourceNotFoundException.CONNECTION_MESSAGE);
        } else if (!bankRecordHasFinancialTransaction || !financialTransactionHasBankRecord) {
            throw new RuntimeException("Invalid state");
        } else {
            bankRecord.getFinancialTransactions().remove(financialTransaction);
            financialTransaction.getBankRecords().remove(bankRecord);
            bankRecordRepository.save(bankRecord);
            financialTransactionRepository.save(financialTransaction);
        }
    }
}
