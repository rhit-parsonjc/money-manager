package com.moneymanager.api.controller;

import com.moneymanager.api.controllers.FinancialTransactionController;
import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.requests.FinancialTransactionRequest;
import com.moneymanager.api.services.FinancialTransactionService.FinancialTransactionService;
import com.moneymanager.api.services.MapperService.MapperService;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(controllers = FinancialTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FinancialTransactionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinancialTransactionService financialTransactionService;

    @MockBean
    private MapperService mapperService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long accountId = 4L;
    private FinancialTransactionRequest financialTransactionCreateRequest;
    private FinancialTransactionRequest financialTransactionUpdateRequest;
    private FinancialTransaction financialTransaction1;
    private FinancialTransaction financialTransaction2;
    private FinancialTransaction financialTransaction3;
    private FinancialTransaction financialTransactionCreated;
    private FinancialTransaction financialTransactionUpdated;
    private FinancialTransactionDto financialTransaction1Dto;
    private FinancialTransactionDto financialTransaction2Dto;
    private FinancialTransactionDto financialTransaction3Dto;
    private FinancialTransactionDto financialTransactionCreatedDto;
    private FinancialTransactionDto financialTransactionUpdatedDto;
    private FinancialTransactionDetailsDto financialTransaction3DetailsDto;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234", roleSet, new HashSet<Account>());
        Account account = new TestAccount(accountId, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        financialTransactionCreateRequest = new FinancialTransactionRequest(
                (short) 2023, (byte) 8, (byte) 31, 500L, "Movie D");
        financialTransactionUpdateRequest = new FinancialTransactionRequest(
                (short) 2025, (byte) 5, (byte) 21, 900L, "Movie E");
        financialTransaction1 = new TestFinancialTransaction(1L, account, (short) 2024, (byte) 2, (byte) 5, -100L,
                "Movie A", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransaction2 = new TestFinancialTransaction(2L, account, (short) 2024, (byte) 8, (byte) 9, -1200L,
                "Movie B", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransaction3 = new TestFinancialTransaction(3L, account, (short) 2025, (byte) 7, (byte) 23, -1000L,
                "Movie C", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransactionCreated = new TestFinancialTransaction(4L, account, (short) 2023, (byte) 8, (byte) 31, -500L,
                "Movie D", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransactionUpdated = new TestFinancialTransaction(3L, account, (short) 2025, (byte) 5, (byte) 21, -900L,
                "Movie E", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        financialTransaction1Dto = new FinancialTransactionDto(1L, (short) 2024, (byte) 2, (byte) 5, -100L, "Movie A");
        financialTransaction2Dto = new FinancialTransactionDto(2L, (short) 2024, (byte) 8, (byte) 9, -1200L, "Movie B");
        financialTransaction3Dto = new FinancialTransactionDto(3L, (short) 2025, (byte) 7, (byte) 23, -1000L, "Movie C");
        financialTransactionCreatedDto = new FinancialTransactionDto(4L, (short) 2023, (byte) 8, (byte) 31, -500L, "Movie D");
        financialTransactionUpdatedDto = new FinancialTransactionDto(3L, (short) 2025, (byte) 5, (byte) 21, -900L, "Movie E");
        financialTransaction3DetailsDto = new FinancialTransactionDetailsDto(3L, (short) 2025, (byte) 7, (byte) 23, -1000L,
                "Movie C", new ArrayList<BankRecordDto>(), new ArrayList<FileAttachmentDto>());
        account.getFinancialTransactions().add(financialTransaction1);
        account.getFinancialTransactions().add(financialTransaction2);
        account.getFinancialTransactions().add(financialTransaction3);
    }

    @Test
    public void FinancialTransactionController_CreateFinancialTransaction() throws Exception {
        when(financialTransactionService.createFinancialTransaction(accountId, financialTransactionCreateRequest)).thenReturn(financialTransactionCreated);
        when(mapperService.mapFinancialTransactionToDto(financialTransactionCreated)).thenReturn(financialTransactionCreatedDto);

        ResultActions response = mockMvc.perform(post("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionCreateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.yearValue", CoreMatchers.is(2023)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthValue", CoreMatchers.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dayValue", CoreMatchers.is(31)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", CoreMatchers.is(-500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Movie D")));
    }
    @Test
    public void FinancialTransactionController_GetFinancialTransactionById() throws Exception {
        when(financialTransactionService.getFinancialTransactionById(accountId, 3L)).thenReturn(financialTransaction3);
        when(mapperService.mapFinancialTransactionToDetailsDto(financialTransaction3)).thenReturn(financialTransaction3DetailsDto);

        ResultActions response = mockMvc.perform(get("/api/v1/financialtransactions/4/3")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Movie C")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.yearValue", CoreMatchers.is(2025)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthValue", CoreMatchers.is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dayValue", CoreMatchers.is(23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", CoreMatchers.is(-1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Movie C")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bankRecords.size()", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileAttachments.size()", CoreMatchers.is(0)));
    }

    @Test
    public void FinancialTransactionController_GetFinancialTransactions() throws Exception {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction1);
        financialTransactionList.add(financialTransaction2);
        financialTransactionList.add(financialTransaction3);
        List<FinancialTransactionDto> financialTransactionDtoList = new ArrayList<FinancialTransactionDto>();
        financialTransactionDtoList.add(financialTransaction1Dto);
        financialTransactionDtoList.add(financialTransaction2Dto);
        financialTransactionDtoList.add(financialTransaction3Dto);
        when(financialTransactionService.getFinancialTransactions(accountId)).thenReturn(financialTransactionList);
        when(mapperService.mapFinancialTransactionsToDtos(financialTransactionList)).thenReturn(financialTransactionDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].monthValue", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].dayValue", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(-100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", CoreMatchers.is("Movie A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].monthValue", CoreMatchers.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].dayValue", CoreMatchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].amount", CoreMatchers.is(-1200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", CoreMatchers.is("Movie B")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].yearValue", CoreMatchers.is(2025)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].monthValue", CoreMatchers.is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].dayValue", CoreMatchers.is(23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].amount", CoreMatchers.is(-1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name", CoreMatchers.is("Movie C")));
    }

    @Test
    public void FinancialTransactionController_GetFinancialTransactions_ByYear() throws Exception {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction1);
        financialTransactionList.add(financialTransaction2);
        List<FinancialTransactionDto> financialTransactionDtoList = new ArrayList<FinancialTransactionDto>();
        financialTransactionDtoList.add(financialTransaction1Dto);
        financialTransactionDtoList.add(financialTransaction2Dto);
        when(financialTransactionService.getFinancialTransactionsForYear(accountId, (short) 2024)).thenReturn(financialTransactionList);
        when(mapperService.mapFinancialTransactionsToDtos(financialTransactionList)).thenReturn(financialTransactionDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2024")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].monthValue", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].dayValue", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(-100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", CoreMatchers.is("Movie A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].monthValue", CoreMatchers.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].dayValue", CoreMatchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].amount", CoreMatchers.is(-1200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", CoreMatchers.is("Movie B")));
    }

    @Test
    public void FinancialTransactionController_GetFinancialTransactions_ByMonth() throws Exception {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        List<FinancialTransactionDto> financialTransactionDtoList = new ArrayList<FinancialTransactionDto>();
        when(financialTransactionService.getFinancialTransactionsForMonth(accountId, (short) 2024, (byte) 3)).thenReturn(financialTransactionList);
        when(mapperService.mapFinancialTransactionsToDtos(financialTransactionList)).thenReturn(financialTransactionDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2024")
                .param("month", "3")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(0)));
    }

    @Test
    public void financialTransactionController_GetFinancialTransactions_ByDay() throws Exception {
        List<FinancialTransaction> financialTransactionList = new ArrayList<FinancialTransaction>();
        financialTransactionList.add(financialTransaction1);
        List<FinancialTransactionDto> financialTransactionDtoList = new ArrayList<FinancialTransactionDto>();
        financialTransactionDtoList.add(financialTransaction1Dto);
        when(financialTransactionService.getFinancialTransactionsForDay(accountId, (short) 2024, (byte) 2, (byte) 5)).thenReturn(financialTransactionList);
        when(mapperService.mapFinancialTransactionsToDtos(financialTransactionList)).thenReturn(financialTransactionDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2024")
                .param("month", "2")
                .param("day", "5")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].monthValue", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].dayValue", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(-100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", CoreMatchers.is("Movie A")));
    }

    @Test
    public void FinancialTransactionController_UpdateFinancialTransaction() throws Exception {
        when(financialTransactionService.updateFinancialTransaction(accountId, 3L, financialTransactionUpdateRequest)).thenReturn(financialTransactionUpdated);
        when(mapperService.mapFinancialTransactionToDto(financialTransactionUpdated)).thenReturn(financialTransactionUpdatedDto);

        ResultActions response = mockMvc.perform(put("/api/v1/financialtransactions/4/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionUpdateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.yearValue", CoreMatchers.is(2025)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthValue", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dayValue", CoreMatchers.is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", CoreMatchers.is(-900)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", CoreMatchers.is("Movie E")));
    }

    @Test
    public void FinancialTransactionController_DeleteFinancialTransaction() throws Exception {
        doNothing().when(financialTransactionService).deleteFinancialTransaction(accountId, 1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/financialtransactions/4/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void FinancialTransactionController_DeleteFinancialTransactions() throws Exception {
        doNothing().when(financialTransactionService).deleteFinancialTransactions(accountId);

        ResultActions response = mockMvc.perform(delete("/api/v1/financialtransactions/4")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
