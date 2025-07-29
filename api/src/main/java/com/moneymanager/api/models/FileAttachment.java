package com.moneymanager.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
A file attachment represents a file, containing the metadata and contents.
*/

@Getter
@Entity
@Table(name="ATTACHMENTS")
@NoArgsConstructor
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Long size;

    @Lob
    private byte[] contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public FileAttachment(String name, String type, Long size, byte[] contents, Item item) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = contents;
        this.item = item;
    }
}
