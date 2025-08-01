package com.moneymanager.api.repositories;

import com.moneymanager.api.models.DateAmount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateAmountRepository extends JpaRepository<DateAmount, Long> {
    List<DateAmount> findByAccountId(Long accountId);
    List<DateAmount> findByAccountIdAndYearValue(Long accountId, Short yearValue);
    List<DateAmount> findByAccountIdAndYearValueAndMonthValue(Long accountId, Short yearValue, Byte monthValue);
    List<DateAmount> findByAccountIdAndYearValueAndMonthValueAndDayValue(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);
    void deleteByAccountId(Long accountId);
}
