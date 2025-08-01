package com.moneymanager.api.controller;

import com.moneymanager.api.controllers.RecordTransactionController;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.services.RecordTransactionConnectionService.RecordTransactionConnectionService;

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

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = RecordTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RecordTransactionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecordTransactionConnectionService recordTransactionConnectionService;

    private Long bankRecordId1;
    private Long bankRecordId2;
    private Long financialTransactionId1;
    private Long financialTransactionId2;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234", roleSet, new HashSet<Account>());
        Account account = new TestAccount(1L, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        bankRecordId1 = 17L;
        bankRecordId2 = 29L;
        financialTransactionId1 = 3L;
        financialTransactionId2 = 44L;
        BankRecord bankRecord1 = new TestBankRecord(bankRecordId1, account, (short) 2025, (byte) 7, (byte) 31, -500L,
                "Streaming Subscription Alpha", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        BankRecord bankRecord2 = new TestBankRecord(bankRecordId2, account, (short) 2025, (byte) 9, (byte) 22, -1000L,
                "Streaming Subscription Beta", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        FinancialTransaction financialTransaction1 = new TestFinancialTransaction(financialTransactionId1, account, (short) 2025, (byte) 7,
                (byte) 28, 500L, "Alpha", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        FinancialTransaction financialTransaction2 = new TestFinancialTransaction(financialTransactionId2, account, (short) 2024, (byte) 11,
                (byte) 3, 900L, "Gamma", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getBankRecords().add(bankRecord1);
        account.getBankRecords().add(bankRecord2);
        account.getFinancialTransactions().add(financialTransaction1);
        account.getFinancialTransactions().add(financialTransaction2);
    }

    @Test
    public void RecordTransactionController_CreateConnection() throws Exception {
        doNothing().when(recordTransactionConnectionService).createConnection(bankRecordId2, financialTransactionId2);

        ResultActions response = mockMvc.perform(post("/api/v1/recordtransactions/29/44")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void RecordTransactionController_DeleteConnection() throws Exception {
        doNothing().when(recordTransactionConnectionService).deleteConnection(bankRecordId1, financialTransactionId1);

        ResultActions response = mockMvc.perform(delete("/api/v1/recordtransactions/17/3")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
