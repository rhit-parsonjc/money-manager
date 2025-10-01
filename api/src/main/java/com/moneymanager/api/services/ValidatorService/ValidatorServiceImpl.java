package com.moneymanager.api.services.ValidatorService;

import com.moneymanager.api.exceptions.InvalidRequestException;
import com.moneymanager.api.requests.*;
import org.springframework.stereotype.Service;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private boolean empty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean validDate(short yearValue, byte monthValue, byte dayValue) {
        if (yearValue < 1)
            return false;
        if (monthValue < 1 || monthValue > 12)
            return false;
        if (dayValue < 1 || dayValue > 31)
            return false;
        boolean isLeapYear = yearValue % 400 == 0 || (yearValue % 100 != 0 && yearValue % 4 == 0);
        int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear) {
            daysPerMonth[1]++;
        }
        return dayValue <= daysPerMonth[monthValue - 1];
    }

    @Override
    public void validate(AccountRequest accountRequest) {
        if (empty(accountRequest.getName()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
    }

    @Override
    public void validate(BankRecordRequest bankRecordRequest) {
        if (bankRecordRequest.getYearValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (bankRecordRequest.getMonthValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (bankRecordRequest.getDayValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (bankRecordRequest.getAmount() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (empty(bankRecordRequest.getName()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (!validDate(bankRecordRequest.getYearValue(),
                bankRecordRequest.getMonthValue(), bankRecordRequest.getDayValue()))
            throw new InvalidRequestException(InvalidRequestException.INVALID_DATE);
    }

    @Override
    public void validate(DateAmountCreateRequest dateAmountCreateRequest) {
        if (dateAmountCreateRequest.getYearValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (dateAmountCreateRequest.getMonthValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (dateAmountCreateRequest.getDayValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (dateAmountCreateRequest.getAmount() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (!validDate(dateAmountCreateRequest.getYearValue(),
                dateAmountCreateRequest.getMonthValue(), dateAmountCreateRequest.getDayValue()))
            throw new InvalidRequestException(InvalidRequestException.INVALID_DATE);
    }

    @Override
    public void validate(DateAmountUpdateRequest dateAmountUpdateRequest) {
        if (dateAmountUpdateRequest.getAmount() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
    }

    @Override
    public void validate(FinancialTransactionRequest financialTransactionRequest) {
        if (financialTransactionRequest.getYearValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (financialTransactionRequest.getMonthValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (financialTransactionRequest.getDayValue() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (financialTransactionRequest.getAmount() == null)
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (empty(financialTransactionRequest.getName()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (!validDate(financialTransactionRequest.getYearValue(),
                financialTransactionRequest.getMonthValue(), financialTransactionRequest.getDayValue()))
            throw new InvalidRequestException(InvalidRequestException.INVALID_DATE);
    }

    @Override
    public void validate(LoginRequest loginRequest) {
        if (empty(loginRequest.getUsername()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (empty(loginRequest.getPassword()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
    }

    @Override
    public void validate(RegisterRequest registerRequest) {
        if (registerRequest.getEmail() != null && registerRequest.getEmail().isEmpty())
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (registerRequest.getFirstName() != null && registerRequest.getFirstName().isEmpty())
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (registerRequest.getLastName() != null && registerRequest.getLastName().isEmpty())
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (empty(registerRequest.getUsername()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
        if (empty(registerRequest.getPassword()))
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
    }

    @Override
    public void validate(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.getPassword() != null && userUpdateRequest.getPassword().isEmpty())
            throw new InvalidRequestException(InvalidRequestException.EMPTY_FIELD);
    }
}
