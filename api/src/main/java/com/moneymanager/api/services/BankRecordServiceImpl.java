package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDetailsDto;
import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankRecordServiceImpl implements BankRecordService {
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

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

    private BankRecordDetailsDto mapBankRecordToDetailsDto(BankRecord bankRecord) {
        BankRecordDetailsDto bankRecordDetailsDto = new BankRecordDetailsDto();
        bankRecordDetailsDto.setId(bankRecord.getId());
        bankRecordDetailsDto.setYear(bankRecord.getYear());
        bankRecordDetailsDto.setMonth(bankRecord.getMonth());
        bankRecordDetailsDto.setDay(bankRecord.getDay());
        bankRecordDetailsDto.setAmount(bankRecord.getAmount());
        bankRecordDetailsDto.setName(bankRecord.getName());
        for (FinancialTransaction financialTransaction : bankRecord.getFinancialTransactions()) {
            bankRecordDetailsDto.getFinancialTransactions().add(mapFinancialTransactionToDto(financialTransaction));
        }
        return bankRecordDetailsDto;
    }

    private List<BankRecordDto> mapBankRecordsToDto(List<BankRecord> bankRecords) {
        return bankRecords.stream().map(this::mapBankRecordToDto).toList();
    }

    private BankRecord mapBankRecordRequestToRecord(BankRecordRequest bankRecordRequest) {
        BankRecord bankRecord = new BankRecord();
        bankRecord.setYear(bankRecordRequest.getYear());
        bankRecord.setMonth(bankRecordRequest.getMonth());
        bankRecord.setDay(bankRecordRequest.getDay());
        bankRecord.setAmount(bankRecordRequest.getAmount());
        bankRecord.setName(bankRecordRequest.getName());
        return bankRecord;
    }

    @Override
    public BankRecordDto createBankRecord(BankRecordRequest request) {
        BankRecord bankRecord = mapBankRecordRequestToRecord(request);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public BankRecordDetailsDto getBankRecordById(Long id) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isPresent()) {
            BankRecord bankRecord = bankRecordOptional.get();
            return mapBankRecordToDetailsDto(bankRecord);
        } else {
            throw new ResourceNotFoundException("Bank record not found");
        }
    }

    @Override
    public List<BankRecordDto> getBankRecords() {
        return mapBankRecordsToDto(bankRecordRepository.findAll());
    }

    @Override
    public List<BankRecordDto> getBankRecordsForYear(Integer year) {
        return mapBankRecordsToDto(bankRecordRepository.findByYear(year));
    }

    @Override
    public List<BankRecordDto> getBankRecordsForMonth(Integer year, Integer month) {
        return mapBankRecordsToDto(bankRecordRepository.findByYearAndMonth(year, month));
    }

    @Override
    public List<BankRecordDto> getBankRecordsForDay(Integer year, Integer month, Integer day) {
        return mapBankRecordsToDto(bankRecordRepository.findByYearAndMonthAndDay(year, month, day));
    }

    @Override
    public BankRecordDto updateBankRecord(Long id, BankRecordRequest request) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bank record not found");
        }
        BankRecord bankRecord = bankRecordOptional.get();
        bankRecord.setYear(request.getYear());
        bankRecord.setMonth(request.getMonth());
        bankRecord.setDay(request.getDay());
        bankRecord.setAmount(request.getAmount());
        bankRecord.setName(request.getName());
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public void deleteBankRecord(Long id) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bank record not found");
        }
        bankRecordRepository.deleteById(id);
    }

    @Override
    public void deleteBankRecords() {
        bankRecordRepository.deleteAll();
    }
}
