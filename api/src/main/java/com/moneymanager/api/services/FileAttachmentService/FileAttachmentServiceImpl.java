package com.moneymanager.api.services.FileAttachmentService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moneymanager.api.exceptions.InvalidStateException;
import com.moneymanager.api.exceptions.PermissionsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.repositories.BankRecordRepository;
import com.moneymanager.api.repositories.FileAttachmentRepository;
import com.moneymanager.api.repositories.FinancialTransactionRepository;
import com.moneymanager.api.services.MapperService.MapperService;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@Service
@RequiredArgsConstructor
public class FileAttachmentServiceImpl implements FileAttachmentService {
    private final FileAttachmentRepository fileAttachmentRepository;
    private final BankRecordRepository bankRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final MapperService mapperService;
    private final UserEntityService userEntityService;

    private BankRecord getBankRecord(UserEntity userEntity, Long recordId) {
        Optional<BankRecord> bankRecordOptional = bankRecordRepository.findById(recordId);
        if (bankRecordOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.BANK_RECORD_MESSAGE);
        BankRecord bankRecord = bankRecordOptional.get();
        if (!Objects.equals(bankRecord.getAccount().getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return bankRecord;
    }

    private FinancialTransaction getFinancialTransaction(UserEntity userEntity, Long transactionId) {
        Optional<FinancialTransaction> financialTransactionOptional = financialTransactionRepository.findById(transactionId);
        if (financialTransactionOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FINANCIAL_TRANSACTION_MESSAGE);
        FinancialTransaction financialTransaction = financialTransactionOptional.get();
        if (!Objects.equals(financialTransaction.getAccount().getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return financialTransaction;
    }

    private FileAttachment getFileAttachment(UserEntity userEntity, Long attachmentId) {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(attachmentId);
        if (fileAttachmentOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FILE_ATTACHMENT_MESSAGE);
        FileAttachment fileAttachment = fileAttachmentOptional.get();
        BankRecord bankRecord = fileAttachment.getBankRecord();
        FinancialTransaction financialTransaction = fileAttachment.getFinancialTransaction();
        Account account;
        if (bankRecord == null && financialTransaction == null)
            throw new InvalidStateException(InvalidStateException.FILE_NO_LINKS);
        else if (bankRecord != null && financialTransaction != null)
            throw new InvalidStateException(InvalidStateException.FILE_TWO_LINKS);
        else if (bankRecord != null)
            account = bankRecord.getAccount();
        else
            account = financialTransaction.getAccount();
        if (!Objects.equals(account.getUserEntity().getId(), userEntity.getId()))
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return fileAttachment;
    }

    @Override
    public FileAttachment createFileAttachmentForBankRecord(MultipartFile file, Long recordId) throws IOException {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        FileAttachment fileAttachment = new FileAttachment(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes(),
                bankRecord
        );
        return fileAttachmentRepository.save(fileAttachment);
    }

    @Override
    public FileAttachment createFileAttachmentForFinancialTransaction(MultipartFile file, Long transactionId) throws IOException {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        FileAttachment fileAttachment = new FileAttachment(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes(),
                financialTransaction
        );
        return fileAttachmentRepository.save(fileAttachment);
    }

    @Override
    public FileAttachment getFileAttachment(Long id) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        return getFileAttachment(userEntity, id);
    }

    @Override
    public List<FileAttachment> getFileAttachmentsByRecordId(Long recordId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        BankRecord bankRecord = getBankRecord(userEntity, recordId);
        return bankRecord.getFileAttachments().stream().toList();
    }

    @Override
    public List<FileAttachment> getFileAttachmentsByTransactionId(Long transactionId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        FinancialTransaction financialTransaction = getFinancialTransaction(userEntity, transactionId);
        return financialTransaction.getFileAttachments().stream().toList();
    }

    @Override
    public void deleteFileAttachment(Long id) {
        getFileAttachment(id);
        fileAttachmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFileAttachmentsByRecordId(Long recordId) {
        getFileAttachmentsByRecordId(recordId);
        fileAttachmentRepository.deleteByBankRecordId(recordId);
    }

    @Override
    @Transactional
    public void deleteFileAttachmentsByTransactionId(Long transactionId) {
        getFileAttachmentsByTransactionId(transactionId);
        fileAttachmentRepository.deleteByFinancialTransactionId(transactionId);
    }
}
