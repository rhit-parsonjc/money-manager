package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.services.AccountService.AccountServiceImpl;
import com.moneymanager.api.services.MapperService.MapperService;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

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
public class AccountServiceTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Long userEntityId;
    private UserEntity userEntity;

    private Account account1;
    private Account account2;

    private void verifyAccount(Account account, long id, String name, String username) {
        Assertions.assertEquals(id, account.getId());
        Assertions.assertEquals(name, account.getName());
        Assertions.assertEquals(username, account.getUserEntity().getUsername());
    }

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        userEntityId = 1L;
        userEntity = new TestUserEntity(userEntityId, "Spring", "pass1234",
                roleSet, new HashSet<Account>());
        account1 = new TestAccount(1L, "Bank Alpha", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        account2 = new TestAccount(2L, "Bank Beta", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account1);
        userEntity.getAccounts().add(account2);

        when(userEntityService.getAuthenticatedUserOrThrow()).thenReturn(userEntity);
    }

    @Test
    public void AccountService_CreateAccount() {
        Long accountId = 3L;
        AccountRequest accountRequest = new AccountRequest("Bank Gamma");
        Account account = new TestAccount(accountId, "Bank Gamma", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        List<Account> emptyAccountList = new ArrayList<Account>();
        when(accountRepository.findByUserEntityIdAndName(userEntityId, "Bank Gamma")).thenReturn(emptyAccountList);
        when(mapperService.mapAccountRequestToAccount(userEntity, accountRequest)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(accountRequest);

        Assertions.assertNotNull(createdAccount);
        verifyAccount(createdAccount, accountId, "Bank Gamma", "Spring");
        verify(accountRepository, times(1)).findByUserEntityIdAndName(userEntityId, "Bank Gamma");
        verify(mapperService, times(1)).mapAccountRequestToAccount(userEntity, accountRequest);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void AccountService_CreateAccount_DuplicateName() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Bank Alpha");
        List<Account> singletonAccountList = new ArrayList<Account>();
        singletonAccountList.add(account1);
        when(accountRepository.findByUserEntityIdAndName(userEntityId, "Bank Alpha")).thenReturn(singletonAccountList);

        Assertions.assertThrows(AlreadyExistsException.class,
                () -> accountService.createAccount(accountRequest));
    }

    @Test
    public void AccountService_GetAccountById() {
        Long accountId = 2L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account2));

        Account foundAccount = accountService.getAccountById(accountId);

        Assertions.assertNotNull(foundAccount);
        verifyAccount(foundAccount, accountId, "Bank Beta", "Spring");
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void AccountService_GetAccountById_NonexistentAccount() {
        Long accountId = 3L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountById(accountId));
    }

    @Test
    public void AccountService_GetAccounts() {
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account1);
        accountList.add(account2);
        when(accountRepository.findByUserEntityId(userEntityId)).thenReturn(accountList);

        List<Account> foundAccountList = accountService.getAccounts();

        Assertions.assertNotNull(foundAccountList);
        Assertions.assertEquals(2, foundAccountList.size());
        verifyAccount(foundAccountList.getFirst(), 1L, "Bank Alpha", "Spring");
        verifyAccount(foundAccountList.getLast(), 2L, "Bank Beta", "Spring");
        verify(accountRepository, times(1)).findByUserEntityId(userEntityId);
    }

    @Test
    public void AccountService_UpdateAccount() {
        Long accountId = 2L;
        AccountRequest accountRequest = new AccountRequest("Bank 2");
        List<Account> emptyAccountList = new ArrayList<Account>();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account2));
        when(accountRepository.findByUserEntityIdAndName(userEntityId, "Bank 2")).thenReturn(emptyAccountList);
        when(accountRepository.save(account2)).thenReturn(account2);

        Account updatedAccount = accountService.updateAccount(accountId, accountRequest);

        Assertions.assertNotNull(updatedAccount);
        verifyAccount(updatedAccount, 2L, "Bank 2", "Spring");
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).findByUserEntityIdAndName(userEntityId, "Bank 2");
        verify(accountRepository, times(1)).save(account2);
    }

    @Test
    public void AccountService_UpdateAccount_NonexistentAccount() {
        Long accountId = 3L;
        AccountRequest accountRequest = new AccountRequest("Bank Gamma");
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountById(accountId));
    }

    @Test
    public void AccountService_UpdateAccount_DuplicateName() {
        Long accountId = 1L;
        AccountRequest accountRequest = new AccountRequest("Bank Beta");
        List<Account> singletonAccountList = new ArrayList<Account>();
        singletonAccountList.add(account2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account1));
        when(accountRepository.findByUserEntityIdAndName(userEntityId, "Bank Beta")).thenReturn(singletonAccountList);

        Assertions.assertThrows(AlreadyExistsException.class,
                () -> accountService.updateAccount(accountId, accountRequest));
    }

    @Test
    public void AccountService_DeleteAccount() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account1));
        doNothing().when(accountRepository).deleteById(accountId);

        accountService.deleteAccount(accountId);

        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    public void AccountService_DeleteAccount_NonexistentAccount() {
        Long accountId = 3L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> accountService.deleteAccount(accountId));
    }

    @Test
    public void AccountService_DeleteAccounts() {
        doNothing().when(accountRepository).deleteByUserEntityId(userEntityId);

        accountService.deleteAccounts();

        verify(accountRepository, times(1)).deleteByUserEntityId(userEntityId);
    }
}
