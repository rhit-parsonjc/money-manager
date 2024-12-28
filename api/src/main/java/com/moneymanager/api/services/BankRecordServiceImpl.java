package com.moneymanager.api.services;

import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankRecordServiceImpl implements BankRecordService {
    private final BankRecordRepository bankRecordRepository;

    private BankRecordDto mapBankRecordToDto(BankRecord bankRecord) {
        BankRecordDto bankRecordDto = new BankRecordDto();
        bankRecordDto.setId(bankRecord.getId());
        bankRecordDto.setYear(bankRecord.getYear());
        bankRecordDto.setMonth(bankRecord.getMonth());
        bankRecordDto.setDay(bankRecord.getDay());
        bankRecordDto.setAmount(bankRecord.getAmount());
        return bankRecordDto;
    }

    private BankRecord mapBankRecordRequestToRecord(BankRecordRequest bankRecordRequest) {
        BankRecord bankRecord = new BankRecord();
        bankRecord.setYear(bankRecordRequest.getYear());
        bankRecord.setMonth(bankRecordRequest.getMonth());
        bankRecord.setDay(bankRecordRequest.getDay());
        bankRecord.setAmount(bankRecordRequest.getAmount());
        return bankRecord;
    }

    @Override
    public BankRecordDto createBankRecord(BankRecordRequest request) {
        BankRecord bankRecord = mapBankRecordRequestToRecord(request);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public BankRecordDto getBankRecordById(Long id) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isPresent()) {
            BankRecord bankRecord = bankRecordOptional.get();
            return mapBankRecordToDto(bankRecord);
        } else {
            throw new ResourceNotFoundException("Bank record not found");
        }
    }

    @Override
    public List<BankRecordDto> getBankRecords() {
        List<BankRecord> bankRecords = bankRecordRepository.findAll();
        return bankRecords.stream().map(this::mapBankRecordToDto).toList();
    }

    @Override
    public BankRecordDto updateBankRecord(Long id, BankRecordRequest request) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (!bankRecordOptional.isPresent()) {
            throw new ResourceNotFoundException("Bank record not found");
        }
        BankRecord bankRecord = bankRecordOptional.get();
        bankRecord.setYear(request.getYear());
        bankRecord.setMonth(request.getMonth());
        bankRecord.setDay(request.getDay());
        bankRecord.setAmount(request.getAmount());
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord);
        return mapBankRecordToDto(savedBankRecord);
    }

    @Override
    public void deleteBankRecord(Long id) {
        bankRecordRepository.deleteById(id);
    }
}
