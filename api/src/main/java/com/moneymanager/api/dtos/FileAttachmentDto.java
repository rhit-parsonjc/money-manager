package com.moneymanager.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FileAttachmentDto is returned in place of FileAttachment
 */

@Data
@AllArgsConstructor
public class FileAttachmentDto {
    private Long id;
    private String name;
    private String type;
    private Long size;
}
