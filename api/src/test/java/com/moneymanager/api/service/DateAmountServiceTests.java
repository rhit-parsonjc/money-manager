package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestDateAmount;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.repositories.DateAmountRepository;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.DateAmountService.DateAmountServiceImpl;
import com.moneymanager.api.services.MapperService.MapperService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DateAmountServiceTests {
    @Mock
    private DateAmountRepository dateAmountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private DateAmountServiceImpl dateAmountService;

    private Long accountId;
    private Account account;

    private DateAmount dateAmount1;
    private DateAmount dateAmount2;
    private DateAmount dateAmount3;
    private DateAmount dateAmount4;

    public void verifyDateAmount(DateAmount dateAmount, long id, short yearValue, byte monthValue, byte dayValue, long amount, String accountName) {
        Assertions.assertEquals(id, dateAmount.getId());
        Assertions.assertEquals(yearValue, dateAmount.getYearValue());
        Assertions.assertEquals(monthValue, dateAmount.getMonthValue());
        Assertions.assertEquals(dayValue, dateAmount.getDayValue());
        Assertions.assertEquals(amount, dateAmount.getAmount());
        Assertions.assertEquals(accountName, dateAmount.getAccount().getName());
    }

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "spring@spring.boot", "Spring",
                "Boot", "SpringBoot", "pass1234", roleSet, new HashSet<Account>());
        accountId = 1L;
        account = new TestAccount(accountId, "Bank A", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);

        dateAmount1 = new TestDateAmount(1L, account, (short) 2025, (byte) 7, (byte) 31, 1500L);
        dateAmount2 = new TestDateAmount(2L, account, (short) 2025, (byte) 8, (byte) 1, 1800L);
        dateAmount3 = new TestDateAmount(3L, account, (short) 2025, (byte) 8, (byte) 2, 1550L);
        dateAmount4 = new TestDateAmount(4L, account, (short) 2026, (byte) 1, (byte) 1, 2000L);

        when(accountService.getAccountById(accountId)).thenReturn(account);
    }

    @Test
    public void DateAmountService_CreateDateAmount() {
        Long dateAmountId = 5L;
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest((short) 2024, (byte) 12, (byte) 31, 1700L);
        DateAmount dateAmount = new TestDateAmount(dateAmountId, account, (short) 2024, (byte) 12, (byte) 31, 1700L);
        List<DateAmount> emptyDateAmountList = new ArrayList<DateAmount>();
        when(dateAmountRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(
                accountId, (short) 2024, (byte) 12, (byte) 31
        )).thenReturn(emptyDateAmountList);
        when(mapperService.mapDateAmountRequestToAmount(account, dateAmountCreateRequest))
                .thenReturn(dateAmount);
        when(dateAmountRepository.save(dateAmount)).thenReturn(dateAmount);

        DateAmount createdDateAmount = dateAmountService.createDateAmount(accountId, dateAmountCreateRequest);

        Assertions.assertNotNull(createdDateAmount);
        verifyDateAmount(createdDateAmount, dateAmountId, (short) 2024, (byte) 12, (byte) 31, 1700L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2024, (byte) 12, (byte) 31);
        verify(mapperService, times(1)).mapDateAmountRequestToAmount(account, dateAmountCreateRequest);
        verify(dateAmountRepository, times(1)).save(dateAmount);
    }

    @Test
    public void DateAmount_CreateDateAmount_DuplicateDate() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest((short) 2025, (byte) 8, (byte) 1, 1700L);
        List<DateAmount> singletonDateAmountList = new ArrayList<DateAmount>();
        singletonDateAmountList.add(dateAmount2);
        when(dateAmountRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(
                accountId, (short) 2025, (byte) 8, (byte) 1
        )).thenReturn(singletonDateAmountList);

        Assertions.assertThrows(AlreadyExistsException.class,
                () -> dateAmountService.createDateAmount(accountId, dateAmountCreateRequest));
    }

    @Test
    public void DateAmountService_GetDateAmounts() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount1);
        dateAmountList.add(dateAmount2);
        dateAmountList.add(dateAmount3);
        dateAmountList.add(dateAmount4);
        when(dateAmountRepository.findByAccountId(accountId)).thenReturn(dateAmountList);

        List<DateAmount> foundDateAmountList = dateAmountService.getDateAmounts(accountId);

        Assertions.assertNotNull(foundDateAmountList);
        Assertions.assertEquals(4, foundDateAmountList.size());
        verifyDateAmount(foundDateAmountList.get(0), 1L, (short) 2025, (byte) 7, (byte) 31, 1500L, "Bank A");
        verifyDateAmount(foundDateAmountList.get(1), 2L, (short) 2025, (byte) 8, (byte) 1, 1800L, "Bank A");
        verifyDateAmount(foundDateAmountList.get(2), 3L, (short) 2025, (byte) 8, (byte) 2, 1550L, "Bank A");
        verifyDateAmount(foundDateAmountList.get(3), 4L, (short) 2026, (byte) 1, (byte) 1, 2000L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void DateAmountService_GetDateAmountsForYear() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount1);
        dateAmountList.add(dateAmount2);
        dateAmountList.add(dateAmount3);
        when(dateAmountRepository.findByAccountIdAndYearValue(accountId, (short) 2025))
                .thenReturn(dateAmountList);

        List<DateAmount> foundDateAmountList = dateAmountService
                .getDateAmountsForYear(accountId, (short) 2025);

        Assertions.assertNotNull(foundDateAmountList);
        Assertions.assertEquals(3, foundDateAmountList.size());
        verifyDateAmount(foundDateAmountList.get(0), 1L, (short) 2025, (byte) 7, (byte) 31, 1500L, "Bank A");
        verifyDateAmount(foundDateAmountList.get(1), 2L, (short) 2025, (byte) 8, (byte) 1, 1800L, "Bank A");
        verifyDateAmount(foundDateAmountList.get(2), 3L, (short) 2025, (byte) 8, (byte) 2, 1550L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountIdAndYearValue(accountId, (short) 2025);
    }

    @Test
    public void DateAmountService_GetDateAmountsForMonth() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount2);
        dateAmountList.add(dateAmount3);
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValue(accountId, (short) 2025, (byte) 8))
                .thenReturn(dateAmountList);

        List<DateAmount> foundDateAmountList = dateAmountService
                .getDateAmountsForMonth(accountId, (short) 2025, (byte) 8);

        Assertions.assertNotNull(foundDateAmountList);
        Assertions.assertEquals(2, foundDateAmountList.size());
        verifyDateAmount(foundDateAmountList.getFirst(), 2L, (short) 2025, (byte) 8, (byte) 1, 1800L, "Bank A");
        verifyDateAmount(foundDateAmountList.getLast(), 3L, (short) 2025, (byte) 8, (byte) 2, 1550L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountIdAndYearValueAndMonthValue(accountId, (short) 2025, (byte) 8);
    }

    @Test
    public void DateAmountService_GetDateAmountsForDay() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount2);
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 8, (byte) 1))
                .thenReturn(dateAmountList);

        List<DateAmount> foundDateAmountList = dateAmountService
                .getDateAmountsForDay(accountId, (short) 2025, (byte) 8, (byte) 1);

        Assertions.assertNotNull(foundDateAmountList);
        Assertions.assertEquals(1, foundDateAmountList.size());
        verifyDateAmount(foundDateAmountList.getFirst(), 2L, (short) 2025, (byte) 8, (byte) 1, 1800L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 8, (byte) 1);
    }

    @Test
    public void DateAmountService_UpdateDateAmount() {
        DateAmountUpdateRequest dateAmountUpdateRequest = new DateAmountUpdateRequest(1700L);
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount1);
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 7, (byte) 31))
                .thenReturn(dateAmountList);
        when(dateAmountRepository.save(dateAmount1)).thenReturn(dateAmount1);

        DateAmount updatedDateAmount = dateAmountService
                .updateDateAmount(accountId, (short) 2025, (byte) 7, (byte) 31, dateAmountUpdateRequest);

        Assertions.assertNotNull(updatedDateAmount);
        verifyDateAmount(updatedDateAmount, 1L, (short) 2025, (byte) 7, (byte) 31, 1700L, "Bank A");
        verify(dateAmountRepository, times(1)).findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 7, (byte) 31);
        verify(dateAmountRepository, times(1)).save(dateAmount1);
    }

    @Test
    public void DateAmountService_UpdateDateAmount_NonexistentAmount() {
        DateAmountUpdateRequest dateAmountUpdateRequest = new DateAmountUpdateRequest();
        dateAmountUpdateRequest.setAmount(1700L);
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 10, (byte) 22))
                .thenReturn(dateAmountList);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> dateAmountService.updateDateAmount(
                        accountId, (short) 2025, (byte) 10, (byte) 22, dateAmountUpdateRequest
                ));
    }

    @Test
    public void DateAmountService_DeleteDateAmount() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        dateAmountList.add(dateAmount3);
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 8, (byte) 2))
                .thenReturn(dateAmountList);
        doNothing().when(dateAmountRepository).deleteById(3L);

        dateAmountService.deleteDateAmount(accountId, (short) 2025, (byte) 8, (byte) 2);

        verify(dateAmountRepository, times(1)).deleteById(3L);
    }

    @Test
    public void DateAmountService_DeleteDateAmount_NonexistentAmount() {
        List<DateAmount> dateAmountList = new ArrayList<DateAmount>();
        when(dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 10, (byte) 22))
                .thenReturn(dateAmountList);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> dateAmountService.deleteDateAmount(accountId, (short) 2025, (byte) 10, (byte) 22));
    }

    @Test
    public void DateAmountService_DeleteDateAmounts() {
        doNothing().when(dateAmountRepository).deleteByAccountId(accountId);

        dateAmountService.deleteDateAmounts(accountId);

        verify(dateAmountRepository, times(1)).deleteByAccountId(accountId);
    }
}
