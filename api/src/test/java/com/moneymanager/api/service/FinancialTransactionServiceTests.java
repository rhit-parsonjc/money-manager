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

    @InjectMocks
    private FinancialTransactionServiceImpl financialTransactionService;

    private Long accountId;
    private Account account;

    private FinancialTransaction financialTransaction1;
    private FinancialTransaction financialTransaction2;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234",
                roleSet, new HashSet<Account>());
        accountId = 1L;
        account = new TestAccount(accountId, "Bank A", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        financialTransaction1 = new TestFinancialTransaction(1L, account, (short) 2025, (byte) 9, (byte) 25, 2200L,
                "Game 1", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction1);
        financialTransaction2 = new TestFinancialTransaction(2L, account, (short) 2025, (byte) 10, (byte) 5, 525L,
                "Game 2", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction2);

        when(accountService.getAccountById(accountId)).thenReturn(account);
    }

    @Test
    public void FinancialTransactionService_CreateFinancialTransaction() {
        Long financialTransactionId = 3L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest(
                (short) 2025, (byte) 9, (byte) 15, 1350L, "Game 3");
        FinancialTransaction financialTransaction = new TestFinancialTransaction(financialTransactionId,
                account, (short) 2025, (byte) 9, (byte) 15, 1350L, "Game 3",
                new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        when(mapperService.mapFinancialTransactionRequestToTransaction(account, financialTransactionRequest))
                .thenReturn(financialTransaction);
        when(financialTransactionRepository.save(financialTransaction)).thenReturn(financialTransaction);

        FinancialTransaction createdFinancialTransaction = financialTransactionService
                .createFinancialTransaction(accountId, financialTransactionRequest);

        Assertions.assertNotNull(createdFinancialTransaction);
        Assertions.assertEquals((short) 2025, createdFinancialTransaction.getYearValue());
        Assertions.assertEquals((byte) 9, createdFinancialTransaction.getMonthValue());
        Assertions.assertEquals((byte) 15, createdFinancialTransaction.getDayValue());
        Assertions.assertEquals(1350L, createdFinancialTransaction.getAmount());
        Assertions.assertEquals("Game 3",createdFinancialTransaction.getName());
        Assertions.assertEquals(financialTransactionId, createdFinancialTransaction.getId());
    }

    @Test
    public void FinancialTransactionService_GetFinancialTransactionById() {
        Long financialTransactionId = 1L;
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.of(financialTransaction1));

        FinancialTransaction foundFinancialTransaction = financialTransactionService
                .getFinancialTransactionById(accountId, financialTransactionId);

        Assertions.assertNotNull(foundFinancialTransaction);
        Assertions.assertEquals("Game 1", foundFinancialTransaction.getName());
        Assertions.assertEquals(financialTransactionId, foundFinancialTransaction.getId());
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
        Assertions.assertEquals(1L, foundFinancialTransactionList.getFirst().getId());
        Assertions.assertEquals("Game 1", financialTransactionList.getFirst().getName());
        Assertions.assertEquals(2L, foundFinancialTransactionList.getLast().getId());
        Assertions.assertEquals("Game 2", financialTransactionList.getLast().getName());
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
        Assertions.assertEquals(1L, foundFinancialTransactionList.getFirst().getId());
        Assertions.assertEquals("Game 1", financialTransactionList.getFirst().getName());
        Assertions.assertEquals(2L, foundFinancialTransactionList.getLast().getId());
        Assertions.assertEquals("Game 2", financialTransactionList.getLast().getName());
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
        Assertions.assertEquals(2L, foundFinancialTransactionList.getLast().getId());
        Assertions.assertEquals("Game 2", financialTransactionList.getLast().getName());
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
    }

    @Test
    public void FinancialTransactionService_UpdateFinancialTransaction() {
        Long financialTransactionId = 1L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest((short) 2027, (byte) 6, (byte) 18, 1000L, "Song");
        when(financialTransactionRepository.findById(financialTransactionId))
                .thenReturn(Optional.of(financialTransaction1));
        when(financialTransactionRepository.save(financialTransaction1))
                .thenReturn(financialTransaction1);

        FinancialTransaction updatedFinancialTransaction = financialTransactionService
                .updateFinancialTransaction(accountId, financialTransactionId, financialTransactionRequest);

        Assertions.assertNotNull(updatedFinancialTransaction);
        Assertions.assertEquals((short) 2027, updatedFinancialTransaction.getYearValue());
        Assertions.assertEquals((byte) 6, updatedFinancialTransaction.getMonthValue());
        Assertions.assertEquals((byte) 18, updatedFinancialTransaction.getDayValue());
        Assertions.assertEquals(1000L, updatedFinancialTransaction.getAmount());
        Assertions.assertEquals("Song", updatedFinancialTransaction.getName());
        Assertions.assertEquals(financialTransactionId, updatedFinancialTransaction.getId());
    }

    @Test
    public void FinancialTransactionService_UpdateFinancialTransaction_NonexistentTransaction() {
        Long financialTransactionId = 10L;
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest((short) 2027, (byte) 6, (byte) 18, 1000L, "Song");
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
