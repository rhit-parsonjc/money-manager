package com.moneymanager.api.controllers;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.FileAttachmentService.FileAttachmentService;
import com.moneymanager.api.services.MapperService.MapperService;

@CrossOrigin
@RestController
@RequestMapping("api/v1/fileattachments")
@RequiredArgsConstructor
public class FileAttachmentController {
    private final FileAttachmentService fileAttachmentService;
    private final MapperService mapperService;

    @PostMapping("{itemId}")
    public ResponseEntity<DataOrErrorResponse> createFile(
            @PathVariable Long itemId,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        try {
            FileAttachment fileAttachment = fileAttachmentService.createFileAttachmentForItem(multipartFile, itemId);
            FileAttachmentDto fileAttachmentDto = mapperService.mapFileAttachmentToDto(fileAttachment);
            DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFileAttachment(
            @PathVariable Long id
    ) {
        FileAttachment fileAttachment = fileAttachmentService.getFileAttachment(id);
        FileAttachmentDetailsDto fileAttachmentDetailsDto = mapperService.mapFileAttachmentToDetailsDto(fileAttachment);
        MediaType mediaType = MediaType.parseMediaType(fileAttachmentDetailsDto.getType());
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileAttachmentDetailsDto.getName() + "\"")
                .body(fileAttachmentDetailsDto.getContents());
    }

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getFileAttachmentsForItem(
            @RequestParam Long itemId
    ) {
        List<FileAttachment> fileAttachmentList = fileAttachmentService.getFileAttachmentsByItemId(itemId);
        List<FileAttachmentDto> fileAttachmentDtoList = mapperService.mapFileAttachmentsToDtos(fileAttachmentList);
        DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDtoList);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachment(
            @PathVariable Long id
    ) {
        fileAttachmentService.deleteFileAttachment(id);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachmentsForItem(
            @RequestParam Long itemId
    ) {
        fileAttachmentService.deleteFileAttachmentsByItemId(itemId);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
