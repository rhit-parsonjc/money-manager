package com.moneymanager.api.services.BankRecordService;

import com.moneymanager.api.exceptions.InvalidRequestException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.models.Account;
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
public class BankRecordServiceImpl implements BankRecordService {
    private final AccountService accountService;
    private final BankRecordRepository bankRecordRepository;
    private final MapperService mapperService;
    private final ValidatorService validatorService;

    @Override
    public BankRecord createBankRecord(Long accountId, BankRecordRequest request) {
        Account account = accountService.getAccountById(accountId);
        validatorService.validate(request);
        BankRecord bankRecord = mapperService.mapBankRecordRequestToRecord(account, request);
        return bankRecordRepository.save(bankRecord);
    }

    @Override
    public BankRecord getBankRecordById(Long accountId, Long id) {
        accountService.getAccountById(accountId);
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        if (bankRecord.getAccount().getId().longValue() != accountId.longValue())
            throw new InvalidRequestException(InvalidRequestException.INCORRECT_ACCOUNT);
        return bankRecord;
    }

    @Override
    public List<BankRecord> getBankRecords(Long accountId) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountId(accountId);
    }

    @Override
    public List<BankRecord> getBankRecordsForYear(Long accountId, Short yearValue) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYearValue(accountId, yearValue);
    }

    @Override
    public List<BankRecord> getBankRecordsForMonth(Long accountId, Short yearValue, Byte monthValue) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYearValueAndMonthValue(accountId, yearValue, monthValue);
    }

    @Override
    public List<BankRecord> getBankRecordsForDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, yearValue, monthValue, dayValue);
    }

    @Override
    public BankRecord updateBankRecord(Long accountId, Long id, BankRecordRequest request) {
        accountService.getAccountById(accountId);
        validatorService.validate(request);
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        if (bankRecord.getAccount().getId() != accountId.longValue())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        bankRecord.update(request);
        return bankRecordRepository.save(bankRecord);
    }

    @Override
    public void deleteBankRecord(Long accountId, Long id) {
        accountService.getAccountById(accountId);
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        if (bankRecord.getAccount().getId().longValue() != accountId.longValue())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        bankRecordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBankRecords(Long accountId) {
        accountService.getAccountById(accountId);
        bankRecordRepository.deleteByAccountId(accountId);
    }
}
