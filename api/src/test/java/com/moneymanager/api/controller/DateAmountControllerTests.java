package com.moneymanager.api.controller;

import com.moneymanager.api.controllers.DateAmountController;
import com.moneymanager.api.dtos.*;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.requests.DateAmountCreateRequest;
import com.moneymanager.api.requests.DateAmountUpdateRequest;
import com.moneymanager.api.services.DateAmountService.DateAmountService;
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

@WebMvcTest(controllers = DateAmountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DateAmountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DateAmountService dateAmountService;

    @MockBean
    private MapperService mapperService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long accountId;

    private DateAmountCreateRequest dateAmountCreateRequest;
    private DateAmountUpdateRequest dateAmountUpdateRequest;
    private DateAmount dateAmount1;
    private DateAmount dateAmount2;
    private DateAmount dateAmount3;
    private DateAmount dateAmount4;
    private DateAmount dateAmount5;
    private DateAmount dateAmountCreated;
    private DateAmount dateAmountUpdated;
    private DateAmountDto dateAmount1Dto;
    private DateAmountDto dateAmount2Dto;
    private DateAmountDto dateAmount3Dto;
    private DateAmountDto dateAmount4Dto;
    private DateAmountDto dateAmount5Dto;
    private DateAmountDto dateAmountCreatedDto;
    private DateAmountDto dateAmountUpdatedDto;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(3L, "Spring", "pass1234", roleSet, new HashSet<Account>());
        accountId = 33L;
        Account account = new TestAccount(accountId, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        dateAmountCreateRequest = new DateAmountCreateRequest();
        dateAmountCreateRequest.setYearValue((short) 2024);
        dateAmountCreateRequest.setMonthValue((byte) 9);
        dateAmountCreateRequest.setDayValue((byte) 6);
        dateAmountCreateRequest.setAmount(800L);
        dateAmountUpdateRequest = new DateAmountUpdateRequest();
        dateAmountUpdateRequest.setAmount(1250L);
        dateAmount1 = new TestDateAmount(1L, account, (short) 2025, (byte) 1, (byte) 13, 899L);
        dateAmount2 = new TestDateAmount(2L, account, (short) 2025, (byte) 3, (byte) 26, 950L);
        dateAmount3 = new TestDateAmount(3L, account, (short) 2025, (byte) 4, (byte) 9, 1100L);
        dateAmount4 = new TestDateAmount(4L, account, (short) 2026, (byte) 8, (byte) 2, 1200L);
        dateAmount5 = new TestDateAmount(5L, account, (short) 2026, (byte) 8, (byte) 3, 1050L);
        dateAmountCreated = new TestDateAmount(6L, account, (short) 2024, (byte) 9, (byte) 6, 800L);
        dateAmountUpdated = new TestDateAmount(5L, account, (short) 2026, (byte) 8, (byte) 3, 1250L);
        dateAmount1Dto = new DateAmountDto(1L, (short) 2025, (byte) 1, (byte) 13, 899L);
        dateAmount2Dto = new DateAmountDto(2L, (short) 2025, (byte) 3, (byte) 26, 950L);
        dateAmount3Dto = new DateAmountDto(3L, (short) 2025, (byte) 4, (byte) 9, 1100L);
        dateAmount4Dto = new DateAmountDto(4L, (short) 2026, (byte) 8, (byte) 2, 1200L);
        dateAmount5Dto = new DateAmountDto(5L, (short) 2026, (byte) 8, (byte) 3, 1050L);
        dateAmountCreatedDto = new DateAmountDto(6L, (short) 2024, (byte) 9, (byte) 6, 800L);
        dateAmountUpdatedDto = new DateAmountDto(5L, (short) 2026, (byte) 8, (byte) 3, 1250L);
    }

    @Test
    public void DateAmountController_CreateDateAmount() throws Exception {
        when(dateAmountService.createDateAmount(accountId, dateAmountCreateRequest)).thenReturn(dateAmountCreated);
        when(mapperService.mapDateAmountToDto(dateAmountCreated)).thenReturn(dateAmountCreatedDto);

        ResultActions response = mockMvc.perform(post("/api/v1/dateamounts/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateAmountCreateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.yearValue", CoreMatchers.is(2024)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.monthValue", CoreMatchers.is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dayValue", CoreMatchers.is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", CoreMatchers.is(800)));
    }

    @Test
    public void DateAmountController_GetDateAmounts() throws Exception {
        List<DateAmount> dateAmounts = new ArrayList<DateAmount>();
        dateAmounts.add(dateAmount1);
        dateAmounts.add(dateAmount2);
        dateAmounts.add(dateAmount3);
        dateAmounts.add(dateAmount4);
        dateAmounts.add(dateAmount5);
        List<DateAmountDto> dateAmountDtos = new ArrayList<DateAmountDto>();
        dateAmountDtos.add(dateAmount1Dto);
        dateAmountDtos.add(dateAmount2Dto);
        dateAmountDtos.add(dateAmount3Dto);
        dateAmountDtos.add(dateAmount4Dto);
        dateAmountDtos.add(dateAmount5Dto);
        when(dateAmountService.getDateAmounts(accountId)).thenReturn(dateAmounts);
        when(mapperService.mapDateAmountsToDtos(dateAmounts)).thenReturn(dateAmountDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/dateamounts/33")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(899)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].amount", CoreMatchers.is(950)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].amount", CoreMatchers.is(1100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].id", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].amount", CoreMatchers.is(1200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[4].id", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[4].amount", CoreMatchers.is(1050)));
    }

    @Test
    public void DateAmountController_GetDateAmounts_ByYear() throws Exception {
        List<DateAmount> dateAmounts = new ArrayList<DateAmount>();
        dateAmounts.add(dateAmount1);
        dateAmounts.add(dateAmount2);
        dateAmounts.add(dateAmount3);
        List<DateAmountDto> dateAmountDtos = new ArrayList<DateAmountDto>();
        dateAmountDtos.add(dateAmount1Dto);
        dateAmountDtos.add(dateAmount2Dto);
        dateAmountDtos.add(dateAmount3Dto);
        when(dateAmountService.getDateAmountsForYear(accountId, (short) 2025)).thenReturn(dateAmounts);
        when(mapperService.mapDateAmountsToDtos(dateAmounts)).thenReturn(dateAmountDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/dateamounts/33")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2025")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(899)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].amount", CoreMatchers.is(950)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].amount", CoreMatchers.is(1100)));
    }

    @Test
    public void DateAmountController_GetDateAmounts_ByMonth() throws Exception {
        List<DateAmount> dateAmounts = new ArrayList<DateAmount>();
        dateAmounts.add(dateAmount4);
        dateAmounts.add(dateAmount5);
        List<DateAmountDto> dateAmountDtos = new ArrayList<DateAmountDto>();
        dateAmountDtos.add(dateAmount4Dto);
        dateAmountDtos.add(dateAmount5Dto);
        when(dateAmountService.getDateAmountsForMonth(accountId, (short) 2026, (byte) 8)).thenReturn(dateAmounts);
        when(mapperService.mapDateAmountsToDtos(dateAmounts)).thenReturn(dateAmountDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/dateamounts/33")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2026")
                .param("month", "8")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(1200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].amount", CoreMatchers.is(1050)));
    }

    @Test
    public void DateAmountController_GetDateAmounts_ByDay() throws Exception {
        List<DateAmount> dateAmounts = new ArrayList<DateAmount>();
        dateAmounts.add(dateAmount3);
        List<DateAmountDto> dateAmountDtos = new ArrayList<DateAmountDto>();
        dateAmountDtos.add(dateAmount3Dto);
        when(dateAmountService.getDateAmountsForDay(accountId, (short) 2025, (byte) 4, (byte) 9)).thenReturn(dateAmounts);
        when(mapperService.mapDateAmountsToDtos(dateAmounts)).thenReturn(dateAmountDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/dateamounts/33")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2025")
                .param("month", "4")
                .param("day", "9")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].amount", CoreMatchers.is(1100)));
    }

    @Test
    public void DateAmountController_UpdateDateAmount() throws Exception {
        when(dateAmountService.updateDateAmount(accountId, (short) 2026, (byte) 8, (byte) 3, dateAmountUpdateRequest)).thenReturn(dateAmountUpdated);
        when(mapperService.mapDateAmountToDto(dateAmountUpdated)).thenReturn(dateAmountUpdatedDto);

        ResultActions response = mockMvc.perform(put("/api/v1/dateamounts/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", "2026")
                        .param("month", "8")
                        .param("day", "3")
                        .content(objectMapper.writeValueAsString(dateAmountUpdateRequest))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", CoreMatchers.is(1250)));
    }

    @Test
    public void DateAmountController_DeleteDateAmount() throws Exception {
        doNothing().when(dateAmountService).deleteDateAmount(accountId, (short) 2026, (byte) 8, (byte) 2);

        ResultActions response = mockMvc.perform(delete("/api/v1/dateamounts/33")
                .contentType(MediaType.APPLICATION_JSON)
                .param("year", "2026")
                .param("month", "8")
                .param("day", "2")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void DateAmountController_DeleteDateAmounts() throws Exception {
        doNothing().when(dateAmountService).deleteDateAmounts(accountId);

        ResultActions response = mockMvc.perform(delete("/api/v1/dateamounts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountId", "33")
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
