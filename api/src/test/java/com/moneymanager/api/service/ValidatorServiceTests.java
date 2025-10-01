package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.InvalidRequestException;
import com.moneymanager.api.requests.*;
import com.moneymanager.api.services.ValidatorService.ValidatorService;
import com.moneymanager.api.services.ValidatorService.ValidatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidatorServiceTests {
    private ValidatorService validatorService;

    @BeforeEach
    public void setup() {
        validatorService = new ValidatorServiceImpl();
    }

    @Test
    public void ValidatorService_AccountRequest() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Account A");

        validatorService.validate(accountRequest);
    }

    @Test
    public void ValidatorService_AccountRequest_EmptyName() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(accountRequest));
    }

    @Test
    public void ValidatorService_AccountRequest_NullName() {
        AccountRequest accountRequest = new AccountRequest();

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(accountRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        validatorService.validate(bankRecordRequest);
    }

    @Test
    public void ValidatorService_BankRecordRequest_NullYear() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_NullMonth() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_NullDay() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_NullAmount() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_EmptyName() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_NullName() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_InvalidYear() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) -100);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 31);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_InvalidMonth() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 14);
        bankRecordRequest.setDayValue((byte) 30);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_InvalidDay() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 0);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_InvalidDayForMonth() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 9);
        bankRecordRequest.setDayValue((byte) 31);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_NotLeapYear1() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2025);
        bankRecordRequest.setMonthValue((byte) 2);
        bankRecordRequest.setDayValue((byte) 29);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_LeapYear1() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2024);
        bankRecordRequest.setMonthValue((byte) 2);
        bankRecordRequest.setDayValue((byte) 29);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        validatorService.validate(bankRecordRequest);
    }

    @Test
    public void ValidatorService_BankRecordRequest_NotLeapYear2() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 1900);
        bankRecordRequest.setMonthValue((byte) 2);
        bankRecordRequest.setDayValue((byte) 29);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(bankRecordRequest));
    }

    @Test
    public void ValidatorService_BankRecordRequest_LeapYear2() {
        BankRecordRequest bankRecordRequest = new BankRecordRequest();
        bankRecordRequest.setYearValue((short) 2000);
        bankRecordRequest.setMonthValue((byte) 2);
        bankRecordRequest.setDayValue((byte) 29);
        bankRecordRequest.setAmount(-1000L);
        bankRecordRequest.setName("Lunch");

        validatorService.validate(bankRecordRequest);
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setMonthValue((byte) 11);
        dateAmountCreateRequest.setDayValue((byte) 14);
        dateAmountCreateRequest.setAmount(123400L);

        validatorService.validate(dateAmountCreateRequest);
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest_NullYear() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setMonthValue((byte) 11);
        dateAmountCreateRequest.setDayValue((byte) 14);
        dateAmountCreateRequest.setAmount(123400L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountCreateRequest));
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest_NullMonth() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setDayValue((byte) 14);
        dateAmountCreateRequest.setAmount(123400L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountCreateRequest));
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest_NullDay() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setMonthValue((byte) 11);
        dateAmountCreateRequest.setAmount(123400L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountCreateRequest));
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest_NullAmount() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setMonthValue((byte) 11);
        dateAmountCreateRequest.setDayValue((byte) 14);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountCreateRequest));
    }

    @Test
    public void ValidatorService_DateAmountCreateRequest_InvalidDate() {
        DateAmountCreateRequest dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setMonthValue((byte) 11);
        dateAmountCreateRequest.setDayValue((byte) 31);
        dateAmountCreateRequest.setAmount(123400L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountCreateRequest));
    }

    @Test
    public void ValidatorService_DateAmountUpdateRequest() {
        DateAmountUpdateRequest dateAmountUpdateRequest = new DateAmountUpdateRequest();
        dateAmountUpdateRequest.setAmount(98875L);

        validatorService.validate(dateAmountUpdateRequest);
    }

    @Test
    public void ValidatorService_DateAmountUpdateRequest_NullAmount() {
        DateAmountUpdateRequest dateAmountUpdateRequest = new DateAmountUpdateRequest();

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(dateAmountUpdateRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        validatorService.validate(financialTransactionRequest);
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NullYear() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NullMonth() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NullDay() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NullAmount() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_EmptyName() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NullName() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_InvalidYear() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 0);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_InvalidMonth() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) -2);
        financialTransactionRequest.setDayValue((byte) 22);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_InvalidDay() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 7);
        financialTransactionRequest.setDayValue((byte) 33);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_InvalidDayForMonth() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 6);
        financialTransactionRequest.setDayValue((byte) 31);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NotLeapYear1() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2023);
        financialTransactionRequest.setMonthValue((byte) 2);
        financialTransactionRequest.setDayValue((byte) 29);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_LeapYear1() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2020);
        financialTransactionRequest.setMonthValue((byte) 2);
        financialTransactionRequest.setDayValue((byte) 29);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        validatorService.validate(financialTransactionRequest);
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_NotLeapYear2() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2100);
        financialTransactionRequest.setMonthValue((byte) 2);
        financialTransactionRequest.setDayValue((byte) 29);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(financialTransactionRequest));
    }

    @Test
    public void ValidatorService_FinancialTransactionRequest_LeapYear2() {
        FinancialTransactionRequest financialTransactionRequest = new FinancialTransactionRequest();
        financialTransactionRequest.setYearValue((short) 2000);
        financialTransactionRequest.setMonthValue((byte) 2);
        financialTransactionRequest.setDayValue((byte) 29);
        financialTransactionRequest.setAmount(-2500L);
        financialTransactionRequest.setName("Dinner");

        validatorService.validate(financialTransactionRequest);
    }

    @Test
    public void ValidatorService_LoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        validatorService.validate(loginRequest);
    }

    @Test
    public void ValidatorService_LoginRequest_EmptyUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(loginRequest));
    }

    @Test
    public void ValidatorService_LoginRequest_NullUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(loginRequest));
    }

    @Test
    public void ValidatorService_LoginRequest_EmptyPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(loginRequest));
    }

    @Test
    public void ValidatorService_LoginRequest_NullPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(loginRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        validatorService.validate(registerRequest);
    }

    @Test
    public void ValidatorService_RegisterRequest_EmptyEmail() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_NullEmail() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        validatorService.validate(registerRequest);
    }

    @Test
    public void ValidatorService_RegisterRequest_EmptyFirstName() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_NullFirstName() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        validatorService.validate(registerRequest);
    }

    @Test
    public void ValidatorService_RegisterRequest_EmptyLastName() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_NullLastName() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");

        validatorService.validate(registerRequest);
    }

    @Test
    public void ValidatorService_RegisterRequest_EmptyUsername() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("");
        registerRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_NullUsername() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setPassword("password");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_EmptyPassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");
        registerRequest.setPassword("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_RegisterRequest_NullPassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setFirstName("First");
        registerRequest.setLastName("Last");
        registerRequest.setUsername("username");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(registerRequest));
    }

    @Test
    public void ValidatorService_UserUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_EmptyEmail() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_NullEmail() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_EmptyFirstName() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_NullFirstName() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_EmptyLastName() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_NullLastName() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setPassword("updated");

        validatorService.validate(userUpdateRequest);
    }

    @Test
    public void ValidatorService_UserUpdateRequest_EmptyPassword() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("UpdatedLast");
        userUpdateRequest.setPassword("");

        Assertions.assertThrows(InvalidRequestException.class,
                () -> validatorService.validate(userUpdateRequest));
    }

    @Test
    public void ValidatorService_UserUpdateRequest_NullPassword() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@updated.com");
        userUpdateRequest.setFirstName("UpdatedFirst");
        userUpdateRequest.setLastName("UpdatedLast");

        validatorService.validate(userUpdateRequest);
    }
}
