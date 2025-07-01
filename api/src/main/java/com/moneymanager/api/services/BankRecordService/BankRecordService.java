package com.moneymanager.api.services.BankRecordService;

import java.util.List;

import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.requests.BankRecordRequest;

public interface BankRecordService {
    BankRecord createBankRecord(Long accountId, BankRecordRequest request);

    BankRecord getBankRecordById(Long accountId, Long id);
    List<BankRecord> getBankRecords(Long accountId);
    List<BankRecord> getBankRecordsForYear(Long accountId, Short year);
    List<BankRecord> getBankRecordsForMonth(Long accountId, Short year, Byte month);
    List<BankRecord> getBankRecordsForDay(Long accountId, Short year, Byte month, Byte day);

    BankRecord updateBankRecord(Long accountId, Long id, BankRecordRequest request);

    void deleteBankRecord(Long accountId, Long id);
    void deleteBankRecords(Long accountId);
}
