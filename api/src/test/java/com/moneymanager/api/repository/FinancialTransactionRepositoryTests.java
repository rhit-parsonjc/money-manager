package com.moneymanager.api.repository;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.FinancialTransaction;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.FinancialTransactionRequest;

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
public class FinancialTransactionRepositoryTests {
    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Account savedAccount1;

    private FinancialTransaction financialTransaction1;
    private FinancialTransaction financialTransaction2;
    private FinancialTransaction financialTransaction3;
    private FinancialTransaction financialTransaction4;
    private FinancialTransaction financialTransaction5;

    private int compareTransactions(FinancialTransaction transaction1, FinancialTransaction transaction2) {
        if (transaction1.getYearValue().shortValue() != transaction2.getYearValue().shortValue())
            return transaction1.getYearValue() - transaction2.getYearValue();
        if (transaction1.getMonthValue().byteValue() != transaction2.getMonthValue().byteValue())
            return transaction1.getMonthValue() - transaction2.getMonthValue();
        if (transaction1.getDayValue().byteValue() != transaction2.getDayValue().byteValue())
            return transaction1.getDayValue() - transaction2.getDayValue();
        return transaction1.getName().compareTo(transaction2.getName());
    }

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(savedUserRole);
        UserEntity userEntity = new UserEntity("Spring", "pass1234", roleSet);
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);
        Account account1 = new Account(savedUserEntity, "Bank A");
        Account account2 = new Account(savedUserEntity, "Bank B");
        savedAccount1 = accountRepository.save(account1);
        Account savedAccount2 = accountRepository.save(account2);
        financialTransaction1 = new FinancialTransaction(savedAccount1, (short) 2025, (byte) 7, (byte) 15, -4096L, "Phone Bill");
        financialTransaction2 = new FinancialTransaction(savedAccount1, (short) 2025, (byte) 7, (byte) 28, -1888L, "Breakfast");
        financialTransaction3 = new FinancialTransaction(savedAccount1, (short) 2025, (byte) 8, (byte) 2, -333L, "Candy");
        financialTransaction4 = new FinancialTransaction(savedAccount1, (short) 2026, (byte) 8, (byte) 2, -1000L, "Office Supplies");
        financialTransaction5 = new FinancialTransaction(savedAccount2, (short) 2025, (byte) 7, (byte) 15, -4096L, "Phone Bill");
    }

    @Test
    public void FinancialTransactionRepository_Save() {
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction1);

        Assertions.assertEquals((short) 2025, savedFinancialTransaction.getYearValue());
        Assertions.assertEquals((byte) 7, savedFinancialTransaction.getMonthValue());
        Assertions.assertEquals((byte) 15, savedFinancialTransaction.getDayValue());
        Assertions.assertEquals(-4096L, savedFinancialTransaction.getAmount());
        Assertions.assertEquals("Phone Bill", savedFinancialTransaction.getName());
        Assertions.assertEquals("Bank A", savedFinancialTransaction.getAccount().getName());
        Assertions.assertTrue(savedFinancialTransaction.getId() > 0);
    }

    @Test
    public void FinancialTransactionRepository_FindById() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long savedFinancialTransactionId = savedFinancialTransaction.getId();

        Optional<FinancialTransaction> foundTransactionOptional = financialTransactionRepository.findById(savedFinancialTransactionId);

        Assertions.assertTrue(foundTransactionOptional.isPresent());
        FinancialTransaction foundTransaction = foundTransactionOptional.get();
        Assertions.assertEquals("Candy", foundTransaction.getName());
    }

    @Test
    public void FinancialTransactionRepository_FindByAccountId() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long savedAccountId = savedAccount1.getId();

        List<FinancialTransaction> foundTransactions = financialTransactionRepository.findByAccountId(savedAccountId);

        Assertions.assertNotNull(foundTransactions);
        Assertions.assertEquals(4, foundTransactions.size());
        foundTransactions.sort(this::compareTransactions);
        Assertions.assertEquals("Phone Bill", foundTransactions.get(0).getName());
        Assertions.assertEquals("Breakfast", foundTransactions.get(1).getName());
        Assertions.assertEquals("Candy", foundTransactions.get(2).getName());
        Assertions.assertEquals("Office Supplies", foundTransactions.get(3).getName());
    }

    @Test
    public void FinancialTransactionRepository_FindByYear() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long savedAccountId = savedAccount1.getId();

        List<FinancialTransaction> foundTransactions = financialTransactionRepository
                .findByAccountIdAndYearValue(savedAccountId, (short) 2025);

        Assertions.assertNotNull(foundTransactions);
        Assertions.assertEquals(3, foundTransactions.size());
        foundTransactions.sort(this::compareTransactions);
        Assertions.assertEquals("Phone Bill", foundTransactions.get(0).getName());
        Assertions.assertEquals("Breakfast", foundTransactions.get(1).getName());
        Assertions.assertEquals("Candy", foundTransactions.get(2).getName());
    }

    @Test
    public void FinancialTransactionRepository_FindByMonth() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long savedAccountId = savedAccount1.getId();

        List<FinancialTransaction> foundTransactions = financialTransactionRepository
                .findByAccountIdAndYearValueAndMonthValue(savedAccountId, (short) 2025, (byte) 7);

        Assertions.assertNotNull(foundTransactions);
        Assertions.assertEquals(2, foundTransactions.size());
        foundTransactions.sort(this::compareTransactions);
        Assertions.assertEquals("Phone Bill", foundTransactions.get(0).getName());
        Assertions.assertEquals("Breakfast", foundTransactions.get(1).getName());
    }

    @Test
    public void FinancialTransactionRepository_FindByDay() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long savedAccountId = savedAccount1.getId();

        List<FinancialTransaction> foundTransactions = financialTransactionRepository
                .findByAccountIdAndYearValueAndMonthValueAndDayValue(
                        savedAccountId, (short) 2026, (byte) 8, (byte) 2);

        Assertions.assertNotNull(foundTransactions);
        Assertions.assertEquals(1, foundTransactions.size());
        foundTransactions.sort(this::compareTransactions);
        Assertions.assertEquals("Office Supplies", foundTransactions.get(0).getName());
    }

    @Test
    public void FinancialTransactionRepository_Update() {
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction1);
        Long financialTransactionId = savedFinancialTransaction.getId();
        Optional<FinancialTransaction> foundTransactionOptional = financialTransactionRepository.findById(financialTransactionId);
        Assertions.assertTrue(foundTransactionOptional.isPresent());
        FinancialTransaction foundTransaction = foundTransactionOptional.get();
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 4);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(12345L);
        financialTransactionRequest.setName("Streaming Subscription");

        foundTransaction.update(financialTransactionRequest);
        financialTransactionRepository.save(foundTransaction);

        Optional<FinancialTransaction> updatedFoundTransactionOptional = financialTransactionRepository.findById(financialTransactionId);
        Assertions.assertTrue(updatedFoundTransactionOptional.isPresent());
        FinancialTransaction updatedFoundTransaction = updatedFoundTransactionOptional.get();
        Assertions.assertEquals((short) 2023, updatedFoundTransaction.getYearValue());
        Assertions.assertEquals((byte) 4, updatedFoundTransaction.getMonthValue());
        Assertions.assertEquals((byte) 22, updatedFoundTransaction.getDayValue());
        Assertions.assertEquals(12345L, updatedFoundTransaction.getAmount());
        Assertions.assertEquals("Streaming Subscription", updatedFoundTransaction.getName());
    }

    @Test
    public void FinancialTransactionRepository_Delete() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction5);
        Long financialTransactionId = savedFinancialTransaction.getId();

        financialTransactionRepository.deleteById(financialTransactionId);

        Optional<FinancialTransaction> foundTransactionOptional = financialTransactionRepository.findById(financialTransactionId);
        Assertions.assertTrue(foundTransactionOptional.isEmpty());
    }

    @Test
    public void FinancialTransactionRepository_DeleteByAccountId() {
        financialTransactionRepository.save(financialTransaction1);
        financialTransactionRepository.save(financialTransaction2);
        financialTransactionRepository.save(financialTransaction3);
        financialTransactionRepository.save(financialTransaction4);
        financialTransactionRepository.save(financialTransaction5);
        Long accountId = savedAccount1.getId();

        financialTransactionRepository.deleteByAccountId(accountId);

        List<FinancialTransaction> foundTransactionList = financialTransactionRepository.findByAccountId(accountId);
        Assertions.assertNotNull(foundTransactionList);
        Assertions.assertTrue(foundTransactionList.isEmpty());
    }
}
