package com.moneymanager.api.repository;

import com.moneymanager.api.models.*;
import com.moneymanager.api.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FileAttachmentRepositoryTests {
    @Autowired
    private FileAttachmentRepository fileAttachmentRepository;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private BankRecordRepository bankRecordRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BankRecord savedBankRecord;
    private FinancialTransaction savedFinancialTransaction;

    private byte[] contents1;
    private byte[] contents2;
    private byte[] contents3;
    private byte[] contents4;

    private FileAttachment fileAttachment1;
    private FileAttachment fileAttachment2;
    private FileAttachment fileAttachment3;
    private FileAttachment fileAttachment4;

    @BeforeEach
    public void setup() {
        Role userRole = new Role("USER");
        Role savedUserRole = roleRepository.save(userRole);
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(savedUserRole);
        UserEntity userEntity = new UserEntity("Spring", "password", roleSet);
        UserEntity savedUserEntity = userEntityRepository.save(userEntity);
        Account account = new Account(savedUserEntity, "Bank A");
        Account savedAccount = accountRepository.save(account);
        BankRecord bankRecord = new BankRecord(savedAccount, (short) 2025, (byte) 5, (byte) 11, 2525L, "Book");
        savedBankRecord = bankRecordRepository.save(bankRecord);
        FinancialTransaction financialTransaction = new FinancialTransaction(
                savedAccount, (short) 2025, (byte) 4, (byte) 23, 3838L, "Snacks");
        savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        contents1 = new byte[100];
        for (int i = 0; i < 100; i++)
            contents1[i] = (byte) (i + 1);
        contents2 = new byte[50];
        for (int i = 0; i < 50; i++)
            contents1[i] = 88;
        contents3 = new byte[250];
        for (int i = 0; i < 250; i++)
            contents3[i] = (byte) (255 - i);
        contents4 = new byte[1000];
        for (int i = 0; i < 1000; i++)
            contents4[i] = (byte) (i % 200 + 1);
        fileAttachment1 = new FileAttachment("receipt", "png", 100L, contents1, savedBankRecord);
        fileAttachment2 = new FileAttachment("email", "pdf", 50L, contents2, savedBankRecord);
        fileAttachment3 = new FileAttachment("invoice", "pdf", 250L, contents3, savedFinancialTransaction);
        fileAttachment4 = new FileAttachment("invoice", "txt", 1000L, contents4, savedFinancialTransaction);
    }

    @Test
    public void FileAttachmentRepository_Save() {
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment1);

        Assertions.assertEquals("receipt", savedFileAttachment.getName());
        Assertions.assertEquals("png", savedFileAttachment.getType());
        Assertions.assertEquals(100L, savedFileAttachment.getSize());
        Assertions.assertArrayEquals(contents1, savedFileAttachment.getContents());
        Assertions.assertEquals("Book", savedFileAttachment.getItem().getName());
        Assertions.assertTrue(savedFileAttachment.getId() > 0);
    }

    @Test
    public void FileAttachmentRepository_FindById() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        fileAttachmentRepository.save(fileAttachment3);
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment4);
        Long savedFileAttachmentId = savedFileAttachment.getId();

        Optional<FileAttachment> foundFileAttachmentOptional = fileAttachmentRepository.findById(savedFileAttachmentId);

        Assertions.assertTrue(foundFileAttachmentOptional.isPresent());
        FileAttachment foundFileAttachment = foundFileAttachmentOptional.get();
        Assertions.assertEquals(1000L, foundFileAttachment.getSize());
    }

    @Test
    public void FileAttachmentRepository_FindByRecordId() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        fileAttachmentRepository.save(fileAttachment3);
        fileAttachmentRepository.save(fileAttachment4);
        Long savedRecordId = savedBankRecord.getId();

        List<FileAttachment> fileAttachmentList = fileAttachmentRepository.findByItemId(savedRecordId);

        Assertions.assertNotNull(fileAttachmentList);
        Assertions.assertEquals(2, fileAttachmentList.size());
        Long fileSize1 = fileAttachmentList.getFirst().getSize();
        Long fileSize2 = fileAttachmentList.getLast().getSize();
        if (fileSize1 > fileSize2) {
            Long tmp = fileSize1;
            fileSize1 = fileSize2;
            fileSize2 = tmp;
        }
        Assertions.assertEquals(50L, fileSize1);
        Assertions.assertEquals(100L, fileSize2);
    }

    @Test
    public void FileAttachmentRepository_FindByTransactionId() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        fileAttachmentRepository.save(fileAttachment3);
        fileAttachmentRepository.save(fileAttachment4);
        Long savedTransactionId = savedFinancialTransaction.getId();

        List<FileAttachment> fileAttachmentList = fileAttachmentRepository.findByItemId(savedTransactionId);

        Assertions.assertNotNull(fileAttachmentList);
        Assertions.assertEquals(2, fileAttachmentList.size());
        Long fileSize1 = fileAttachmentList.getFirst().getSize();
        Long fileSize2 = fileAttachmentList.getLast().getSize();
        if (fileSize1 > fileSize2) {
            Long tmp = fileSize1;
            fileSize1 = fileSize2;
            fileSize2 = tmp;
        }
        Assertions.assertEquals(250L, fileSize1);
        Assertions.assertEquals(1000L, fileSize2);
    }

    @Test
    public void FileAttachmentRepository_DeleteById() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        FileAttachment savedFileAttachment = fileAttachmentRepository.save(fileAttachment3);
        fileAttachmentRepository.save(fileAttachment4);
        Long fileAttachmentId = savedFileAttachment.getId();

        fileAttachmentRepository.deleteById(fileAttachmentId);

        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(fileAttachmentId);
        Assertions.assertTrue(fileAttachmentOptional.isEmpty());
    }

    @Test
    public void FileAttachmentRepository_DeleteByRecordId() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        fileAttachmentRepository.save(fileAttachment3);
        fileAttachmentRepository.save(fileAttachment4);
        Long savedRecordId = savedBankRecord.getId();

        fileAttachmentRepository.deleteByItemId(savedRecordId);

        List<FileAttachment> foundFileAttachments = fileAttachmentRepository.findByItemId(savedRecordId);
        Assertions.assertNotNull(foundFileAttachments);
        Assertions.assertTrue(foundFileAttachments.isEmpty());
    }

    @Test
    public void FileAttachmentRepository_DeleteByTransactionId() {
        fileAttachmentRepository.save(fileAttachment1);
        fileAttachmentRepository.save(fileAttachment2);
        fileAttachmentRepository.save(fileAttachment3);
        fileAttachmentRepository.save(fileAttachment4);
        Long savedTransactionId = savedFinancialTransaction.getId();

        fileAttachmentRepository.deleteByItemId(savedTransactionId);

        List<FileAttachment> foundFileAttachments = fileAttachmentRepository.findByItemId(savedTransactionId);
        Assertions.assertNotNull(foundFileAttachments);
        Assertions.assertTrue(foundFileAttachments.isEmpty());
    }
}
