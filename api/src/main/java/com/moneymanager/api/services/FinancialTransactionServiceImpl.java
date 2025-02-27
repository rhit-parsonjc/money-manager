package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.dtos.FinancialTransactionDetailsDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {
    private final FinancialTransactionRepository financialTransactionRepository;
    private final BankRecordRepository bankRecordRepository;

    private FinancialTransactionDto mapFinancialTransactionToDto(FinancialTransaction financialTransaction) {
        FinancialTransactionDto financialTransactionDto = new FinancialTransactionDto();
        financialTransactionDto.setId(financialTransaction.getId());
        financialTransactionDto.setYear(financialTransaction.getYear());
        financialTransactionDto.setMonth(financialTransaction.getMonth());
        financialTransactionDto.setDay(financialTransaction.getDay());
        financialTransactionDto.setAmount(financialTransaction.getAmount());
        financialTransactionDto.setName(financialTransaction.getName());
        return financialTransactionDto;
    }

    private List<FinancialTransactionDto> mapFinancialTransactionsToDtos(List<FinancialTransaction> financialTransactions) {
        return financialTransactions.stream().map(this::mapFinancialTransactionToDto).toList();
    }

    private BankRecordDto mapBankRecordToDto(BankRecord bankRecord) {
        BankRecordDto bankRecordDto = new BankRecordDto();
        bankRecordDto.setId(bankRecord.getId());
        bankRecordDto.setYear(bankRecord.getYear());
        bankRecordDto.setMonth(bankRecord.getMonth());
        bankRecordDto.setDay(bankRecord.getDay());
        bankRecordDto.setAmount(bankRecord.getAmount());
        bankRecordDto.setName(bankRecord.getName());
        return bankRecordDto;
    }

    private FinancialTransactionDetailsDto mapFinancialTransactionToDetailsDto(FinancialTransaction financialTransaction) {
        FinancialTransactionDetailsDto financialTransactionDetailsDto = new FinancialTransactionDetailsDto();
        financialTransactionDetailsDto.setId(financialTransaction.getId());
        financialTransactionDetailsDto.setYear(financialTransaction.getYear());
        financialTransactionDetailsDto.setMonth(financialTransaction.getMonth());
        financialTransactionDetailsDto.setDay(financialTransaction.getDay());
        financialTransactionDetailsDto.setAmount(financialTransaction.getAmount());
        financialTransactionDetailsDto.setName(financialTransaction.getName());
        Set<BankRecordDto> bankRecordDtos = financialTransaction
                .getBankRecords()
                .stream()
                .map(this::mapBankRecordToDto)
                .collect(Collectors.toSet());
        financialTransactionDetailsDto.setBankRecords(bankRecordDtos);
        return financialTransactionDetailsDto;
    }

    private FinancialTransaction mapFinancialTransactionRequestToTransaction(FinancialTransactionRequest financialTransactionRequest) {
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setYear(financialTransactionRequest.getYear());
        financialTransaction.setMonth(financialTransactionRequest.getMonth());
        financialTransaction.setDay(financialTransactionRequest.getDay());
        financialTransaction.setAmount(financialTransactionRequest.getAmount());
        financialTransaction.setName(financialTransactionRequest.getName());
        return financialTransaction;
    }

    @Override
    public FinancialTransactionDto createFinancialTransaction(FinancialTransactionRequest request) {
        FinancialTransaction financialTransaction = mapFinancialTransactionRequestToTransaction(request);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        return mapFinancialTransactionToDto(savedFinancialTransaction);
    }

    @Override
    public FinancialTransactionDetailsDto getFinancialTransactionById(Long id) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isPresent()) {
            FinancialTransaction financialTransaction = financialTransactionOptional.get();
            return mapFinancialTransactionToDetailsDto(financialTransaction);
        } else {
            throw new ResourceNotFoundException("Financial transaction not found");
        }
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactions() {
        return mapFinancialTransactionsToDtos(financialTransactionRepository.findAll());
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForYear(Integer year) {
        return mapFinancialTransactionsToDtos(financialTransactionRepository.findByYear(year));
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForMonth(Integer year, Integer month) {
        return mapFinancialTransactionsToDtos(financialTransactionRepository.findByYearAndMonth(year, month));
    }

    @Override
    public List<FinancialTransactionDto> getFinancialTransactionsForDay(Integer year, Integer month, Integer day) {
        return mapFinancialTransactionsToDtos(financialTransactionRepository.findByYearAndMonthAndDay(year, month, day));
    }

    @Override
    public FinancialTransactionDto updateFinancialTransaction(Long id, FinancialTransactionRequest request) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty()) {
            throw new ResourceNotFoundException("Financial transaction not found");
        }
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        financialTransaction.setYear(request.getYear());
        financialTransaction.setMonth(request.getMonth());
        financialTransaction.setDay(request.getDay());
        financialTransaction.setAmount(request.getAmount());
        financialTransaction.setName(request.getName());
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        return mapFinancialTransactionToDto(savedFinancialTransaction);
    }

    @Override
    public void deleteFinancialTransaction(Long id) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(id);
        if (financialTransactionOptional.isEmpty()) {
            throw new ResourceNotFoundException("Financial transaction not found");
        }
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        financialTransaction.getBankRecords().clear();
        financialTransactionRepository.deleteById(id);
    }

    @Override
    public void deleteFinancialTransactions() {
        List<FinancialTransaction> financialTransactions = financialTransactionRepository.findAll();
        for (FinancialTransaction financialTransaction : financialTransactions) {
            financialTransaction.getBankRecords().clear();
        }
        financialTransactionRepository.deleteAll();
    }
}
