package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestFinancialTransaction;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.FinancialTransactionService.FinancialTransactionServiceImpl;
import com.moneymanager.api.services.MapperService.MapperService;

import com.moneymanager.api.services.ValidatorService.ValidatorService;
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
public class FinancialTransactionServiceTests {
    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MapperService mapperService;

    @Mock
    private ValidatorService validatorService;

    @InjectMocks
    private FinancialTransactionServiceImpl financialTransactionService;

    private Long accountId;
    private Account account;

    private FinancialTransaction financialTransaction1;
    private FinancialTransaction financialTransaction2;

    private void verifyFinancialTransaction(FinancialTransaction financialTransaction, long id, short yearValue,
                                            byte monthValue, byte dayValue, long amount, String name, String accountName) {
        Assertions.assertEquals(id, financialTransaction.getId());
        Assertions.assertEquals(yearValue, financialTransaction.getYearValue());
        Assertions.assertEquals(monthValue, financialTransaction.getMonthValue());
        Assertions.assertEquals(dayValue, financialTransaction.getDayValue());
        Assertions.assertEquals(amount, financialTransaction.getAmount());
        Assertions.assertEquals(name, financialTransaction.getName());
        Assertions.assertEquals(accountName, financialTransaction.getAccount().getName());
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
        financialTransaction1 = new TestFinancialTransaction(1L, account, (short) 2025, (byte) 9, (byte) 25, -2200L,
                "Game 1", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransaction2 = new TestFinancialTransaction(2L, account, (short) 2025, (byte) 10, (byte) 5, -525L,
                "Game 2", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction1);
        account.getFinancialTransactions().add(financialTransaction2);

        when(accountService.getAccountById(accountId)).thenReturn(account);
    }

    @Test
    public void FinancialTransactionService_CreateFinancialTransaction() {
        Long financialTransactionId = 3L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest(
                (short) 2025, (byte) 9, (byte) 15, -1350L, "Game 3");
        FinancialTransaction financialTransaction = new TestFinancialTransaction(financialTransactionId,
                account, (short) 2025, (byte) 9, (byte) 15, -1350L, "Game 3",
                new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        when(mapperService.mapFinancialTransactionRequestToTransaction(account, financialTransactionRequest))
                .thenReturn(financialTransaction);
        when(financialTransactionRepository.save(financialTransaction)).thenReturn(financialTransaction);

        FinancialTransaction createdFinancialTransaction = financialTransactionService
                .createFinancialTransaction(accountId, financialTransactionRequest);

        Assertions.assertNotNull(createdFinancialTransaction);
        verifyFinancialTransaction(createdFinancialTransaction, financialTransactionId, (short) 2025, (byte) 9, (byte) 15, -1350L, "Game 3", "Bank A");
        verify(validatorService, times(1)).validate(financialTransactionRequest);
        verify(mapperService, times(1)).mapFinancialTransactionRequestToTransaction(account, financialTransactionRequest);
        verify(financialTransactionRepository, times(1)).save(financialTransaction);
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactionById() {
        Long financialTransactionId = 1L;
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.of(financialTransaction1));

        FinancialTransaction foundFinancialTransaction = financialTransactionService
                .getFinancialTransactionById(accountId, financialTransactionId);

        Assertions.assertNotNull(foundFinancialTransaction);
        verifyFinancialTransaction(foundFinancialTransaction, financialTransactionId, (short) 2025, (byte) 9, (byte) 25, -2200L, "Game 1", "Bank A");
        verify(financialTransactionRepository, times(1)).findById(financialTransactionId);
    }


    @Test
    public void FinancialTransactionService_GetFinancialTransactionById_NonexistentTransaction() {
        Long financialTransactionId = 3L;
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> financialTransactionService.getFinancialTransactionById(accountId, financialTransactionId));
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactions() {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction1);
        financialTransactionList.add(financialTransaction2);
        when(financialTransactionRepository.findByAccountId(accountId))
                .thenReturn(financialTransactionList);

        List<FinancialTransaction> foundFinancialTransactionList = financialTransactionService
                .getFinancialTransactions(accountId);

        Assertions.assertNotNull(foundFinancialTransactionList);
        Assertions.assertEquals(2, foundFinancialTransactionList.size());
        verifyFinancialTransaction(financialTransactionList.getFirst(), 1L, (short) 2025, (byte) 9, (byte) 25, -2200L, "Game 1", "Bank A");
        verifyFinancialTransaction(financialTransactionList.getLast(), 2L, (short) 2025, (byte) 10, (byte) 5, -525L, "Game 2", "Bank A");
        verify(financialTransactionRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactionsForYear() {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction1);
        financialTransactionList.add(financialTransaction2);
        when(financialTransactionRepository.findByAccountIdAndYearValue(accountId, (short) 2025))
                .thenReturn(financialTransactionList);

        List<FinancialTransaction> foundFinancialTransactionList = financialTransactionService
                .getFinancialTransactionsForYear(accountId, (short) 2025);

        Assertions.assertNotNull(foundFinancialTransactionList);
        Assertions.assertEquals(2, foundFinancialTransactionList.size());
        verifyFinancialTransaction(financialTransactionList.getFirst(), 1L, (short) 2025, (byte) 9, (byte) 25, -2200L, "Game 1", "Bank A");
        verifyFinancialTransaction(financialTransactionList.getLast(), 2L, (short) 2025, (byte) 10, (byte) 5, -525L, "Game 2", "Bank A");
        verify(financialTransactionRepository, times(1))
                .findByAccountIdAndYearValue(accountId, (short) 2025);
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactionsForMonth() {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction2);
        when(financialTransactionRepository
                .findByAccountIdAndYearValueAndMonthValue(accountId, (short) 2025, (byte) 10))
                .thenReturn(financialTransactionList);

        List<FinancialTransaction> foundFinancialTransactionList = financialTransactionService
                .getFinancialTransactionsForMonth(accountId, (short) 2025, (byte) 10);

        Assertions.assertNotNull(foundFinancialTransactionList);
        Assertions.assertEquals(1, foundFinancialTransactionList.size());
        verifyFinancialTransaction(financialTransactionList.getFirst(), 2L, (short) 2025, (byte) 10, (byte) 5, -525L, "Game 2", "Bank A");
        verify(financialTransactionRepository, times(1))
                .findByAccountIdAndYearValueAndMonthValue(accountId, (short) 2025, (byte) 10);
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactionsForDay() {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        when(financialTransactionRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 10, (byte) 25))
                .thenReturn(financialTransactionList);

        List<FinancialTransaction> foundFinancialTransactionList = financialTransactionService
                .getFinancialTransactionsForDay(accountId, (short) 2025, (byte) 10, (byte) 25);

        Assertions.assertNotNull(foundFinancialTransactionList);
        Assertions.assertTrue(foundFinancialTransactionList.isEmpty());
        verify(financialTransactionRepository, times(1))
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 10, (byte) 25);
    }

    @Test
    public void FinancialTransactionService_UpdateFinancialTransaction() {
        Long financialTransactionId = 1L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest((short) 2027, (byte) 6, (byte) 18, -1000L, "Song");
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.of(financialTransaction1));
        when(financialTransactionRepository.save(financialTransaction1))
                .thenReturn(financialTransaction1);

        FinancialTransaction updatedFinancialTransaction = financialTransactionService
                .updateFinancialTransaction(accountId, financialTransactionId, financialTransactionRequest);

        Assertions.assertNotNull(updatedFinancialTransaction);
        verifyFinancialTransaction(updatedFinancialTransaction, financialTransactionId, (short) 2027, (byte) 6, (byte) 18, -1000L, "Song", "Bank A");
        verify(validatorService, times(1)).validate(financialTransactionRequest);
        verify(financialTransactionRepository, times(1)).findById(financialTransactionId);
        verify(financialTransactionRepository, times(1)).save(financialTransaction1);
    }

