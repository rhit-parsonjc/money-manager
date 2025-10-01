package com.moneymanager.api.services.FinancialTransactionService;

import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

import com.moneymanager.api.services.ValidatorService.ValidatorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {
    private final AccountService accountService;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MapperService mapperService;
    private final ValidatorService validatorService;

    @Override
    public FinancialTransaction createFinancialTransaction(Long accountId, FinancialTransactionRequest request) {
        Account account = accountService.getAccountById(accountId);
        validatorService.validate(request);
        FinancialTransaction financialTransaction = mapperService.mapFinancialTransactionRequestToTransaction(account, request);
        return financialTransactionRepository.save(financialTransaction);
    }

    @Override
    public FinancialTransaction getFinancialTransactionById(Long accountId, Long id) {
        accountService.getAccountById(accountId);
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        if (financialTransaction.getAccount().getId().longValue() != accountId.longValue())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        return financialTransaction;
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactions(Long accountId) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountId(accountId);
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForYear(Long accountId, Short yearValue) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYearValue(accountId, yearValue);
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForMonth(Long accountId, Short yearValue, Byte monthValue) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYearValueAndMonthValue(accountId, yearValue, monthValue);
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, yearValue, monthValue, dayValue);
    }

    @Override
    public FinancialTransaction updateFinancialTransaction(Long accountId, Long id, FinancialTransactionRequest request) {
        accountService.getAccountById(accountId);
        validatorService.validate(request);
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        if (financialTransaction.getAccount().getId().longValue() != accountId.longValue())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        financialTransaction.update(request);
        return financialTransactionRepository.save(financialTransaction);
    }

    @Override
    public void deleteFinancialTransaction(Long accountId, Long id) {
        accountService.getAccountById(accountId);
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        if (financialTransaction.getAccount().getId().longValue() != accountId.longValue())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        financialTransactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFinancialTransactions(Long accountId) {
        accountService.getAccountById(accountId);
        financialTransactionRepository.deleteByAccountId(accountId);
    }
}
