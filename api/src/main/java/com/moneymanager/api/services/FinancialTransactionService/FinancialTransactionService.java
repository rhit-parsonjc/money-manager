package com.moneymanager.api.services.FinancialTransactionService;

import java.util.List;

import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.requests.FinancialTransactionRequest;

public interface FinancialTransactionService {
    FinancialTransaction createFinancialTransaction(Long accountId, FinancialTransactionRequest request);

    FinancialTransaction getFinancialTransactionById(Long accountId, Long id);
    List<FinancialTransaction> getFinancialTransactions(Long accountId);
    List<FinancialTransaction> getFinancialTransactionsForYear(Long accountId, Short yearValue);
    List<FinancialTransaction> getFinancialTransactionsForMonth(Long accountId, Short yearValue, Byte monthValue);
    List<FinancialTransaction> getFinancialTransactionsForDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);

    FinancialTransaction updateFinancialTransaction(Long accountId, Long id, FinancialTransactionRequest request);

    void deleteFinancialTransaction(Long accountId, Long id);
    void deleteFinancialTransactions(Long accountId);
}
