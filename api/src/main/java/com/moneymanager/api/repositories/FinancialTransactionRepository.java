package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.FinancialTransaction;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {
    List<FinancialTransaction> findByAccountId(Long accountId);
    List<FinancialTransaction> findByAccountIdAndYearValue(Long accountId, Short yearValue);
    List<FinancialTransaction> findByAccountIdAndYearValueAndMonthValue(Long accountId, Short yearValue, Byte monthValue);
    List<FinancialTransaction> findByAccountIdAndYearValueAndMonthValueAndDayValue(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);
    void deleteByAccountId(Long accountId);
}
