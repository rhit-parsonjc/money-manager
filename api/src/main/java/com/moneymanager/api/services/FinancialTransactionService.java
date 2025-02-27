package com.moneymanager.api.services;

import com.moneymanager.api.dtos.FinancialTransactionDetailsDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.requests.FinancialTransactionRequest;

import java.util.List;

public interface FinancialTransactionService {
    public FinancialTransactionDto createFinancialTransaction(FinancialTransactionRequest request);

    public FinancialTransactionDetailsDto getFinancialTransactionById(Long id);
    public List<FinancialTransactionDto> getFinancialTransactions();
    public List<FinancialTransactionDto> getFinancialTransactionsForYear(Integer year);
    public List<FinancialTransactionDto> getFinancialTransactionsForMonth(Integer year, Integer month);
    public List<FinancialTransactionDto> getFinancialTransactionsForDay(Integer year, Integer month, Integer day);

    public FinancialTransactionDto updateFinancialTransaction(Long id, FinancialTransactionRequest request);

    public void deleteFinancialTransaction(Long id);
    public void deleteFinancialTransactions();
}
