package com.moneymanager.api.services.FinancialTransactionService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {
    private final AccountService accountService;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MapperService mapperService;

    @Override
    public FinancialTransaction createFinancialTransaction(Long accountId, FinancialTransactionRequest request) {
        Account account = accountService.getAccountById(accountId);
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
        if (!Objects.equals(financialTransaction.getAccount().getId(), accountId))
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        return financialTransaction;
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactions(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return account.getFinancialTransactions().stream().toList();
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForYear(Long accountId, Short year) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYear(accountId, year);
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForMonth(Long accountId, Short year, Byte month) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYearAndMonth(accountId, year, month);
    }

    @Override
    public List<FinancialTransaction> getFinancialTransactionsForDay(Long accountId, Short year, Byte month, Byte day) {
        accountService.getAccountById(accountId);
        return financialTransactionRepository.findByAccountIdAndYearAndMonthAndDay(accountId, year, month, day);
    }

    @Override
    public FinancialTransaction updateFinancialTransaction(Long accountId, Long id, FinancialTransactionRequest request) {
        accountService.getAccountById(accountId);
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        if (!Objects.equals(financialTransaction.getAccount().getId(), accountId))
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
        if (!Objects.equals(financialTransaction.getAccount().getId(), accountId))
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
