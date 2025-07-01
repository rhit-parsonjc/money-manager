package com.moneymanager.api.services.DateAmountService;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.repositories.DateAmountRepository;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

@Service
@RequiredArgsConstructor
public class DateAmountServiceImpl implements DateAmountService {
    private final AccountService accountService;
    private final DateAmountRepository dateAmountRepository;
    private final MapperService mapperService;

    private Optional<DateAmount> getDateAmountByDay(Account account, Short year, Byte month, Byte day) {
        List<DateAmount> dateAmounts = dateAmountRepository.findByAccountIdAndYearAndMonthAndDay(account.getId(), year, month, day);
        if (dateAmounts.isEmpty())
            return Optional.empty();
        else
            return Optional.of(dateAmounts.getFirst());
    }

    @Override
    public DateAmount createDateAmount(Long accountId, DateAmountCreateRequest request) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> existingDateAmountOptional = this.getDateAmountByDay(account, request.getYear(), request.getMonth(), request.getDay());
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
    public List<DateAmount> getDateAmountsByYear(Long accountId, Short year) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYear(accountId, year);
    }

    @Override
    public List<DateAmount> getDateAmountsByMonth(Long accountId, Short year, Byte month) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYearAndMonth(accountId, year, month);
    }

    @Override
    public List<DateAmount> getDateAmountsByDay(Long accountId, Short year, Byte month, Byte day) {
        accountService.getAccountById(accountId);
        return dateAmountRepository.findByAccountIdAndYearAndMonthAndDay(accountId, year, month, day);
    }

    @Override
    public DateAmount updateDateAmount(Long accountId, Short year, Byte month, Byte day, DateAmountUpdateRequest request) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> dateAmountOptional = this.getDateAmountByDay(account, year, month, day);
        if (dateAmountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.DATE_AMOUNT_MESSAGE);
        DateAmount dateAmount = dateAmountOptional.get();
        dateAmount.update(request);
        return  dateAmountRepository.save(dateAmount);
    }

    @Override
    public void deleteDateAmount(Long accountId, Short year, Byte month, Byte day) {
        Account account = accountService.getAccountById(accountId);
        Optional<DateAmount> dateAmountOptional = this.getDateAmountByDay(account, year, month, day);
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
