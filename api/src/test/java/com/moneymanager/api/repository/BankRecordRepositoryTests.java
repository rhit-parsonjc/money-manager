package com.moneymanager.api.repository;

import com.moneymanager.api.models.*;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.BankRecordRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BankRecordRepositoryTests {
    @Autowired
    private BankRecordRepository bankRecordRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Account savedAccount;

    private BankRecord bankRecord1;
    private BankRecord bankRecord2;
    private BankRecord bankRecord3;
    private BankRecord bankRecord4;
    private BankRecord bankRecord5;

    private int compareRecords(BankRecord record1, BankRecord record2) {
        if (record1.getYearValue().shortValue() != record2.getYearValue().shortValue())
            return record1.getYearValue() - record2.getYearValue();
        if (record1.getMonthValue().byteValue() != record2.getMonthValue().byteValue())
            return record1.getMonthValue() - record2.getMonthValue();
        if (record1.getDayValue().byteValue() != record2.getDayValue().byteValue())
            return record1.getDayValue() - record2.getDayValue();
        return record1.getName().compareTo(record2.getName());
    }

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(savedUserRole);
        UserEntity userEntity = new UserEntity("Spring", "pass1234", roleSet);
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);
        Account account = new Account(savedUserEntity, "Bank A");
        savedAccount = accountRepository.save(account);
        bankRecord1 = new BankRecord(savedAccount, (short) 2025, (byte) 7, (byte) 4, -1599L, "Movie Tickets");
        bankRecord2 = new BankRecord(savedAccount, (short) 2025, (byte) 7, (byte) 4, -3500L, "Dinner");
        bankRecord3 = new BankRecord(savedAccount, (short) 2025, (byte) 7, (byte) 25, -750L, "Streaming Service");
        bankRecord4 = new BankRecord(savedAccount, (short) 2025, (byte) 12, (byte) 27, 2500L, "Holiday Gift");
        bankRecord5 = new BankRecord(savedAccount, (short) 2024, (byte) 4, (byte) 15, -1250L, "Lunch");
    }

    @Test
    public void BankRecordRepository_Save() {
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord1);

        Assertions.assertEquals((short) 2025, savedBankRecord.getYearValue());
        Assertions.assertEquals((byte) 7, savedBankRecord.getMonthValue());
        Assertions.assertEquals((byte) 4, savedBankRecord.getDayValue());
        Assertions.assertEquals(-1599L, savedBankRecord.getAmount());
        Assertions.assertEquals("Movie Tickets", savedBankRecord.getName());
        Assertions.assertEquals("Bank A", savedBankRecord.getAccount().getName());
        Assertions.assertTrue(savedBankRecord.getId() > 0);
    }

    @Test
    public void BankRecordRepository_FindById() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long savedBankRecordId = savedBankRecord.getId();

        Optional<BankRecord> foundBankRecordOptional = bankRecordRepository.findById(savedBankRecordId);

        Assertions.assertTrue(foundBankRecordOptional.isPresent());
        BankRecord foundBankRecord = foundBankRecordOptional.get();
        Assertions.assertEquals("Holiday Gift", foundBankRecord.getName());
    }

    @Test
    public void BankRecordRepository_FindByAccountId() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long savedAccountId = savedAccount.getId();

        List<BankRecord> foundBankRecords = bankRecordRepository.findByAccountId(savedAccountId);

        Assertions.assertNotNull(foundBankRecords);
        Assertions.assertEquals(5, foundBankRecords.size());
        foundBankRecords.sort(this::compareRecords);
        Assertions.assertEquals("Lunch", foundBankRecords.get(0).getName());
        Assertions.assertEquals("Dinner", foundBankRecords.get(1).getName());
        Assertions.assertEquals("Movie Tickets", foundBankRecords.get(2).getName());
        Assertions.assertEquals("Streaming Service", foundBankRecords.get(3).getName());
        Assertions.assertEquals("Holiday Gift", foundBankRecords.get(4).getName());
    }

    @Test
    public void BankRecordRepository_FindByYear() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long savedAccountId = savedAccount.getId();

        List<BankRecord> foundBankRecords = bankRecordRepository.findByAccountIdAndYearValue(savedAccountId, (short) 2025);

        Assertions.assertNotNull(foundBankRecords);
        Assertions.assertEquals(4, foundBankRecords.size());
        foundBankRecords.sort(this::compareRecords);
        Assertions.assertEquals("Dinner", foundBankRecords.get(0).getName());
        Assertions.assertEquals("Movie Tickets", foundBankRecords.get(1).getName());
        Assertions.assertEquals("Streaming Service", foundBankRecords.get(2).getName());
        Assertions.assertEquals("Holiday Gift", foundBankRecords.get(3).getName());
    }

    @Test
    public void BankRecordRepository_FindByMonth() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long savedAccountId = savedAccount.getId();

        List<BankRecord> foundBankRecords = bankRecordRepository
                .findByAccountIdAndYearValueAndMonthValue(savedAccountId, (short) 2025, (byte) 7);

        Assertions.assertNotNull(foundBankRecords);
        Assertions.assertEquals(3, foundBankRecords.size());
        foundBankRecords.sort(this::compareRecords);
        Assertions.assertEquals("Dinner", foundBankRecords.get(0).getName());
        Assertions.assertEquals("Movie Tickets", foundBankRecords.get(1).getName());
        Assertions.assertEquals("Streaming Service", foundBankRecords.get(2).getName());
    }

    @Test
    public void BankRecordRepository_FindByDay() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long savedAccountId = savedAccount.getId();

        List<BankRecord> foundBankRecords = bankRecordRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(
                savedAccountId, (short) 2025, (byte) 7, (byte) 4);

        Assertions.assertNotNull(foundBankRecords);
        Assertions.assertEquals(2, foundBankRecords.size());
        foundBankRecords.sort(this::compareRecords);
        Assertions.assertEquals("Dinner", foundBankRecords.get(0).getName());
        Assertions.assertEquals("Movie Tickets", foundBankRecords.get(1).getName());
    }

    @Test
    public void BankRecordRepository_Update() {
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord1);
        Long bankRecordId = savedBankRecord.getId();
        Optional<BankRecord> foundBankRecordOptional = bankRecordRepository.findById(bankRecordId);
        Assertions.assertTrue(foundBankRecordOptional.isPresent());
        BankRecord foundBankRecord = foundBankRecordOptional.get();
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2026);
        bankRecordRequest.setMonthValue((byte) 12);
        bankRecordRequest.setDayValue((byte) 27);
        bankRecordRequest.setAmount(9999L);
        bankRecordRequest.setName("Fireworks");

        foundBankRecord.update(bankRecordRequest);
        bankRecordRepository.save(foundBankRecord);

        Optional<BankRecord> updatedFoundBankRecordOptional = bankRecordRepository.findById(bankRecordId);
        Assertions.assertTrue(updatedFoundBankRecordOptional.isPresent());
        BankRecord updatedFoundBankRecord = updatedFoundBankRecordOptional.get();
        Assertions.assertEquals((short) 2026, updatedFoundBankRecord.getYearValue());
        Assertions.assertEquals((byte) 12, updatedFoundBankRecord.getMonthValue());
        Assertions.assertEquals((byte) 27, updatedFoundBankRecord.getDayValue());
        Assertions.assertEquals(9999L, updatedFoundBankRecord.getAmount());
        Assertions.assertEquals("Fireworks", updatedFoundBankRecord.getName());
    }

    @Test
    public void BankRecordRepository_Delete() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        BankRecord savedBankRecord = bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long bankRecordId = savedBankRecord.getId();

        bankRecordRepository.deleteById(bankRecordId);

        Optional<BankRecord> foundBankRecordOptional = bankRecordRepository.findById(bankRecordId);
        Assertions.assertTrue(foundBankRecordOptional.isEmpty());
    }

    @Test
    public void BankRecordRepository_DeleteByAccountId() {
        bankRecordRepository.save(bankRecord1);
        bankRecordRepository.save(bankRecord2);
        bankRecordRepository.save(bankRecord3);
        bankRecordRepository.save(bankRecord4);
        bankRecordRepository.save(bankRecord5);
        Long accountId = savedAccount.getId();

        bankRecordRepository.deleteByAccountId(accountId);

        List<BankRecord> foundBankRecordList = bankRecordRepository.findByAccountId(accountId);
        Assertions.assertNotNull(foundBankRecordList);
        Assertions.assertTrue(foundBankRecordList.isEmpty());
    }
}
