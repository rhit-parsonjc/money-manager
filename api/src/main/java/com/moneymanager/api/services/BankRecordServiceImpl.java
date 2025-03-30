package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDetailsDto;
import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankRecordServiceImpl implements BankRecordService {
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MapperService mapperService;

    @Override
    public BankRecordDto createBankRecord(BankRecordRequest request) {
        BankRecord bankRecord = mapperService.mapBankRecordRequestToRecord(request);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapperService.mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public BankRecordDetailsDto getBankRecordById(Long id) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        return mapperService.mapBankRecordToDetailsDto(bankRecord);
    }

    @Override
    public List<BankRecordDto> getBankRecords() {
        return mapperService.mapBankRecordsToDtos(bankRecordRepository.findAll());
    }

    @Override
    public List<BankRecordDto> getBankRecordsForYear(Integer year) {
        return mapperService.mapBankRecordsToDtos(bankRecordRepository.findByYear(year));
    }

    @Override
    public List<BankRecordDto> getBankRecordsForMonth(Integer year, Integer month) {
        return mapperService.mapBankRecordsToDtos(bankRecordRepository.findByYearAndMonth(year, month));
    }

    @Override
    public List<BankRecordDto> getBankRecordsForDay(Integer year, Integer month, Integer day) {
        return mapperService.mapBankRecordsToDtos(bankRecordRepository.findByYearAndMonthAndDay(year, month, day));
    }

    @Override
    public BankRecordDto updateBankRecord(Long id, BankRecordRequest request) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        bankRecord.update(request);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapperService.mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public void deleteBankRecord(Long id) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        bankRecordRepository.deleteById(id);
    }

    @Override
    public void deleteBankRecords() {
        bankRecordRepository.deleteAll();
    }
}
