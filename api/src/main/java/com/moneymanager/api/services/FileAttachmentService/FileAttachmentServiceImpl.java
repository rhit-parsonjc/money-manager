package com.moneymanager.api.services.FileAttachmentService;

import com.moneymanager.api.exceptions.PermissionsException;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.models.*;
import com.moneymanager.api.repositories.FileAttachmentRepository;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

import com.moneymanager.api.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileAttachmentServiceImpl implements FileAttachmentService {
    private final FileAttachmentRepository fileAttachmentRepository;
    private final ItemRepository itemRepository;
    private final UserEntityService userEntityService;

    private Item getItem(UserEntity userEntity, Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.ITEM_MESSAGE);
        Item item = itemOptional.get();
        if (item.getAccount().getUserEntity().getId().longValue() != userEntity.getId())
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return item;
    }

    private FileAttachment getFileAttachment(UserEntity userEntity, Long attachmentId) {
        Optional<FileAttachment> fileAttachmentOptional = fileAttachmentRepository.findById(attachmentId);
        if (fileAttachmentOptional.isEmpty())
            throw new ResourceNotFoundException(ResourceNotFoundException.FILE_ATTACHMENT_MESSAGE);
        FileAttachment fileAttachment = fileAttachmentOptional.get();
        Item item = fileAttachment.getItem();
        Account account = item.getAccount();
        if (account.getUserEntity().getId().longValue() != userEntity.getId())
            throw new PermissionsException(PermissionsException.INCORRECT_USER);
        return fileAttachment;
    }

    @Override
    public FileAttachment createFileAttachmentForItem(MultipartFile file, Long itemId) throws IOException {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        Item item = getItem(userEntity, itemId);
        FileAttachment fileAttachment = new FileAttachment(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes(),
                item
        );
        return fileAttachmentRepository.save(fileAttachment);
    }

    @Override
    public FileAttachment getFileAttachment(Long id) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        return getFileAttachment(userEntity, id);
    }

    @Override
    public List<FileAttachment> getFileAttachmentsByItemId(Long itemId) {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        getItem(userEntity, itemId);
        return fileAttachmentRepository.findByItemId(itemId);
    }

    @Override
    public void deleteFileAttachment(Long id) {
        getFileAttachment(id);
        fileAttachmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFileAttachmentsByItemId(Long itemId) {
        getFileAttachmentsByItemId(itemId);
        fileAttachmentRepository.deleteByItemId(itemId);
    }
}
