package com.moneymanager.api.repositories;

import com.moneymanager.api.models.BankRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRecordRepository extends JpaRepository<BankRecord, Long> {
    List<BankRecord> findByAccountId(Long accountId);
    List<BankRecord> findByAccountIdAndYearValue(Long accountId, Short yearValue);
    List<BankRecord> findByAccountIdAndYearValueAndMonthValue(Long accountId, Short yearValue, Byte monthValue);
    List<BankRecord> findByAccountIdAndYearValueAndMonthValueAndDayValue(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);
    void deleteByAccountId(Long accountId);
}
