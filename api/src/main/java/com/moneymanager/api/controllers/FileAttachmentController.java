package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.DateAmountDto;
import com.moneymanager.api.dtos.FileAttachmentDetailsDto;
import com.moneymanager.api.dtos.FileAttachmentDto;
import com.moneymanager.api.exceptions.ResourceNotFoundException;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.FileAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/fileattachments")
@RequiredArgsConstructor
public class FileAttachmentController {
    private final FileAttachmentService fileAttachmentService;

    @PostMapping("")
    public ResponseEntity<DataOrErrorResponse> createFileAttachment(@RequestParam("file") MultipartFile multipartFile) {
        try {
            FileAttachmentDto fileAttachmentDto = fileAttachmentService.createFileAttachment(multipartFile);
            DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDto);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFileAttachment(@PathVariable Long id) {
        try {
            FileAttachmentDetailsDto fileAttachmentDetailsDto = fileAttachmentService.getFileAttachment(id);
            MediaType mediaType = MediaType.parseMediaType(fileAttachmentDetailsDto.getType());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileAttachmentDetailsDto.getName() + "\"")
                    .body(fileAttachmentDetailsDto.getContents());
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            byte[] emptyArr = new byte[0];
            return new ResponseEntity<byte[]>(emptyArr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getFileAttachments() {
        try {
            List<FileAttachmentDto> fileAttachmentDtoList = fileAttachmentService.getFileAttachments();
            DataOrErrorResponse response = new DataOrErrorResponse(true, fileAttachmentDtoList);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataOrErrorResponse> deleteFileAttachment(@PathVariable Long id) {
        try {
            fileAttachmentService.deleteFileAttachment(id);
            DataOrErrorResponse response = new DataOrErrorResponse(true, null);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
