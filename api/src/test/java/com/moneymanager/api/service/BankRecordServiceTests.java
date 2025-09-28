package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestBankRecord;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.BankRecordService.BankRecordServiceImpl;
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
public class BankRecordServiceTests {
    @Mock
    private BankRecordRepository bankRecordRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private BankRecordServiceImpl bankRecordService;

    private Long accountId;
    private Account account;

    private BankRecord bankRecord1;
    private BankRecord bankRecord2;
    private BankRecord bankRecord3;

    private void verifyBankRecord(BankRecord bankRecord, long id, short yearValue, byte monthValue, byte dayValue, long amount, String name, String accountName) {
        Assertions.assertEquals(id, bankRecord.getId());
        Assertions.assertEquals(yearValue, bankRecord.getYearValue());
        Assertions.assertEquals(monthValue, bankRecord.getMonthValue());
        Assertions.assertEquals(dayValue, bankRecord.getDayValue());
        Assertions.assertEquals(amount, bankRecord.getAmount());
        Assertions.assertEquals(name, bankRecord.getName());
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
        bankRecord1 = new TestBankRecord(1L, account, (short) 2025, (byte) 7, (byte) 25, -1500L,
                "Breakfast", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecord2 = new TestBankRecord(2L, account, (short) 2025, (byte) 7, (byte) 30, -2200L,
                "Lunch", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecord3 = new TestBankRecord(3L, account, (short) 2025, (byte) 8, (byte) 8, -3850L,
                "Dinner", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord1);
        account.getBankRecords().add(bankRecord2);
        account.getBankRecords().add(bankRecord3);

        when(accountService.getAccountById(1L)).thenReturn(account);
    }

    @Test
    public void BankRecordService_CreateBankRecord() {
        Long bankRecordId = 4L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2025, (byte) 8, (byte) 27, -733L, "Snack");
        BankRecord bankRecord = new TestBankRecord(bankRecordId, account, (short) 2025, (byte) 8,
                (byte) 27, -733L, "Snack", new HashSet<FileAttachment>(),
                new HashSet<FinancialTransaction>());
        when(mapperService.mapBankRecordRequestToRecord(account, bankRecordRequest)).thenReturn(bankRecord);
        when(bankRecordRepository.save(bankRecord)).thenReturn(bankRecord);

        BankRecord createdBankRecord = bankRecordService.createBankRecord(accountId, bankRecordRequest);

        Assertions.assertNotNull(createdBankRecord);
        verifyBankRecord(createdBankRecord, bankRecordId, (short) 2025, (byte) 8, (byte) 27, -733L, "Snack", "Bank A");
        verify(mapperService, times(1)).mapBankRecordRequestToRecord(account, bankRecordRequest);
        verify(bankRecordRepository, times(1)).save(bankRecord);
    }

    @Test
    public void BankRecordService_GetBankRecordById() {
        Long bankRecordId = 3L;
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord3));

        BankRecord foundBankRecord = bankRecordService.getBankRecordById(accountId, bankRecordId);

