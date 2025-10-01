package com.moneymanager.api.controller;

import com.moneymanager.api.controllers.BankRecordController;
import com.moneymanager.api.dtos.BankRecordDetailsDto;
import com.moneymanager.api.dtos.BankRecordDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.dtos.FinancialTransactionDto;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.TestAccount;
import com.moneymanager.api.models.test.TestBankRecord;
import com.moneymanager.api.models.test.TestRole;
import com.moneymanager.api.models.test.TestUserEntity;
import com.moneymanager.api.requests.BankRecordRequest;
import com.moneymanager.api.services.BankRecordService.BankRecordService;
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

@WebMvcTest(controllers = BankRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BankRecordControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankRecordService bankRecordService;

    @MockBean
    private MapperService mapperService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long accountId = 4L;
    private BankRecordRequest bankRecordCreateRequest;
    private BankRecordRequest bankRecordUpdateRequest;
    private BankRecord bankRecord1;
    private BankRecord bankRecord2;
    private BankRecord bankRecord3;
    private BankRecord bankRecordCreated;
    private BankRecord bankRecordUpdated;
    private BankRecordDto bankRecord1Dto;
    private BankRecordDto bankRecord2Dto;
    private BankRecordDto bankRecord3Dto;
    private BankRecordDto bankRecordCreatedDto;
    private BankRecordDto bankRecordUpdatedDto;
    private BankRecordDetailsDto bankRecord3DetailsDto;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "spring@spring.boot", "Spring",
                "Boot", "SpringBoot", "pass1234", roleSet, new HashSet<Account>());
        Account account = new TestAccount(accountId, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        bankRecordCreateRequest = new BankRecordRequest((short) 2023, (byte) 8, (byte) 31, -500L, "Movie D");
        bankRecordUpdateRequest = new BankRecordRequest((short) 2025, (byte) 5, (byte) 21, -900L, "Movie E");
        bankRecord1 = new TestBankRecord(1L, account, (short) 2024, (byte) 2, (byte) 5, -100L,
                "Movie A", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecord2 = new TestBankRecord(2L, account, (short) 2024, (byte) 8, (byte) 9, -1200L,
                "Movie B", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecord3 = new TestBankRecord(3L, account, (short) 2025, (byte) 7, (byte) 23, -1000L,
                "Movie C", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecordCreated = new TestBankRecord(4L, account, (short) 2023, (byte) 8, (byte) 31, -500L,
                "Movie D", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecordUpdated = new TestBankRecord(3L, account, (short) 2025, (byte) 5, (byte) 21, -900L,
                "Movie E", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        bankRecord1Dto = new BankRecordDto(1L, (short) 2024, (byte) 2, (byte) 5, -100L, "Movie A");
        bankRecord2Dto = new BankRecordDto(2L, (short) 2024, (byte) 8, (byte) 9, -1200L, "Movie B");
        bankRecord3Dto = new BankRecordDto(3L, (short) 2025, (byte) 7, (byte) 23, -1000L, "Movie C");
        bankRecordCreatedDto = new BankRecordDto(4L, (short) 2023, (byte) 8, (byte) 31, -500L, "Movie D");
        bankRecordUpdatedDto = new BankRecordDto(3L, (short) 2025, (byte) 5, (byte) 21, -900L, "Movie E");
        bankRecord3DetailsDto = new BankRecordDetailsDto(3L, (short) 2025, (byte) 7, (byte) 23, -1000L,
                "Movie C", new ArrayList<FinancialTransactionDto>(), new ArrayList<FileAttachmentDto>());
        account.getBankRecords().add(bankRecord1);
        account.getBankRecords().add(bankRecord2);
        account.getBankRecords().add(bankRecord3);
    }

    @Test
    public void BankRecordController_CreateBankRecord() throws Exception {
        when(bankRecordService.createBankRecord(accountId, bankRecordCreateRequest)).thenReturn(bankRecordCreated);
        when(mapperService.mapBankRecordToDto(bankRecordCreated)).thenReturn(bankRecordCreatedDto);

        ResultActions response = mockMvc.perform(post("/api/v1/bankrecords/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankRecordCreateRequest))
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
    public void BankRecordController_GetBankRecordById() throws Exception {
        when(bankRecordService.getBankRecordById(accountId, 3L)).thenReturn(bankRecord3);
        when(mapperService.mapBankRecordToDetailsDto(bankRecord3)).thenReturn(bankRecord3DetailsDto);

        ResultActions response = mockMvc.perform(get("/api/v1/bankrecords/4/3")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.financialTransactions.size()", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileAttachments.size()", CoreMatchers.is(0)));
    }

    @Test
    public void BankRecordController_GetBankRecords() throws Exception {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        bankRecordList.add(bankRecord2);
        bankRecordList.add(bankRecord3);
        List<BankRecordDto> bankRecordDtoList = new ArrayList<BankRecordDto>();
        bankRecordDtoList.add(bankRecord1Dto);
        bankRecordDtoList.add(bankRecord2Dto);
        bankRecordDtoList.add(bankRecord3Dto);
        when(bankRecordService.getBankRecords(accountId)).thenReturn(bankRecordList);
        when(mapperService.mapBankRecordsToDtos(bankRecordList)).thenReturn(bankRecordDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/bankrecords/4")
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
    public void BankRecordController_GetBankRecords_ByYear() throws Exception {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        bankRecordList.add(bankRecord2);
        List<BankRecordDto> bankRecordDtoList = new ArrayList<BankRecordDto>();
        bankRecordDtoList.add(bankRecord1Dto);
        bankRecordDtoList.add(bankRecord2Dto);
        when(bankRecordService.getBankRecordsForYear(accountId, (short) 2024)).thenReturn(bankRecordList);
        when(mapperService.mapBankRecordsToDtos(bankRecordList)).thenReturn(bankRecordDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/bankrecords/4")
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
    public void BankRecordController_GetBankRecords_ByMonth() throws Exception {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        List<BankRecordDto> bankRecordDtoList = new ArrayList<BankRecordDto>();
        when(bankRecordService.getBankRecordsForMonth(accountId, (short) 2024, (byte) 3)).thenReturn(bankRecordList);
        when(mapperService.mapBankRecordsToDtos(bankRecordList)).thenReturn(bankRecordDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/bankrecords/4")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2024")
                .param("month", "3")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(0)));
    }

    @Test
    public void BankRecordController_GetBankRecords_ByDay() throws Exception {
        List<BankRecord> bankRecordList = new ArrayList<BankRecord>();
        bankRecordList.add(bankRecord1);
        List<BankRecordDto> bankRecordDtoList = new ArrayList<BankRecordDto>();
        bankRecordDtoList.add(bankRecord1Dto);
        when(bankRecordService.getBankRecordsForDay(accountId, (short) 2024, (byte) 2, (byte) 5)).thenReturn(bankRecordList);
        when(mapperService.mapBankRecordsToDtos(bankRecordList)).thenReturn(bankRecordDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/bankrecords/4")
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
    public void BankRecordController_UpdateBankRecord() throws Exception {
        when(bankRecordService.updateBankRecord(accountId, 3L, bankRecordUpdateRequest)).thenReturn(bankRecordUpdated);
        when(mapperService.mapBankRecordToDto(bankRecordUpdated)).thenReturn(bankRecordUpdatedDto);

        ResultActions response = mockMvc.perform(put("/api/v1/bankrecords/4/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankRecordUpdateRequest))
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
    public void BankRecordController_DeleteBankRecord() throws Exception {
        doNothing().when(bankRecordService).deleteBankRecord(accountId, 1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/bankrecords/4/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void BankRecordController_DeleteBankRecords() throws Exception {
        doNothing().when(bankRecordService).deleteBankRecords(accountId);

        ResultActions response = mockMvc.perform(delete("/api/v1/bankrecords/4")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
