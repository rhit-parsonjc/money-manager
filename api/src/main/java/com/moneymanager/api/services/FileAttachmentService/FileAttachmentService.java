package com.moneymanager.api.services.FileAttachmentService;

import com.moneymanager.api.models.FileAttachment;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileAttachmentService {
    FileAttachment createFileAttachmentForItem(MultipartFile file, Long itemId) throws IOException;
    FileAttachment getFileAttachment(Long id);
    List<FileAttachment> getFileAttachmentsByItemId(Long itemId);
    void deleteFileAttachment(Long id);
    void deleteFileAttachmentsByItemId(Long itemId);
}