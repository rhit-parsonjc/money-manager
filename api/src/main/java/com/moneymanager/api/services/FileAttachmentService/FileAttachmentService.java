package com.moneymanager.api.services.FileAttachmentService;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.moneymanager.api.models.FileAttachment;

public interface FileAttachmentService {
    FileAttachment createFileAttachmentForBankRecord(MultipartFile file, Long recordId) throws IOException;
    FileAttachment createFileAttachmentForFinancialTransaction(MultipartFile file, Long transactionId) throws IOException;
    FileAttachment getFileAttachment(Long id);
    List<FileAttachment> getFileAttachmentsByRecordId(Long recordId);
    List<FileAttachment> getFileAttachmentsByTransactionId(Long transactionId);
    void deleteFileAttachment(Long id);
    void deleteFileAttachmentsByRecordId(Long recordId);
    void deleteFileAttachmentsByTransactionId(Long transactionId);
}