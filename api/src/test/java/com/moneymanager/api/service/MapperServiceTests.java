package com.moneymanager.api.service;

import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.services.MapperService.MapperService;
import com.moneymanager.api.services.MapperService.MapperServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MapperServiceTests {
    private MapperService mapperService;

    private Role userRole;
    private UserEntity userEntity;
    private Account account1;
    private Account account2;
    private BankRecord bankRecord1;
    private BankRecord bankRecord2;
    private BankRecord bankRecord3;
    private FinancialTransaction financialTransaction1;
    private FinancialTransaction financialTransaction2;
    private FinancialTransaction financialTransaction3;
    private DateAmount dateAmount1;
    private DateAmount dateAmount2;
    private FileAttachment fileAttachment1;
    private FileAttachment fileAttachment2;

    private int compareBankRecordDtos(BankRecordDto dto1, BankRecordDto dto2) {
        return (int) (dto1.getId() - dto2.getId());
    }

    private int compareDateAmountDtos(DateAmountDto dto1, DateAmountDto dto2) {
        return (int) (dto1.getId() - dto2.getId());
    }

    private int compareFileAttachmentDtos(FileAttachmentDto dto1, FileAttachmentDto dto2) {
        return (int) (dto1.getId() - dto2.getId());
    }

    private int compareAccountDtos(AccountDto dto1, AccountDto dto2) {
        return (int) (dto1.getId() - dto2.getId());
    }

    @BeforeEach
    public void setup() {
        mapperService = new MapperServiceImpl();
        userRole = new TestRole(7L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        userEntity = new TestUserEntity(6L, "Spring", "password",
                roleSet, new HashSet<Account>());
        account1 = new TestAccount(3L, "Bank Epsilon", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(),
                new HashSet<DateAmount>());
        userEntity.getAccounts().add(account1);
        account2 = new TestAccount(5L, "Bank Theta", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(),
                new HashSet<DateAmount>());
        bankRecord1 = new TestBankRecord(15L, account1, (short) 2023, (byte) 11, (byte) 3,
                100000L, "Rent", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account1.getBankRecords().add(bankRecord1);
        bankRecord2 = new TestBankRecord(32L, account1, (short) 2025, (byte) 2, (byte) 23,
                30000L, "Laptop I", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account1.getBankRecords().add(bankRecord2);
        bankRecord3 = new TestBankRecord(33L, account1, (short) 2025, (byte) 2, (byte) 24,
                20000L, "Laptop II", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account1.getBankRecords().add(bankRecord3);
        financialTransaction1 = new TestFinancialTransaction(9L, account1, (short) 2025, (byte) 2, (byte) 22,
                50000L, "Laptop", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account1.getFinancialTransactions().add(financialTransaction1);
        financialTransaction2 = new TestFinancialTransaction(40L, account1, (short) 2023, (byte) 11, (byte) 1,
                100000L, "Rent", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account1.getFinancialTransactions().add(financialTransaction2);
        financialTransaction3 = new TestFinancialTransaction(67L, account1, (short) 2024, (byte) 7, (byte) 3,
                2000L, "Game", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        bankRecord1.getFinancialTransactions().add(financialTransaction2);
        bankRecord2.getFinancialTransactions().add(financialTransaction1);
        bankRecord3.getFinancialTransactions().add(financialTransaction1);
        financialTransaction1.getBankRecords().add(bankRecord2);
        financialTransaction1.getBankRecords().add(bankRecord3);
        financialTransaction2.getBankRecords().add(bankRecord1);
        dateAmount1 = new TestDateAmount(100L, account2, (short) 2026, (byte) 1, (byte) 1, 11222L);
        dateAmount2 = new TestDateAmount(200L, account2, (short) 2026, (byte) 3, (byte) 1, 15555L);
        byte[] contents = new byte[] {(byte) 89, (byte) 222, (byte) 123, (byte) 255, (byte) 88, (byte) 79, (byte) 222, (byte) 0, (byte) 128, (byte) 128};
        fileAttachment1 = new TestFileAttachment(88L, "receipt", "PNG", 123456789L, contents, financialTransaction1);
        financialTransaction1.getFileAttachments().add(fileAttachment1);
        fileAttachment2 = new TestFileAttachment(300L, "invoice", "PDF", 222444666L, new byte[50], financialTransaction1);
        financialTransaction1.getFileAttachments().add(fileAttachment2);
    }

    @Test
    public void MapperService_MapBankRecordToDto() {
        BankRecordDto bankRecordDto = mapperService.mapBankRecordToDto(bankRecord1);

        Assertions.assertEquals(15L, bankRecordDto.getId());
        Assertions.assertEquals((short) 2023, bankRecordDto.getYearValue());
        Assertions.assertEquals((byte) 11, bankRecordDto.getMonthValue());
        Assertions.assertEquals((byte) 3, bankRecordDto.getDayValue());
        Assertions.assertEquals(100000L, bankRecordDto.getAmount());
        Assertions.assertEquals("Rent", bankRecordDto.getName());
    }

    @Test
    public void MapperService_MapBankRecordToDetailsDto() {
        BankRecordDetailsDto bankRecordDetailsDto = mapperService.mapBankRecordToDetailsDto(bankRecord2);

        Assertions.assertEquals(32L, bankRecordDetailsDto.getId());
        Assertions.assertEquals((short) 2025, bankRecordDetailsDto.getYearValue());
        Assertions.assertEquals((byte) 2, bankRecordDetailsDto.getMonthValue());
        Assertions.assertEquals((byte) 23, bankRecordDetailsDto.getDayValue());
        Assertions.assertEquals(30000L, bankRecordDetailsDto.getAmount());
        Assertions.assertEquals("Laptop I", bankRecordDetailsDto.getName());

        List<FinancialTransactionDto> financialTransactionDtos = bankRecordDetailsDto.getFinancialTransactions();
        Assertions.assertNotNull(financialTransactionDtos);
        Assertions.assertEquals(1, financialTransactionDtos.size());

        FinancialTransactionDto financialTransactionDto = financialTransactionDtos.getFirst();
        Assertions.assertEquals(9L, financialTransactionDto.getId());
        Assertions.assertEquals((short) 2025, financialTransactionDto.getYearValue());
        Assertions.assertEquals((byte) 2, financialTransactionDto.getMonthValue());
        Assertions.assertEquals((byte) 22, financialTransactionDto.getDayValue());
        Assertions.assertEquals(50000L, financialTransactionDto.getAmount());
        Assertions.assertEquals("Laptop", financialTransactionDto.getName());
        Assertions.assertNotNull(bankRecordDetailsDto.getFileAttachments());
        Assertions.assertTrue(bankRecordDetailsDto.getFileAttachments().isEmpty());
    }

    @Test
    public void MapperService_MapBankRecordsToDtos() {
        List<BankRecord> bankRecords = new ArrayList<BankRecord>();
        bankRecords.add(bankRecord1);
        bankRecords.add(bankRecord2);
        bankRecords.add(bankRecord3);

        List<BankRecordDto> bankRecordDtos = mapperService.mapBankRecordsToDtos(bankRecords);

        Assertions.assertNotNull(bankRecordDtos);
        Assertions.assertEquals(3, bankRecordDtos.size());
        List<BankRecordDto> sortedBankRecordDtos = new ArrayList<BankRecordDto>(bankRecordDtos);
        sortedBankRecordDtos.sort(this::compareBankRecordDtos);

        BankRecordDto bankRecordDto1 = sortedBankRecordDtos.getFirst();
        Assertions.assertEquals(15L, bankRecordDto1.getId());
        Assertions.assertEquals((short) 2023, bankRecordDto1.getYearValue());
        Assertions.assertEquals((byte) 11, bankRecordDto1.getMonthValue());
        Assertions.assertEquals((byte) 3, bankRecordDto1.getDayValue());
        Assertions.assertEquals(100000L, bankRecordDto1.getAmount());
        Assertions.assertEquals("Rent", bankRecordDto1.getName());

        BankRecordDto bankRecordDto2 = sortedBankRecordDtos.get(1);
        Assertions.assertEquals(32L, bankRecordDto2.getId());
        Assertions.assertEquals((short) 2025, bankRecordDto2.getYearValue());
        Assertions.assertEquals((byte) 2, bankRecordDto2.getMonthValue());
        Assertions.assertEquals((byte) 23, bankRecordDto2.getDayValue());
        Assertions.assertEquals(30000L, bankRecordDto2.getAmount());
        Assertions.assertEquals("Laptop I", bankRecordDto2.getName());

        BankRecordDto bankRecordDto3 = sortedBankRecordDtos.getLast();
        Assertions.assertEquals(33L, bankRecordDto3.getId());
        Assertions.assertEquals((short) 2025, bankRecordDto3.getYearValue());
        Assertions.assertEquals((byte) 2, bankRecordDto3.getMonthValue());
        Assertions.assertEquals((byte) 24, bankRecordDto3.getDayValue());
        Assertions.assertEquals(20000L, bankRecordDto3.getAmount());
        Assertions.assertEquals("Laptop II", bankRecordDto3.getName());
    }

    @Test
    public void MapperService_MapBankRecordRequestToRecord() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest((short) 2024, (byte) 7, (byte) 3, 2000L, "Game");

        BankRecord bankRecord = mapperService.mapBankRecordRequestToRecord(account1, bankRecordRequest);

        Assertions.assertNotNull(bankRecord);
        Assertions.assertNull(bankRecord.getId());
        Assertions.assertEquals((short) 2024, bankRecord.getYearValue());
        Assertions.assertEquals((byte) 7, bankRecord.getMonthValue());
        Assertions.assertEquals((byte) 3, bankRecord.getDayValue());
        Assertions.assertEquals(2000L, bankRecord.getAmount());
        Assertions.assertEquals("Game", bankRecord.getName());
    }

    @Test
    public void MapperService_MapDateAmountToDto() {
        DateAmountDto dateAmountDto = mapperService.mapDateAmountToDto(dateAmount1);

        Assertions.assertEquals(100L, dateAmountDto.getId());
        Assertions.assertEquals((short) 2026, dateAmountDto.getYearValue());
        Assertions.assertEquals((byte) 1, dateAmountDto.getMonthValue());
        Assertions.assertEquals((byte) 1, dateAmountDto.getDayValue());
        Assertions.assertEquals(11222L, dateAmountDto.getAmount());
    }

    @Test
    public void MapperService_MapDateAmountsToDtos() {
        List<DateAmount> dateAmounts = new ArrayList<DateAmount>();
        dateAmounts.add(dateAmount1);
        dateAmounts.add(dateAmount2);

        List<DateAmountDto> dateAmountDtos = mapperService.mapDateAmountsToDtos(dateAmounts);

        Assertions.assertNotNull(dateAmountDtos);
        Assertions.assertEquals(2, dateAmountDtos.size());
        List<DateAmountDto> sortedDateAmountDtos = new ArrayList<DateAmountDto>(dateAmountDtos);
        sortedDateAmountDtos.sort(this::compareDateAmountDtos);

        DateAmountDto dateAmountDto1 = sortedDateAmountDtos.getFirst();
        Assertions.assertEquals(100L, dateAmountDto1.getId());
        Assertions.assertEquals((short) 2026, dateAmountDto1.getYearValue());
        Assertions.assertEquals((byte) 1, dateAmountDto1.getMonthValue());
        Assertions.assertEquals((byte) 1, dateAmountDto1.getDayValue());
        Assertions.assertEquals(11222L, dateAmountDto1.getAmount());

        DateAmountDto dateAmountDto2 = sortedDateAmountDtos.getLast();
        Assertions.assertEquals(200L, dateAmountDto2.getId());
        Assertions.assertEquals((short) 2026, dateAmountDto2.getYearValue());
        Assertions.assertEquals((byte) 3, dateAmountDto2.getMonthValue());
        Assertions.assertEquals((byte) 1, dateAmountDto2.getDayValue());
        Assertions.assertEquals(15555L, dateAmountDto2.getAmount());
    }

    @Test
    public void MapperService_MapDateAmountRequestToAmount() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest((short) 2025, (byte) 10, (byte) 25, 9191L);

        DateAmount dateAmount = mapperService.mapDateAmountRequestToAmount(account2, dateAmountCreateRequest);

        Assertions.assertNotNull(dateAmount);
        Assertions.assertNull(dateAmount.getId());
        Assertions.assertEquals((short) 2025, dateAmount.getYearValue());
        Assertions.assertEquals((byte) 10, dateAmount.getMonthValue());
        Assertions.assertEquals((byte) 25, dateAmount.getDayValue());
        Assertions.assertEquals(9191L, dateAmount.getAmount());
    }

    @Test
    public void MapperService_MapFinancialTransactionToDto() {
        FinancialTransactionDto financialTransactionDto = mapperService.mapFinancialTransactionToDto(financialTransaction3);

        Assertions.assertEquals(67L, financialTransactionDto.getId());
        Assertions.assertEquals((short) 2024, financialTransactionDto.getYearValue());
        Assertions.assertEquals((byte) 7, financialTransactionDto.getMonthValue());
        Assertions.assertEquals((byte) 3, financialTransactionDto.getDayValue());
        Assertions.assertEquals(2000L, financialTransactionDto.getAmount());
        Assertions.assertEquals("Game", financialTransactionDto.getName());
    }

    @Test
    public void MapperService_MapFinancialTransactionToDetailsDto() {
        FinancialTransactionDetailsDto financialTransactionDetailsDto = mapperService.mapFinancialTransactionToDetailsDto(financialTransaction1);

        Assertions.assertEquals(9L, financialTransactionDetailsDto.getId());
        Assertions.assertEquals((short) 2025, financialTransactionDetailsDto.getYearValue());
        Assertions.assertEquals((byte) 2, financialTransactionDetailsDto.getMonthValue());
        Assertions.assertEquals((byte) 22, financialTransactionDetailsDto.getDayValue());
        Assertions.assertEquals(50000L, financialTransactionDetailsDto.getAmount());
        Assertions.assertEquals("Laptop", financialTransactionDetailsDto.getName());

        List<BankRecordDto> bankRecordDtos = financialTransactionDetailsDto.getBankRecords();
        Assertions.assertNotNull(bankRecordDtos);
        Assertions.assertEquals(2, bankRecordDtos.size());
        List<BankRecordDto> sortedBankRecordDtos = new ArrayList<BankRecordDto>(bankRecordDtos);
        sortedBankRecordDtos.sort(this::compareBankRecordDtos);

        BankRecordDto bankRecordDto1 = sortedBankRecordDtos.getFirst();
        Assertions.assertEquals(32L, bankRecordDto1.getId());
        Assertions.assertEquals((short) 2025, bankRecordDto1.getYearValue());
        Assertions.assertEquals((byte) 2, bankRecordDto1.getMonthValue());
        Assertions.assertEquals((byte) 23, bankRecordDto1.getDayValue());
        Assertions.assertEquals(30000L, bankRecordDto1.getAmount());
        Assertions.assertEquals("Laptop I", bankRecordDto1.getName());

        BankRecordDto bankRecordDto2 = sortedBankRecordDtos.getLast();
        Assertions.assertEquals(33L, bankRecordDto2.getId());
        Assertions.assertEquals((short) 2025, bankRecordDto2.getYearValue());
        Assertions.assertEquals((byte) 2, bankRecordDto2.getMonthValue());
        Assertions.assertEquals((byte) 24, bankRecordDto2.getDayValue());
        Assertions.assertEquals(20000L, bankRecordDto2.getAmount());
        Assertions.assertEquals("Laptop II", bankRecordDto2.getName());

        List<FileAttachmentDto> fileAttachmentDtos = financialTransactionDetailsDto.getFileAttachments();
        Assertions.assertNotNull(fileAttachmentDtos);
        Assertions.assertEquals(2, fileAttachmentDtos.size());
        List<FileAttachmentDto> sortedFileAttachmentDtos = new ArrayList<FileAttachmentDto>(fileAttachmentDtos);
        sortedFileAttachmentDtos.sort(this::compareFileAttachmentDtos);

        FileAttachmentDto fileAttachmentDto1 = sortedFileAttachmentDtos.getFirst();
        Assertions.assertEquals(88L, fileAttachmentDto1.getId());
        Assertions.assertEquals("receipt", fileAttachmentDto1.getName());
        Assertions.assertEquals("PNG", fileAttachmentDto1.getType());
        Assertions.assertEquals(123456789L, fileAttachmentDto1.getSize());

        FileAttachmentDto fileAttachmentDto2 = sortedFileAttachmentDtos.getLast();
        Assertions.assertEquals(300L, fileAttachmentDto2.getId());
        Assertions.assertEquals("invoice", fileAttachmentDto2.getName());
        Assertions.assertEquals("PDF", fileAttachmentDto2.getType());
        Assertions.assertEquals(222444666L, fileAttachmentDto2.getSize());
    }

    @Test
    public void MapperService_MapFileAttachmentToDto() {
        FileAttachmentDto fileAttachmentDto = mapperService.mapFileAttachmentToDto(fileAttachment2);

        Assertions.assertEquals(300L, fileAttachmentDto.getId());
        Assertions.assertEquals("invoice", fileAttachmentDto.getName());
        Assertions.assertEquals("PDF", fileAttachmentDto.getType());
        Assertions.assertEquals(222444666L, fileAttachmentDto.getSize());
    }

    @Test
    public void MapperService_MapFileAttachmentToDetailsDto() {
        FileAttachmentDetailsDto fileAttachmentDetailsDto = mapperService.mapFileAttachmentToDetailsDto(fileAttachment1);

        Assertions.assertEquals(88L, fileAttachmentDetailsDto.getId());
        Assertions.assertEquals("receipt", fileAttachmentDetailsDto.getName());
        Assertions.assertEquals("PNG", fileAttachmentDetailsDto.getType());
        Assertions.assertEquals(123456789L, fileAttachmentDetailsDto.getSize());
        byte[] contents = new byte[] {
                (byte) 89, (byte) 222, (byte) 123, (byte) 255, (byte) 88,
                (byte) 79, (byte) 222, (byte) 0, (byte) 128, (byte) 128
        };
        Assertions.assertArrayEquals(contents, fileAttachmentDetailsDto.getContents());
    }

    @Test
    public void MapperService_MapFileAttachmentsToDtos() {
        List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
        fileAttachments.add(fileAttachment1);

        List<FileAttachmentDto> fileAttachmentDtos = mapperService.mapFileAttachmentsToDtos(fileAttachments);

        Assertions.assertNotNull(fileAttachmentDtos);
        Assertions.assertEquals(1, fileAttachmentDtos.size());

        FileAttachmentDto fileAttachmentDto = fileAttachmentDtos.getFirst();
        Assertions.assertEquals(88L, fileAttachmentDto.getId());
        Assertions.assertEquals("receipt", fileAttachmentDto.getName());
        Assertions.assertEquals("PNG", fileAttachmentDto.getType());
        Assertions.assertEquals(123456789L, fileAttachmentDto.getSize());
    }

    @Test
    public void MapperService_MapAccountToDto() {
        AccountDto accountDto = mapperService.mapAccountToDto(account1);

        Assertions.assertEquals(3L, accountDto.getId());
        Assertions.assertEquals("Bank Epsilon", accountDto.getName());
    }

    @Test
    public void MapperService_MapAccountsToDtos() {
        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account1);
        accounts.add(account2);

        List<AccountDto> accountDtos = mapperService.mapAccountsToDtos(accounts);

        Assertions.assertNotNull(accountDtos);
        Assertions.assertEquals(2, accountDtos.size());
        List<AccountDto> sortedAccountDtos = new ArrayList<AccountDto>(accountDtos);
        sortedAccountDtos.sort(this::compareAccountDtos);

        AccountDto accountDto1 = sortedAccountDtos.getFirst();
        Assertions.assertEquals(3L, accountDto1.getId());
        Assertions.assertEquals("Bank Epsilon", accountDto1.getName());

        AccountDto accountDto2 = sortedAccountDtos.getLast();
        Assertions.assertEquals(5L, accountDto2.getId());
        Assertions.assertEquals("Bank Theta", accountDto2.getName());
    }

    @Test
    public void MapperService_MapAccountRequestToAccount() {
        AccountRequest accountRequest = new AccountRequest("Bank Omega");

        Account account = mapperService.mapAccountRequestToAccount(userEntity, accountRequest);

        Assertions.assertNotNull(account);
        Assertions.assertNull(account.getId());
        Assertions.assertEquals("Bank Omega", account.getName());
    }
}
