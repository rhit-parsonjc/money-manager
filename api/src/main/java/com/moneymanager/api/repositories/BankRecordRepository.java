package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.BankRecord;

public interface BankRecordRepository extends JpaRepository<BankRecord, Long> {
    List<BankRecord> findByAccountIdAndYear(Long accountId, Short year);
    List<BankRecord> findByAccountIdAndYearAndMonth(Long accountId, Short year, Byte month);
    List<BankRecord> findByAccountIdAndYearAndMonthAndDay(Long accountId, Short year, Byte month, Byte day);
    void deleteByAccountId(Long accountId);
}
