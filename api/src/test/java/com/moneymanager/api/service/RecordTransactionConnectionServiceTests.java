package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.services.RecordTransactionConnectionService.RecordTransactionConnectionServiceImpl;
import com.moneymanager.api.services.UserEntityService.UserEntityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordTransactionConnectionServiceTests {
    @Mock
    private BankRecordRepository bankRecordRepository;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private RecordTransactionConnectionServiceImpl recordTransactionConnectionService;

    private Long bankRecordId;
    private BankRecord bankRecord;
    private Long financialTransactionId1;
    private FinancialTransaction financialTransaction1;
    private Long financialTransactionId2;
    private FinancialTransaction financialTransaction2;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234",
                roleSet, new HashSet<Account>());
        Account account = new TestAccount(1L, "Bank A", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        bankRecordId = 1L;
        bankRecord = new TestBankRecord(bankRecordId, account, (short) 2026, (byte) 3, (byte) 27, 2000L,
                "Lunch", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord);
        financialTransactionId1 = 2L;
        financialTransaction1 = new TestFinancialTransaction(financialTransactionId1, account, (short) 2026, (byte) 3, (byte) 26, 1500L,
                "Lunch I", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction1);
        financialTransactionId2 = 3L;
        financialTransaction2 = new TestFinancialTransaction(financialTransactionId2, account, (short) 2026, (byte) 3, (byte) 25, 500L,
                "Lunch II", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction2);
        bankRecord.getFinancialTransactions().add(financialTransaction1);
        financialTransaction1.getBankRecords().add(bankRecord);
        when(userEntityService.getAuthenticatedUserOrThrow()).thenReturn(userEntity);
    }

    @Test
    public void RecordTransactionConnectionService_CreateConnection() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(financialTransactionId2)).thenReturn(Optional.of(financialTransaction2));
        when(bankRecordRepository.save(bankRecord)).thenReturn(bankRecord);

        recordTransactionConnectionService.createConnection(bankRecordId, financialTransactionId2);

        Assertions.assertTrue(bankRecord.getFinancialTransactions().contains(financialTransaction2));
    }

    @Test
    public void RecordTransactionConnectionService_CreateConnection_AlreadyExists() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(financialTransactionId1)).thenReturn(Optional.of(financialTransaction1));

        Assertions.assertThrows(AlreadyExistsException.class,
                () -> recordTransactionConnectionService.createConnection(bankRecordId, financialTransactionId1));
    }

    @Test
    public void RecordTransactionConnectionService_CreateConnection_NonexistentRecord() {
        when(bankRecordRepository.findById(4L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> recordTransactionConnectionService.createConnection(4L, financialTransactionId2));
    }

    @Test
    public void RecordTransactionConnectionService_CreateConnection_NonexistentTransaction() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> recordTransactionConnectionService.createConnection(bankRecordId, 5L));
    }

    @Test
    public void RecordTransactionConnectionService_DeleteConnection() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(financialTransactionId1)).thenReturn(Optional.of(financialTransaction1));
        when(bankRecordRepository.save(bankRecord)).thenReturn(bankRecord);

        recordTransactionConnectionService.deleteConnection(bankRecordId, financialTransactionId1);

        Assertions.assertFalse(bankRecord.getFinancialTransactions().contains(financialTransaction1));
    }

    @Test
    public void RecordTransactionConnectionService_DeleteConnection_NonexistentConnection() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(financialTransactionId2)).thenReturn(Optional.of(financialTransaction2));

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> recordTransactionConnectionService.deleteConnection(bankRecordId, financialTransactionId2));
    }

    @Test
    public void RecordTransactionConnectionService_DeleteConnection_NonexistentRecord() {
        when(bankRecordRepository.findById(4L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> recordTransactionConnectionService.deleteConnection(4L, financialTransactionId2));
    }

    @Test
    public void RecordTransactionConnectionService_DeleteConnection_NonexistentTransaction() {
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(financialTransactionRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> recordTransactionConnectionService.deleteConnection(bankRecordId, 5L));
    }
}
