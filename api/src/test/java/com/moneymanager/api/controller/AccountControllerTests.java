package com.moneymanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneymanager.api.controllers.AccountController;
import com.moneymanager.api.dtos.AccountDto;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.requests.AccountRequest;
import com.moneymanager.api.services.AccountService.AccountService;
import com.moneymanager.api.services.MapperService.MapperService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private MapperService mapperService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountRequest accountCreateRequest;
    private AccountRequest accountUpdateRequest;
    private Account account1;
    private Account account2;
    private Account accountCreated;
    private Account accountUpdated;
    private AccountDto accountDto1;
    private AccountDto accountDto2;
    private AccountDto accountCreatedDto;
    private AccountDto accountUpdatedDto;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234", roleSet, new HashSet<Account>());
        accountCreateRequest = new AccountRequest();
        accountCreateRequest.setName("Bank 3");
        accountUpdateRequest = new AccountRequest();
        accountUpdateRequest.setName("Bank A");
        account1 = new TestAccount(1L, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        account2 = new TestAccount(2L, "Bank 2", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        accountCreated = new TestAccount(3L, "Bank 3", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        accountUpdated = new TestAccount(1L, "Bank 2", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        accountDto1 = new AccountDto(1L, "Bank 1");
        accountDto2 = new AccountDto(2L, "Bank 2");
        accountCreatedDto = new AccountDto(3L, "Bank 3");
        accountUpdatedDto = new AccountDto(1L, "Bank A");
        userEntity.getAccounts().add(account1);
        userEntity.getAccounts().add(account2);
    }

    @Test
    public void AccountController_CreateAccount() throws Exception {
        when(accountService.createAccount(accountCreateRequest)).thenReturn(accountCreated);
        when(mapperService.mapAccountToDto(accountCreated)).thenReturn(accountCreatedDto);

        ResultActions response = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Bank 3")));
    }

    @Test
    public void AccountController_GetAccountById() throws Exception {
        when(accountService.getAccountById(2L)).thenReturn(account2);
        when(mapperService.mapAccountToDto(account2)).thenReturn(accountDto2);

        ResultActions response = mockMvc.perform(get("/api/v1/accounts/2")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Bank 2")));
    }

    @Test
    public void AccountController_GetAccounts() throws Exception {
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(account1);
        accountList.add(account2);
        List<AccountDto> accountDtoList = new ArrayList<AccountDto>();
        accountDtoList.add(accountDto1);
        accountDtoList.add(accountDto2);
        when(accountService.getAccounts()).thenReturn(accountList);
        when(mapperService.mapAccountsToDtos(accountList)).thenReturn(accountDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", CoreMatchers.is("Bank 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", CoreMatchers.is("Bank 2")));

    }

    @Test
    public void AccountController_UpdateAccount() throws Exception {
        when(accountService.updateAccount(1L, accountUpdateRequest)).thenReturn(accountUpdated);
        when(mapperService.mapAccountToDto(accountUpdated)).thenReturn(accountUpdatedDto);

        ResultActions response = mockMvc.perform(put("/api/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountUpdateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Bank A")));
    }

    @Test
    public void AccountController_DeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(2L);

        ResultActions response = mockMvc.perform(delete("/api/v1/accounts/2")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void AccountController_DeleteAccounts() throws Exception {
        doNothing().when(accountService).deleteAccounts();

        ResultActions response = mockMvc.perform(delete("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
