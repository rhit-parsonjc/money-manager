package com.moneymanager.api.services.ValidatorService;

import com.moneymanager.api.requests.*;

public interface ValidatorService {
    void validate(AccountRequest accountRequest);
    void validate(BankRecordRequest bankRecordRequest);
    void validate(DateAmountCreateRequest dateAmountCreateRequest);
    void validate(DateAmountUpdateRequest dateAmountUpdateRequest);
    void validate(FinancialTransactionRequest financialTransactionRequest);
    void validate(LoginRequest loginRequest);
    void validate(RegisterRequest registerRequest);
    void validate(UserUpdateRequest userUpdateRequest);
}
