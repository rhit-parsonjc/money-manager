package com.moneymanager.api.services.AccountService;

import com.moneymanager.api.models.Account;
import com.moneymanager.api.requests.AccountRequest;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountRequest request);

    Account getAccountById(Long id);
    List<Account> getAccounts();

    Account updateAccount(Long id, AccountRequest request);

    void deleteAccount(Long id);
    void deleteAccounts();
}
