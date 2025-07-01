package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.FinancialTransaction;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {
    List<FinancialTransaction> findByAccountIdAndYear(Long accountId, Short year);
    List<FinancialTransaction> findByAccountIdAndYearAndMonth(Long accountId, Short year, Byte month);
    List<FinancialTransaction> findByAccountIdAndYearAndMonthAndDay(Long accountId, Short year, Byte month, Byte day);
    void deleteByAccountId(Long accountId);
}
