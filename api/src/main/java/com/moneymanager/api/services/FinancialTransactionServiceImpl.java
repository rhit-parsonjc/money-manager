package com.moneymanager.api.services;

import com.moneymanager.api.dtos.FinancialTransactionDetailsDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {
    private final FinancialTransactionRepository financialTransactionRepository;
    private final BankRecordRepository bankRecordRepository;
    private final MapperService mapperService;

    @Override
    public FinancialTransactionDto createFinancialTransaction(FinancialTransactionRequest request) {
        FinancialTransaction financialTransaction = mapperService.mapFinancialTransactionRequestToTransaction(request);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        return mapperService.mapFinancialTransactionToDto(savedFinancialTransaction);
    }

    @Override
    public FinancialTransactionDetailsDto getFinancialTransactionById(Long id) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        return mapperService.mapFinancialTransactionToDetailsDto(financialTransaction);
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactions() {
        return mapperService.mapFinancialTransactionsToDtos(financialTransactionRepository.findAll());
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForYear(Integer year) {
        return mapperService.mapFinancialTransactionsToDtos(financialTransactionRepository.findByYear(year));
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForMonth(Integer year, Integer month) {
        return mapperService.mapFinancialTransactionsToDtos(financialTransactionRepository.findByYearAndMonth(year, month));
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForDay(Integer year, Integer month, Integer day) {
        return mapperService.mapFinancialTransactionsToDtos(financialTransactionRepository.findByYearAndMonthAndDay(year, month, day));
    }

    @Override
    public FinancialTransactionDto updateFinancialTransaction(Long id, FinancialTransactionRequest request) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        financialTransaction.update(request);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        return mapperService.mapFinancialTransactionToDto(savedFinancialTransaction);
    }

    @Override
    public void deleteFinancialTransaction(Long id) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        financialTransactionRepository.deleteById(id);
    }

    @Override
    public void deleteFinancialTransactions() {
        financialTransactionRepository.deleteAll();
    }
}
