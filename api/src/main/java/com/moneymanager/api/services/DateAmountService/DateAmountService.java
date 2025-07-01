package com.moneymanager.api.services.DateAmountService;

import java.util.List;

import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;

public interface DateAmountService {
    DateAmount createDateAmount(Long accountId, DateAmountCreateRequest request);

    List<DateAmount> getDateAmounts(Long accountId);
    List<DateAmount> getDateAmountsByYear(Long accountId, Short year);
    List<DateAmount> getDateAmountsByMonth(Long accountId, Short year, Byte month);
    List<DateAmount> getDateAmountsByDay(Long accountId, Short year, Byte month, Byte day);

    DateAmount updateDateAmount(Long accountId, Short year, Byte month, Byte day, DateAmountUpdateRequest request);

    void deleteDateAmount(Long accountId, Short year, Byte month, Byte day);
    void deleteDateAmounts(Long accountId);
}
