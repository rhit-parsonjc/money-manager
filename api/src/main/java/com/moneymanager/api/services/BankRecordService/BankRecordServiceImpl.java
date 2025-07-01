package com.moneymanager.api.services.BankRecordService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.InvalidRequestException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

@Service
@RequiredArgsConstructor
public class BankRecordServiceImpl implements BankRecordService {
    private final AccountService accountService;
    private final BankRecordRepository bankRecordRepository;
    private final MapperService mapperService;

    @Override
    public BankRecord createBankRecord(Long accountId, BankRecordRequest request) {
        Account account = accountService.getAccountById(accountId);
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
        if (!Objects.equals(bankRecord.getAccount().getId(), accountId))
            throw new InvalidRequestException(InvalidRequestException.INCORRECT_ACCOUNT);
        return bankRecord;
    }

    @Override
    public List<BankRecord> getBankRecords(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return account.getBankRecords().stream().toList();
    }

    @Override
    public List<BankRecord> getBankRecordsForYear(Long accountId, Short year) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYear(accountId, year);
    }

    @Override
    public List<BankRecord> getBankRecordsForMonth(Long accountId, Short year, Byte month) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYearAndMonth(accountId, year, month);
    }

    @Override
    public List<BankRecord> getBankRecordsForDay(Long accountId, Short year, Byte month, Byte day) {
        accountService.getAccountById(accountId);
        return bankRecordRepository.findByAccountIdAndYearAndMonthAndDay(accountId, year, month, day);
    }

    @Override
    public BankRecord updateBankRecord(Long accountId, Long id, BankRecordRequest request) {
        accountService.getAccountById(accountId);
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(id);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        if (!Objects.equals(bankRecord.getAccount().getId(), accountId))
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
        if (!Objects.equals(bankRecord.getAccount().getId(), accountId))
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
