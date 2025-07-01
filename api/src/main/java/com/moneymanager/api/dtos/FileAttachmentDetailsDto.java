package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FileAttachmentDetailsDto contains the properties and contents of a file
 */

@Data
@AllArgsConstructor
public class FileAttachmentDetailsDto {
    private Long id;
    private String name;
    private String type;
    private Long size;
    private byte[] contents;
}
