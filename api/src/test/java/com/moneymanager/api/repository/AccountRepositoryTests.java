package com.moneymanager.api.repository;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.Role;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.repositories.RoleRepository;
import com.moneymanager.api.repositories.UserEntityRepository;
import com.moneymanager.api.requests.AccountRequest;

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
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private UserEntity savedUserEntity1;
    private UserEntity savedUserEntity2;

    private Account accountA;
    private Account accountB;
    private Account accountC;

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(savedUserRole);
        UserEntity userEntity1 = new UserEntity("Spring1", "pass1234", roleSet);
        UserEntity userEntity2 = new UserEntity("Spring2", "password", roleSet);
        savedUserEntity1 = userEntityRepository.save(userEntity1);
        savedUserEntity2 = userEntityRepository.save(userEntity2);
        accountA = new Account(savedUserEntity1, "Bank A");
        accountB = new Account(savedUserEntity2, "Bank B");
        accountC = new Account(savedUserEntity2, "Bank C");
    }

    @Test
    public void AccountRepository_Save() {
        Account savedAccount = accountRepository.save(accountA);

        Assertions.assertEquals("Bank A", savedAccount.getName());
        Assertions.assertEquals("Spring1", savedAccount.getUserEntity().getUsername());
        Assertions.assertTrue(savedAccount.getId() > 0);
    }

    @Test
    public void AccountRepository_FindById() {
        accountRepository.save(accountA);
        accountRepository.save(accountB);
        Account savedAccount = accountRepository.save(accountC);
        Long accountId = savedAccount.getId();

        Optional<Account> foundAccountOptional = accountRepository.findById(accountId);

        Assertions.assertTrue(foundAccountOptional.isPresent());
        Account foundAccount = foundAccountOptional.get();
        Assertions.assertEquals("Bank C", foundAccount.getName());
    }

    @Test
    public void AccountRepository_FindByUserEntityId() {
        accountRepository.save(accountA);
        accountRepository.save(accountB);
        accountRepository.save(accountC);
        Long userEntityId = savedUserEntity2.getId();

        List<Account> foundAccountList = accountRepository.findByUserEntityId(userEntityId);

        Assertions.assertNotNull(foundAccountList);
        Assertions.assertEquals(2, foundAccountList.size());
        String foundAccount1 = foundAccountList.getFirst().getName();
        String foundAccount2 = foundAccountList.getLast().getName();
        if (foundAccount1.compareTo(foundAccount2) > 0) {
            String tmp = foundAccount1;
            foundAccount1 = foundAccount2;
            foundAccount2 = tmp;
        }
        Assertions.assertEquals("Bank B", foundAccount1);
        Assertions.assertEquals("Bank C", foundAccount2);
    }

    @Test
    public void AccountRepository_FindByUserEntityIdAndName() {
        accountRepository.save(accountA);
        accountRepository.save(accountB);
        accountRepository.save(accountC);
        Long userEntityId = savedUserEntity2.getId();

        List<Account> foundAccountList = accountRepository.findByUserEntityIdAndName(userEntityId, "Bank B");

        Assertions.assertNotNull(foundAccountList);
        Assertions.assertEquals(1, foundAccountList.size());
        Assertions.assertEquals("Bank B", foundAccountList.getFirst().getName());
    }

    @Test
    public void AccountRepository_Update() {
        Account savedAccount = accountRepository.save(accountA);
        Long accountId = savedAccount.getId();
        Optional<Account> foundAccountOptional = accountRepository.findById(accountId);
        Assertions.assertTrue(foundAccountOptional.isPresent());
        Account foundAccount = foundAccountOptional.get();
        AccountRequest updateAccountRequest = new AccountRequest();
        updateAccountRequest.setName("Bank Alpha");

        foundAccount.update(updateAccountRequest);
        accountRepository.save(foundAccount);

        Optional<Account> updatedFoundAccountOptional = accountRepository.findById(accountId);
        Assertions.assertTrue(updatedFoundAccountOptional.isPresent());
        Account updatedFoundAccount = updatedFoundAccountOptional.get();
        Assertions.assertEquals("Bank Alpha", updatedFoundAccount.getName());
    }

    @Test
    public void AccountRepository_Delete() {
        accountRepository.save(accountA);
        Account savedAccount = accountRepository.save(accountB);
        accountRepository.save(accountC);
        Long accountId = savedAccount.getId();

        accountRepository.deleteById(accountId);

        Optional<Account> foundAccountOptional = accountRepository.findById(accountId);
        Assertions.assertTrue(foundAccountOptional.isEmpty());
    }

    @Test
    public void AccountRepository_DeleteByUserEntityId() {
        accountRepository.save(accountA);
        accountRepository.save(accountB);
        accountRepository.save(accountC);
        Long userEntityId = savedUserEntity2.getId();

        accountRepository.deleteByUserEntityId(userEntityId);

        List<Account> foundAccountList = accountRepository.findByUserEntityId(userEntityId);
        Assertions.assertNotNull(foundAccountList);
        Assertions.assertTrue(foundAccountList.isEmpty());
    }
}
