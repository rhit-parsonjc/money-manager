package com.moneymanager.api.services.DateAmountService;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.repositories.DateAmountRepository;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DateAmountServiceImpl implements DateAmountService {
    private final AccountService accountService;
    private final DateAmountRepository dateAmountRepository;
    private final MapperService mapperService;

    private Optional<DateAmount> getDateAmountByDay(Account account, Short yearValue, Byte monthValue, Byte dayValue) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(account.getId(), yearValue, monthValue, dayValue);
        if (dateAmounts.isEmpty())
            return Optional.empty();
        else
            return Optional.of(dateAmounts.getFirst());
    }

    @Override
    public DateAmount createDateAmount(Long accountId, DateAmountCreateRequest request) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> existingDateAmountOptional = this.getDateAmountByDay(account, request.getYearValue(), request.getMonthValue(), request.getDayValue());
        if (existingDateAmountOptional.isPresent())
            throw new AlreadyExistsException(AlreadyExistsException.DATE_AMOUNT_MESSAGE);
        DateAmount dateAmount = mapperService.mapDateAmountRequestToAmount(account, request);
        return dateAmountRepository.save(dateAmount);
    }

    @Override
    public List<DateAmount> getDateAmounts(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountId(accountId);
    }

    @Override
    public List<DateAmount> getDateAmountsForYear(Long accountId, Short yearValue) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYearValue(accountId, yearValue);
    }

    @Override
    public List<DateAmount> getDateAmountsForMonth(Long accountId, Short yearValue, Byte monthValue) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYearValueAndMonthValue(accountId, yearValue, monthValue);
    }

    @Override
    public List<DateAmount> getDateAmountsForDay(Long accountId, Short yearValue, Byte monthValue, Byte dayValue) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, yearValue, monthValue, dayValue);
    }

    @Override
    public DateAmount updateDateAmount(Long accountId, Short yearValue, Byte monthValue, Byte dayValue, DateAmountUpdateRequest request) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> dateAmountOptional = this.getDateAmountByDay(account, yearValue, monthValue, dayValue);
        if (dateAmountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.DATE_AMOUNT_MESSAGE);
        DateAmount dateAmount = dateAmountOptional.get();
        dateAmount.update(request);
        return  dateAmountRepository.save(dateAmount);
    }

    @Override
    public void deleteDateAmount(Long accountId, Short yearValue, Byte monthValue, Byte dayValue) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> dateAmountOptional = this.getDateAmountByDay(account, yearValue, monthValue, dayValue);
        if (dateAmountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.DATE_AMOUNT_MESSAGE);
        DateAmount dateAmount = dateAmountOptional.get();
        dateAmountRepository.deleteById(dateAmount.getId());
    }

    @Override
    @Transactional
    public void deleteDateAmounts(Long accountId) {
        accountService.getAccountById(accountId);
        dateAmountRepository.deleteByAccountId(accountId);
    }
}
