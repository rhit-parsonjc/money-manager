package com.moneymanager.api.services;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface FileAttachmentService {
    public FileAttachmentDto createFileAttachment(MultipartFile file) throws IOException, SQLException;
    public FileAttachmentDetailsDto getFileAttachment(Long id) throws SQLException;
    public List<FileAttachmentDto> getFileAttachments();
    public void deleteFileAttachment(Long id);
    public void attachBankRecord(Long fileId, Long recordId);
    public void detachBankRecord(Long fileId, Long recordId);
    public void attachFinancialTransaction(Long fileId, Long transactionId);
    public void detachFinancialTransaction(Long fileId, Long transactionId);
}
