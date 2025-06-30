package com.moneymanager.api.services.FileAttachmentService;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.models.FileAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileAttachmentService {
    public FileAttachment createFileAttachment(MultipartFile file) throws IOException;
    public FileAttachmentDto attachFileToBankRecord(MultipartFile file, Long recordId) throws IOException;
    public FileAttachmentDto attachFileToFinancialTransaction(MultipartFile file, Long transactionId) throws IOException;
    public FileAttachmentDetailsDto getFileAttachment(Long id);
    public List<FileAttachmentDto> getFileAttachments();
    public List<FileAttachmentDto> getFileAttachmentsByRecordId(Long recordId);
    public List<FileAttachmentDto> getFileAttachmentsByTransactionId(Long transactionId);
    public void deleteFileAttachment(Long id);
    public void deleteFileAttachments();
}