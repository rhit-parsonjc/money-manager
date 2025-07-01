package com.moneymanager.api.services.AccountService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.moneymanager.api.exceptions.AlreadyExistsException;
import com.moneymanager.api.exceptions.PermissionsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.repositories.AccountRepository;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.services.MapperService.MapperService;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final MapperService mapperService;
    private final UserEntityService userEntityService;

    private Optional<Account> getAccountByName(UserEntity userEntity, String name) {
        List<Account> accounts = accountRepository.findByUserEntityIdAndName(userEntity.getId(), name);
        if (accounts.isEmpty())
            return Optional.empty();
        else
            return Optional.of(accounts.getFirst());
    }

    @Override
    public Account createAccount(AccountRequest request) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        Optional<Account> existingAccountOptional = this.getAccountByName(userEntity, request.getName());
        if (existingAccountOptional.isPresent())
            throw new AlreadyExistsException(ResourceNotFoundException.ACCOUNT_MESSAGE);
        Account account = mapperService.mapAccountRequestToAccount(userEntity, request);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.ACCOUNT_MESSAGE);
        Account account = accountOptional.get();
        if (!Objects.equals(account.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return account;
    }

    @Override
    public List<Account> getAccounts() {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        return accountRepository.findByUserEntityId(userEntity.getId());
    }

    @Override
    public Account updateAccount(Long id, AccountRequest request) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.ACCOUNT_MESSAGE);
        Account account = accountOptional.get();
        if (!Objects.equals(account.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        Optional<Account> existingAccountOptional = this.getAccountByName(userEntity, request.getName());
        if (existingAccountOptional.isPresent())
            throw new AlreadyExistsException(ResourceNotFoundException.ACCOUNT_MESSAGE);
        account.update(request);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.ACCOUNT_MESSAGE);
        Account account = accountOptional.get();
        if (!Objects.equals(account.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAccounts() {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        accountRepository.deleteByUserEntityId(userEntity.getId());
    }
}
