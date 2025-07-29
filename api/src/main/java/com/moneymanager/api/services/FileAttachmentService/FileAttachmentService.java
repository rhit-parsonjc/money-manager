package com.moneymanager.api.services.FileAttachmentService;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.moneymanager.api.models.FileAttachment;

public interface FileAttachmentService {
    FileAttachment createFileAttachmentForItem(MultipartFile file, Long itemId) throws IOException;
    FileAttachment getFileAttachment(Long id);
    List<FileAttachment> getFileAttachmentsByItemId(Long itemId);
    void deleteFileAttachment(Long id);
    void deleteFileAttachmentsByItemId(Long itemId);
}