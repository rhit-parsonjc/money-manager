package com.moneymanager.api.repository;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.DateAmount;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.DateAmountRepository;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.DateAmountUpdateRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DateAmountRepositoryTests {
    @Autowired
    private DateAmountRepository dateAmountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Account savedAccount1;
    private Account savedAccount2;

    private DateAmount dateAmount1;
    private DateAmount dateAmount2;
    private DateAmount dateAmount3;
    private DateAmount dateAmount4;
    private DateAmount dateAmount5;

    private int compareDateAmounts(DateAmount amount1, DateAmount amount2) {
        if (amount1.getYearValue().shortValue() != amount2.getYearValue().shortValue())
            return amount1.getYearValue() - amount2.getYearValue();
        if (amount1.getMonthValue().byteValue() != amount2.getMonthValue().byteValue())
            return amount1.getMonthValue() - amount2.getMonthValue();
        if (amount1.getDayValue().byteValue() != amount2.getDayValue().byteValue())
            return amount1.getDayValue() - amount2.getDayValue();
        return 0;
    }

    private void verifyDateAmount(DateAmount dateAmount, short yearValue, byte monthValue, byte dayValue, long amount, String accountName) {
        Assertions.assertEquals(yearValue, dateAmount.getYearValue());
        Assertions.assertEquals(monthValue, dateAmount.getMonthValue());
        Assertions.assertEquals(dayValue, dateAmount.getDayValue());
        Assertions.assertEquals(amount, dateAmount.getAmount());
        Assertions.assertEquals(accountName, dateAmount.getAccount().getName());
        Assertions.assertTrue(dateAmount.getId() > 0);
    }

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(savedUserRole);
        UserEntity userEntity = new UserEntity("spring@spring.io", "First", "Last", "Spring", "pass1234", roleSet);
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);
        Account account1 = new Account(savedUserEntity, "Bank A");
        Account account2 = new Account(savedUserEntity, "Bank B");
        savedAccount1 = accountRepository.save(account1);
        savedAccount2 = accountRepository.save(account2);
        dateAmount1 = new DateAmount(savedAccount1, (short) 2024, (byte) 3, (byte) 25, 98765L);
        dateAmount2 = new DateAmount(savedAccount2, (short) 2023, (byte) 11, (byte) 22, 1000L);
        dateAmount3 = new DateAmount(savedAccount2, (short) 2024, (byte) 3, (byte) 7, 1500L);
        dateAmount4 = new DateAmount(savedAccount2, (short) 2024, (byte) 2, (byte) 5, 2000L);
        dateAmount5 = new DateAmount(savedAccount2, (short) 2024, (byte) 2, (byte) 15, 1800L);
    }

    @Test
    public void DateAmountRepository_Save() {
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount1);

        verifyDateAmount(savedDateAmount, (short) 2024, (byte) 3, (byte) 25, 98765L, "Bank A");
    }

    @Test
    public void DateAmountRepository_FindById() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount5);
        Long savedDateAmountId = savedDateAmount.getId();

        Optional<DateAmount> foundDateAmountOptional = dateAmountRepository.findById(savedDateAmountId);

        Assertions.assertTrue(foundDateAmountOptional.isPresent());
        DateAmount foundDateAmount = foundDateAmountOptional.get();
        verifyDateAmount(foundDateAmount, (short) 2024, (byte) 2, (byte) 15, 1800L, "Bank B");
    }

    @Test
    public void DateAmountRepository_FindByAccountId() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long savedAccountId = savedAccount1.getId();

        List<DateAmount> foundDateAmounts = dateAmountRepository.findByAccountId(savedAccountId);

        Assertions.assertNotNull(foundDateAmounts);
        Assertions.assertEquals(1, foundDateAmounts.size());
        foundDateAmounts.sort(this::compareDateAmounts);
        verifyDateAmount(foundDateAmounts.getFirst(), (short) 2024, (byte) 3, (byte) 25, 98765L, "Bank A");
    }

    @Test
    public void DateAmountRepository_FindByYear() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long savedAccountId = savedAccount2.getId();

        List<DateAmount> foundDateAmounts = dateAmountRepository
                .findByAccountIdAndYearValue(savedAccountId, (short) 2024);

        Assertions.assertNotNull(foundDateAmounts);
        Assertions.assertEquals(3, foundDateAmounts.size());
        foundDateAmounts.sort(this::compareDateAmounts);
        verifyDateAmount(foundDateAmounts.get(0), (short) 2024, (byte) 2, (byte) 5, 2000L, "Bank B");
        verifyDateAmount(foundDateAmounts.get(1), (short) 2024, (byte) 2, (byte) 15, 1800L, "Bank B");
        verifyDateAmount(foundDateAmounts.get(2), (short) 2024, (byte) 3, (byte) 7, 1500L, "Bank B");
    }

    @Test
    public void DateAmountRepository_FindByMonth() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long savedAccountId = savedAccount2.getId();

        List<DateAmount> foundDateAmounts = dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValue(savedAccountId, (short) 2024, (byte) 2);

        Assertions.assertNotNull(foundDateAmounts);
        Assertions.assertEquals(2, foundDateAmounts.size());
        foundDateAmounts.sort(this::compareDateAmounts);
        verifyDateAmount(foundDateAmounts.getFirst(), (short) 2024, (byte) 2, (byte) 5, 2000L, "Bank B");
        verifyDateAmount(foundDateAmounts.getLast(), (short) 2024, (byte) 2, (byte) 15, 1800L, "Bank B");
    }

    @Test
    public void DateAmountRepository_FindByDay() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long savedAccountId = savedAccount2.getId();

        List<DateAmount> foundDateAmounts = dateAmountRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(
                        savedAccountId, (short) 2024, (byte) 2, (byte) 15);

        Assertions.assertNotNull(foundDateAmounts);
        Assertions.assertEquals(1, foundDateAmounts.size());
        foundDateAmounts.sort(this::compareDateAmounts);
        verifyDateAmount(foundDateAmounts.getFirst(), (short) 2024, (byte) 2, (byte) 15, 1800L, "Bank B");
    }

    @Test
    public void DateAmountRepository_Update() {
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount1);
        Long dateAmountId = savedDateAmount.getId();
        Optional<DateAmount> foundDateAmountOptional = dateAmountRepository.findById(dateAmountId);
        Assertions.assertTrue(foundDateAmountOptional.isPresent());
        DateAmount foundDateAmount = foundDateAmountOptional.get();
        DateAmountUpdateRequest dateAmountUpdateRequest = new DateAmountUpdateRequest(77777L);

        foundDateAmount.update(dateAmountUpdateRequest);
        dateAmountRepository.save(foundDateAmount);

        Optional<DateAmount> updatedFoundDateAmountOptional = dateAmountRepository.findById(dateAmountId);
        Assertions.assertTrue(updatedFoundDateAmountOptional.isPresent());
        DateAmount updatedFoundDateAmount = updatedFoundDateAmountOptional.get();
        verifyDateAmount(updatedFoundDateAmount, (short) 2024, (byte) 3, (byte) 25, 77777L, "Bank A");
    }

    @Test
    public void DateAmountRepository_Delete() {
        DateAmount savedDateAmount = dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long dateAmountId = savedDateAmount.getId();

        dateAmountRepository.deleteById(dateAmountId);

        Optional<DateAmount> foundDateAmountOptional = dateAmountRepository.findById(dateAmountId);
        Assertions.assertTrue(foundDateAmountOptional.isEmpty());
    }

    @Test
    public void DateAmountRepository_DeleteByAccountId() {
        dateAmountRepository.save(dateAmount1);
        dateAmountRepository.save(dateAmount2);
        dateAmountRepository.save(dateAmount3);
        dateAmountRepository.save(dateAmount4);
        dateAmountRepository.save(dateAmount5);
        Long accountId = savedAccount1.getId();

        dateAmountRepository.deleteByAccountId(accountId);

        List<DateAmount> foundDateAmountList = dateAmountRepository.findByAccountId(accountId);
        Assertions.assertNotNull(foundDateAmountList);
        Assertions.assertTrue(foundDateAmountList.isEmpty());
    }
}
