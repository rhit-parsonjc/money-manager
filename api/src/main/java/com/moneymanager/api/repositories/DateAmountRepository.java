package com.moneymanager.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moneymanager.api.models.DateAmount;

public interface DateAmountRepository extends JpaRepository<DateAmount, Long> {
    List<DateAmount> findByAccountId(Long accountId);
    List<DateAmount> findByAccountIdAndYearValue(Long accountId, Short yearValue);
    List<DateAmount> findByAccountIdAndYearValueAndMonthValue(Long accountId, Short yearValue, Byte monthValue);
    List<DateAmount> findByAccountIdAndYearValueAndMonthValueAndDayValue(Long accountId, Short yearValue, Byte monthValue, Byte dayValue);
    void deleteByAccountId(Long accountId);
}
