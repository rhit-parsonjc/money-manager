package com.moneymanager.api.controller;

import com.moneymanager.api.controllers.FileAttachmentController;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.services.FileAttachmentService.FileAttachmentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = FileAttachmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FileAttachmentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileAttachmentService fileAttachmentService;

    @MockBean
    private MapperService mapperService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long itemId;
    private Item item;
    private byte[] contents1;
    private byte[] contents2;
    private FileAttachment fileAttachment1;
    private FileAttachment fileAttachment2;
    private FileAttachmentDto fileAttachment1Dto;
    private FileAttachmentDto fileAttachment2Dto;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234", roleSet, new HashSet<Account>());
        Account account = new TestAccount(1L, "Bank 1", userEntity,
                new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        itemId = 1L;
        item = new TestBankRecord(itemId, account, (short) 2026, (byte) 8, (byte) 21, 999L,
                "Song A", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        contents1 = new byte[100];
        for (int i = 0; i < 100; i++)
            contents1[i] = (byte) i;
        contents2 = new byte[250];
        for (int i = 0; i < 250; i++)
            contents2[i] = (byte) (i % 10);
        fileAttachment1 = new TestFileAttachment(1L, "Purchase-Record", "DOC", 200L, contents1, item);
        fileAttachment2 = new TestFileAttachment(2L, "SongReceipt", "JPG", 500L, contents2, item);
        fileAttachment1Dto = new FileAttachmentDto(1L, "Purchase-Record", "DOC", 200L);
        fileAttachment2Dto = new FileAttachmentDto(2L, "SongReceipt", "JPG", 500L);
    }

    @Test
    public void FileAttachmentController_GetFileAttachmentsForItem() throws Exception {
        List<FileAttachment> fileAttachmentList = new ArrayList<FileAttachment>();
        fileAttachmentList.add(fileAttachment1);
        fileAttachmentList.add(fileAttachment2);
        List<FileAttachmentDto> fileAttachmentDtoList = new ArrayList<FileAttachmentDto>();
        fileAttachmentDtoList.add(fileAttachment1Dto);
        fileAttachmentDtoList.add(fileAttachment2Dto);
        when(fileAttachmentService.getFileAttachmentsByItemId(itemId)).thenReturn(fileAttachmentList);
        when(mapperService.mapFileAttachmentsToDtos(fileAttachmentList)).thenReturn(fileAttachmentDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/fileattachments")
                .contentType(MediaType.APPLICATION_JSON)
                .param("itemId", "" + itemId)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", CoreMatchers.is("Purchase-Record")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", CoreMatchers.is("SongReceipt")));
    }

    @Test
    public void FileAttachmentController_DeleteFileAttachment() throws Exception {
        doNothing().when(fileAttachmentService).deleteFileAttachment(1L);

        ResultActions response = mockMvc.perform(delete("/api/v1/fileattachments/1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }

    @Test
    public void FileAttachmentController_DeleteFileAttachmentsForItem() throws Exception {
        doNothing().when(fileAttachmentService).deleteFileAttachmentsByItemId(itemId);

        ResultActions response = mockMvc.perform(delete("/api/v1/fileattachments")
                .contentType(MediaType.APPLICATION_JSON)
                .param("itemId", "" + itemId)
        );

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", CoreMatchers.nullValue()));
    }
}
