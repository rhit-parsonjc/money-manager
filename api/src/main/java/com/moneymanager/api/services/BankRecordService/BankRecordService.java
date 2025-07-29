package com.moneymanager.api.services.BankRecordService;

import java.util.List;

import com.moneymanager.api.models.BankRecord;
import com.moneymanager.api.requests.BankRecordRequest;

public interface BankRecordService {
    BankRecord createBankRecord(Long accountId, BankRecordRequest request);

    BankRecord getBankRecordById(Long accountId, Long id);
    List<BankRecord> getBankRecords(Long accountId);
    List<BankRecord> getBankRecordsForYear(Long accountId, Short yearValue);
    List<BankRecord> getBankRecordsForMonth(Long accountId, Short yearValue, Byte monthValue);
    List<BankRecord> getBankRecordsForDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);

    BankRecord updateBankRecord(Long accountId, Long id, BankRecordRequest request);

    void deleteBankRecord(Long accountId, Long id);
    void deleteBankRecords(Long accountId);
}
