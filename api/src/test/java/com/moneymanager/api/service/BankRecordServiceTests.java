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
        bankRecord1 = new TestBankRecord(1L, account, (short) 2025, (byte) 7, (byte) 25, 1500L,
                "Breakfast", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord1);
        bankRecord2 = new TestBankRecord(2L, account, (short) 2025, (byte) 7, (byte) 30, 2200L,
                "Lunch", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord2);
        bankRecord3 = new TestBankRecord(3L, account, (short) 2025, (byte) 8, (byte) 8, 3850L,
                "Dinner", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord3);

        when(accountService.getAccountById(1L)).thenReturn(account);
    }

    @Test
    public void BankRecordService_CreateBankRecord() {
        Long bankRecordId = 4L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2025, (byte) 8, (byte) 27, 733L, "Snack");
        BankRecord bankRecord = new TestBankRecord(bankRecordId, account, (short) 2025, (byte) 8,
                (byte) 27, 733L, "Snack", new HashSet<FileAttachment>(),
                new HashSet<FinancialTransaction>());
        when(mapperService.mapBankRecordRequestToRecord(account, bankRecordRequest)).thenReturn(bankRecord);
        when(bankRecordRepository.save(bankRecord)).thenReturn(bankRecord);

        BankRecord createdBankRecord = bankRecordService.createBankRecord(accountId, bankRecordRequest);

        Assertions.assertNotNull(createdBankRecord);
        Assertions.assertEquals((short) 2025, createdBankRecord.getYearValue());
        Assertions.assertEquals((byte) 8, createdBankRecord.getMonthValue());
        Assertions.assertEquals((byte) 27, createdBankRecord.getDayValue());
        Assertions.assertEquals(733L, createdBankRecord.getAmount());
        Assertions.assertEquals("Snack", createdBankRecord.getName());
        Assertions.assertEquals(bankRecordId, createdBankRecord.getId());
    }

    @Test
    public void BankRecordService_GetBankRecordById() {
        Long bankRecordId = 3L;
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord3));

        BankRecord foundBankRecord = bankRecordService.getBankRecordById(accountId, bankRecordId);

        Assertions.assertNotNull(foundBankRecord);
        Assertions.assertEquals("Dinner", foundBankRecord.getName());
        Assertions.assertEquals(bankRecordId, foundBankRecord.getId());
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
        Assertions.assertEquals(1L, foundBankRecordList.get(0).getId());
        Assertions.assertEquals("Breakfast", foundBankRecordList.get(0).getName());
        Assertions.assertEquals(2L, foundBankRecordList.get(1).getId());
        Assertions.assertEquals("Lunch", foundBankRecordList.get(1).getName());
        Assertions.assertEquals(3L, foundBankRecordList.get(2).getId());
        Assertions.assertEquals("Dinner", foundBankRecordList.get(2).getName());
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
        Assertions.assertEquals(1L, foundBankRecordList.get(0).getId());
        Assertions.assertEquals("Breakfast", foundBankRecordList.get(0).getName());
        Assertions.assertEquals(2L, foundBankRecordList.get(1).getId());
        Assertions.assertEquals("Lunch", foundBankRecordList.get(1).getName());
        Assertions.assertEquals(3L, foundBankRecordList.get(2).getId());
        Assertions.assertEquals("Dinner", foundBankRecordList.get(2).getName());
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
        Assertions.assertEquals(1L, foundBankRecordList.get(0).getId());
        Assertions.assertEquals("Breakfast", foundBankRecordList.get(0).getName());
        Assertions.assertEquals(2L, foundBankRecordList.get(1).getId());
        Assertions.assertEquals("Lunch", foundBankRecordList.get(1).getName());
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
        Assertions.assertEquals(2L, foundBankRecordList.get(0).getId());
        Assertions.assertEquals("Lunch", foundBankRecordList.get(0).getName());
    }

    @Test
    public void BankRecordService_UpdateBankRecord() {
        Long bankRecordId = 2L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2024, (byte) 10, (byte) 22, 2400L, "Brunch");
        when(bankRecordRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord2));
        when(bankRecordRepository.save(bankRecord2)).thenReturn(bankRecord2);

        BankRecord updatedBankRecord = bankRecordService.updateBankRecord(accountId, bankRecordId, bankRecordRequest);

        Assertions.assertNotNull(updatedBankRecord);
        Assertions.assertEquals((short) 2024, updatedBankRecord.getYearValue());
        Assertions.assertEquals((byte) 10, updatedBankRecord.getMonthValue());
        Assertions.assertEquals((byte) 22, updatedBankRecord.getDayValue());
        Assertions.assertEquals(2400L, updatedBankRecord.getAmount());
        Assertions.assertEquals("Brunch", updatedBankRecord.getName());
        Assertions.assertEquals(bankRecordId, updatedBankRecord.getId());
    }

    @Test
    public void BankRecordService_UpdateBankRecord_NonexistentRecord() {
        Long bankRecordId = 7L;
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2026, (byte) 2, (byte) 28, 1024L, "Parking");
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
