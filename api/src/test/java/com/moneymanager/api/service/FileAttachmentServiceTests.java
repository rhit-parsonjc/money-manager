package com.moneymanager.api.service;

import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.models.test.*;
import com.moneymanager.api.repositories.FileAttachmentRepository;
import com.moneymanager.api.repositories.ItemRepository;
import com.moneymanager.api.services.FileAttachmentService.FileAttachmentServiceImpl;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileAttachmentServiceTests {
    @Mock
    private FileAttachmentRepository fileAttachmentRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private FileAttachmentServiceImpl fileAttachmentService;

    private Long bankRecordId;
    private Long financialTransactionId;
    private BankRecord bankRecord;
    private FinancialTransaction financialTransaction;

    private byte[] contents1;
    private byte[] contents2;
    private byte[] contents3;

    private FileAttachment fileAttachment1;
    private FileAttachment fileAttachment2;
    private FileAttachment fileAttachment3;

    @BeforeEach
    public void setup() {
        Role userRole = new TestRole(1L, "USER");
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(userRole);
        UserEntity userEntity = new TestUserEntity(1L, "Spring", "pass1234",
                roleSet, new HashSet<Account>());
        Account account = new TestAccount(1L, "Bank A", userEntity,
            new HashSet<BankRecord>(), new HashSet<FinancialTransaction>(), new HashSet<DateAmount>());
        userEntity.getAccounts().add(account);
        bankRecordId = 1L;
        bankRecord = new TestBankRecord(bankRecordId, account, (short) 2024, (byte) 9, (byte) 17, 800L,
            "Snack", new HashSet<FileAttachment>(), new HashSet<FinancialTransaction>());
        account.getBankRecords().add(bankRecord);
        financialTransactionId = 2L;
        financialTransaction = new TestFinancialTransaction(financialTransactionId, account, (short) 2024, (byte) 10, (byte) 24, 300L,
                "Water Bottle", new HashSet<FileAttachment>(), new HashSet<BankRecord>());
        account.getFinancialTransactions().add(financialTransaction);
        contents1 = new byte[100];
        for (int i = 0; i < 100; i++)
            contents1[i] = (byte) (i + 1);
        contents2 = new byte[500];
        for (int i = 0; i < 500; i++)
            contents2[i] = (byte) 111;
        contents3 = new byte[15];
        for (int i = 0; i < 15; i++)
            contents3[i] = (byte) (i * i);
        fileAttachment1 = new TestFileAttachment(1L, "receipt", "PNG", 1000L, contents1, bankRecord);
        bankRecord.getFileAttachments().add(fileAttachment1);
        fileAttachment2 = new TestFileAttachment(2L, "invoice", "PDF", 5000L, contents2, bankRecord);
        bankRecord.getFileAttachments().add(fileAttachment2);
        fileAttachment3 = new TestFileAttachment(3L, "purchase", "TXT", 100L, contents3, financialTransaction);
        financialTransaction.getFileAttachments().add(fileAttachment3);
        when(userEntityService.getAuthenticatedUserOrThrow()).thenReturn(userEntity);
    }

    @Test
    public void FileAttachmentService_GetFileAttachment() {
        Long fileAttachmentId = 1L;
        when(fileAttachmentRepository.findById(fileAttachmentId)).thenReturn(Optional.of(fileAttachment1));

        FileAttachment foundFileAttachment = fileAttachmentService.getFileAttachment(fileAttachmentId);

        Assertions.assertNotNull(foundFileAttachment);
        Assertions.assertEquals("receipt", foundFileAttachment.getName());
        Assertions.assertEquals("PNG", foundFileAttachment.getType());
        Assertions.assertEquals(1000L, foundFileAttachment.getSize());
        Assertions.assertArrayEquals(contents1, foundFileAttachment.getContents());
        Assertions.assertEquals(1L, foundFileAttachment.getId());
    }

    @Test
    public void FileAttachmentService_GetFileAttachment_NonexistentAttachment() {
        Long fileAttachmentId = 4L;
        when(fileAttachmentRepository.findById(fileAttachmentId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> fileAttachmentService.getFileAttachment(fileAttachmentId));
    }

    @Test
    public void FileAttachmentService_GetFileAttachmentByItemId_BankRecord() {
        List<FileAttachment> fileAttachmentList = new ArrayList<FileAttachment>();
        fileAttachmentList.add(fileAttachment1);
        fileAttachmentList.add(fileAttachment2);
        when(itemRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(fileAttachmentRepository.findByItemId(bankRecordId)).thenReturn(fileAttachmentList);

        List<FileAttachment> foundFileAttachmentList = fileAttachmentService.getFileAttachmentsByItemId(bankRecordId);

        Assertions.assertNotNull(foundFileAttachmentList);
        Assertions.assertEquals(2, foundFileAttachmentList.size());
        Assertions.assertEquals(1L, foundFileAttachmentList.getFirst().getId());
        Assertions.assertEquals(1000L, foundFileAttachmentList.getFirst().getSize());
        Assertions.assertEquals(2L, foundFileAttachmentList.getLast().getId());
        Assertions.assertEquals(5000L, foundFileAttachmentList.getLast().getSize());
    }

    @Test
    public void FileAttachmentService_GetFileAttachmentByItemId_FinancialTransaction() {
        List<FileAttachment> fileAttachmentList = new ArrayList<FileAttachment>();
        fileAttachmentList.add(fileAttachment3);
        when(itemRepository.findById(financialTransactionId)).thenReturn(Optional.of(financialTransaction));
        when(fileAttachmentRepository.findByItemId(financialTransactionId)).thenReturn(fileAttachmentList);

        List<FileAttachment> foundFileAttachmentList = fileAttachmentService.getFileAttachmentsByItemId(financialTransactionId);

        Assertions.assertNotNull(foundFileAttachmentList);
        Assertions.assertEquals(1, foundFileAttachmentList.size());
        Assertions.assertEquals(3L, foundFileAttachmentList.getFirst().getId());
        Assertions.assertEquals(100L, foundFileAttachmentList.getFirst().getSize());
    }

    @Test
    public void FileAttachmentService_GetFileAttachmentByItemId_NonexistentItem() {
        Long itemId = 3L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> fileAttachmentService.getFileAttachmentsByItemId(itemId));
    }

    @Test
    public void FileAttachmentService_DeleteFileAttachment() {
        Long fileAttachmentId = 2L;
        when(fileAttachmentRepository.findById(fileAttachmentId)).thenReturn(Optional.of(fileAttachment2));
        doNothing().when(fileAttachmentRepository).deleteById(fileAttachmentId);

        fileAttachmentService.deleteFileAttachment(fileAttachmentId);

        verify(fileAttachmentRepository, times(1)).deleteById(fileAttachmentId);
    }

    @Test
    public void FileAttachmentService_DeleteFileAttachmentsByItemId_BankRecord() {
        List<FileAttachment> fileAttachmentList = new ArrayList<FileAttachment>();
        fileAttachmentList.add(fileAttachment1);
        fileAttachmentList.add(fileAttachment2);
        when(itemRepository.findById(bankRecordId)).thenReturn(Optional.of(bankRecord));
        when(fileAttachmentRepository.findByItemId(bankRecordId)).thenReturn(fileAttachmentList);
        doNothing().when(fileAttachmentRepository).deleteByItemId(bankRecordId);

        fileAttachmentService.deleteFileAttachmentsByItemId(bankRecordId);

        verify(fileAttachmentRepository, times(1)).deleteByItemId(bankRecordId);
    }

    @Test
    public void FileAttachmentService_DeleteFileAttachmentsByItemId_FinancialTransaction() {
        List<FileAttachment> fileAttachmentList = new ArrayList<FileAttachment>();
        fileAttachmentList.add(fileAttachment3);
        when(itemRepository.findById(financialTransactionId)).thenReturn(Optional.of(financialTransaction));
        when(fileAttachmentRepository.findByItemId(financialTransactionId)).thenReturn(fileAttachmentList);
        doNothing().when(fileAttachmentRepository).deleteByItemId(financialTransactionId);

        fileAttachmentService.deleteFileAttachmentsByItemId(financialTransactionId);

        verify(fileAttachmentRepository, times(1)).deleteByItemId(financialTransactionId);
    }

    @Test
    public void FileAttachmentService_DeleteFileAttachmentByItemId_NonexistentItem() {
        Long itemId = 3L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> fileAttachmentService.deleteFileAttachmentsByItemId(itemId));
    }
}
