package com.moneymanager.api.services.AccountService;

import java.util.List;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.requests.AccountRequest;

public interface AccountService {
    Account createAccount(AccountRequest request);

    Account getAccountById(Long id);
    List<Account> getAccounts();

    Account updateAccount(Long id, AccountRequest request);

    void deleteAccount(Long id);
    void deleteAccounts();
}
