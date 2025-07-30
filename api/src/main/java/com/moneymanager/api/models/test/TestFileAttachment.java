package com.moneymanager.api.models.test;

import com.moneymanager.api.models.FileAttachment;
import com.moneymanager.api.models.Item;

public class TestFileAttachment extends FileAttachment {
    public TestFileAttachment(Long id, String name, String type, Long size,
                              byte[] contents, Item item) {
        super(name, type, size, contents, item);
        this.id = id;
    }
}
