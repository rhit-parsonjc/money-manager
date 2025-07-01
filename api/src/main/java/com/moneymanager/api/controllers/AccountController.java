package com.moneymanager.api.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.dtos.AccountDto;
import com.moneymanager.api.models.Account;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final MapperService mapperService;

    @PostMapping("")
    public ResponseEntity<DataOrErrorResponse> createAccount(
            @RequestBody AccountRequest accountRequest
    ) {
        Account account = accountService.createAccount(accountRequest);
        AccountDto accountDto = mapperService.mapAccountToDto(account);
        DataOrErrorResponse response = new DataOrErrorResponse(true, accountDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DataOrErrorResponse> getAccountById(
            @PathVariable Long id
    ) {
        Account account = accountService.getAccountById(id);
        AccountDto accountDto = mapperService.mapAccountToDto(account);
        DataOrErrorResponse response = new DataOrErrorResponse(true, accountDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        List<AccountDto> accountDtos = mapperService.mapAccountsToDtos(accounts);
        DataOrErrorResponse response = new DataOrErrorResponse(true, accountDtos);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<DataOrErrorResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest accountRequest
    ) {
        Account account = accountService.updateAccount(id, accountRequest);
        AccountDto accountDto = mapperService.mapAccountToDto(account);
        DataOrErrorResponse response = new DataOrErrorResponse(true, accountDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DataOrErrorResponse> deleteAccount(
            @PathVariable Long id
    ) {
        accountService.deleteAccount(id);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteAccounts() {
        accountService.deleteAccounts();
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