        Assertions.assertNotNull(foundBankRecord);
        verifyBankRecord(foundBankRecord, bankRecordId, (short) 2025, (byte) 8, (byte) 8, -3850L, "Dinner", "Bank A");
        verify(bankRecordRepository, times(1)).findById(bankRecordId);
    }

    @Test
    public void BankRecordService_GetBankRecordById_NonexistentRecord() {
        Long bankRecordId = 11L;
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> bankRecordService.getBankRecordById(accountId, bankRecordId));
    }

    @Test
    public void BankRecordService_GetBankRecords() {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        bankRecordList.add(bankRecord2);
        bankRecordList.add(bankRecord3);
        when(bankRecordRepository.findByAccountId(accountId)).thenReturn(bankRecordList);

        List<BankRecord> foundBankRecordList = bankRecordService.getBankRecords(accountId);

        Assertions.assertNotNull(foundBankRecordList);
        Assertions.assertEquals(3, foundBankRecordList.size());
        verifyBankRecord(foundBankRecordList.get(0), 1L, (short) 2025, (byte) 7, (byte) 25, -1500L, "Breakfast", "Bank A");
        verifyBankRecord(foundBankRecordList.get(1), 2L, (short) 2025, (byte) 7, (byte) 30, -2200L, "Lunch", "Bank A");
        verifyBankRecord(foundBankRecordList.get(2), 3L, (short) 2025, (byte) 8, (byte) 8, -3850L, "Dinner", "Bank A");
        verify(bankRecordRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void BankRecordService_GetBankRecordsForYear() {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        bankRecordList.add(bankRecord2);
        bankRecordList.add(bankRecord3);
        when(bankRecordRepository.findByAccountIdAndYearValue(accountId, (short) 2025)).thenReturn(bankRecordList);

        List<BankRecord> foundBankRecordList = bankRecordService.getBankRecordsForYear(
                accountId, (short) 2025);

        Assertions.assertNotNull(foundBankRecordList);
        Assertions.assertEquals(3, foundBankRecordList.size());
        verifyBankRecord(foundBankRecordList.get(0), 1L, (short) 2025, (byte) 7, (byte) 25, -1500L, "Breakfast", "Bank A");
        verifyBankRecord(foundBankRecordList.get(1), 2L, (short) 2025, (byte) 7, (byte) 30, -2200L, "Lunch", "Bank A");
        verifyBankRecord(foundBankRecordList.get(2), 3L, (short) 2025, (byte) 8, (byte) 8, -3850L, "Dinner", "Bank A");
        verify(bankRecordRepository, times(1)).findByAccountIdAndYearValue(accountId, (short) 2025);
    }

    @Test
    public void BankRecordService_GetBankRecordsForMonth() {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        bankRecordList.add(bankRecord2);
        when(bankRecordRepository.findByAccountIdAndYearValueAndMonthValue(
                accountId, (short) 2025, (byte) 7)).thenReturn(bankRecordList);

        List<BankRecord> foundBankRecordList = bankRecordService.getBankRecordsForMonth(
                accountId, (short) 2025, (byte) 7);

        Assertions.assertNotNull(foundBankRecordList);
        Assertions.assertEquals(2, foundBankRecordList.size());
        verifyBankRecord(foundBankRecordList.getFirst(), 1L, (short) 2025, (byte) 7, (byte) 25, -1500L, "Breakfast", "Bank A");
        verifyBankRecord(foundBankRecordList.getLast(), 2L, (short) 2025, (byte) 7, (byte) 30, -2200L, "Lunch", "Bank A");
        verify(bankRecordRepository, times(1)).findByAccountIdAndYearValueAndMonthValue(accountId, (short) 2025, (byte) 7);
    }

    @Test
    public void BankRecordService_GetBankRecordsForDay() {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord2);
        when(bankRecordRepository.findByAccountIdAndYearValueAndMonthValueAndDayValue(
                accountId, (short) 2025, (byte) 7, (byte) 30)).thenReturn(bankRecordList);

        List<BankRecord> foundBankRecordList = bankRecordService.getBankRecordsForDay(
                accountId, (short) 2025, (byte) 7, (byte) 30);

        Assertions.assertNotNull(foundBankRecordList);
        Assertions.assertEquals(1, foundBankRecordList.size());
        verifyBankRecord(foundBankRecordList.getFirst(), 2L, (short) 2025, (byte) 7, (byte) 30, -2200L, "Lunch", "Bank A");
        verify(bankRecordRepository, times(1)).findByAccountIdAndYearValueAndMonthValueAndDayValue(accountId, (short) 2025, (byte) 7, (byte) 30);
    }

    @Test
    public void BankRecordService_UpdateBankRecord() {
        Long bankRecordId = 2L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2024, (byte) 10, (byte) 22, -2400L, "Brunch");
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord2));
        when(bankRecordRepository.save(bankRecord2)).thenReturn(bankRecord2);

        BankRecord updatedBankRecord = bankRecordService.updateBankRecord(accountId, bankRecordId, bankRecordRequest);

        Assertions.assertNotNull(updatedBankRecord);
        verifyBankRecord(updatedBankRecord, bankRecordId, (short) 2024, (byte) 10, (byte) 22, -2400L, "Brunch", "Bank A");
        verify(bankRecordRepository, times(1)).findById(bankRecordId);
        verify(bankRecordRepository, times(1)).save(bankRecord2);
    }

    @Test
    public void BankRecordService_UpdateBankRecord_NonexistentRecord() {
        Long bankRecordId = 7L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2026, (byte) 2, (byte) 28, -1024L, "Parking");
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> bankRecordService.updateBankRecord(accountId, bankRecordId, bankRecordRequest));
    }

    @Test
    public void BankRecordService_DeleteBankRecord() {
        Long bankRecordId = 3L;
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord3));
        doNothing().when(bankRecordRepository).deleteById(bankRecordId);

        bankRecordService.deleteBankRecord(accountId, bankRecordId);

        verify(bankRecordRepository, times(1)).deleteById(bankRecordId);
    }

    @Test
    public void BankRecordService_DeleteBankRecord_NonexistentRecord() {
        Long bankRecordId = 5L;
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> bankRecordService.deleteBankRecord(accountId, bankRecordId));
    }

    @Test
    public void BankRecordService_DeleteBankRecords() {
        doNothing().when(bankRecordRepository).deleteByAccountId(accountId);

        bankRecordService.deleteBankRecords(accountId);

        verify(bankRecordRepository, times(1)).deleteByAccountId(accountId);
    }
}