    @Test
    public void FinancialTransactionService_UpdateFinancialTransaction_NonexistentTransaction() {
        Long financialTransactionId = 10L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest((short) 2027, (byte) 6, (byte) 18, -1000L, "Song");
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> financialTransactionService.updateFinancialTransaction(accountId, financialTransactionId, financialTransactionRequest));
    }

    @Test
    public void FinancialTransactionService_DeleteFinancialTransaction() {
        Long financialTransactionId = 2L;
        when(financialTransactionRepository.findById(financialTransactionId)).thenReturn(Optional.of(financialTransaction1));
        doNothing().when(financialTransactionRepository).deleteById(financialTransactionId);

        financialTransactionService.deleteFinancialTransaction(accountId, financialTransactionId);

        verify(financialTransactionRepository, times(1)).deleteById(financialTransactionId);
    }

    @Test
    public void FinancialTransactionService_DeleteFinancialTransaction_NonexistentTransaction() {
        Long financialTransactionId = 8L;
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> financialTransactionService.deleteFinancialTransaction(accountId, financialTransactionId));
    }

    @Test
    public void FinancialTransactionService_DeleteFinancialTransactions() {
        doNothing().when(financialTransactionRepository).deleteByAccountId(accountId);

        financialTransactionService.deleteFinancialTransactions(accountId);

        verify(financialTransactionRepository, times(1)).deleteByAccountId(accountId);
    }
}
