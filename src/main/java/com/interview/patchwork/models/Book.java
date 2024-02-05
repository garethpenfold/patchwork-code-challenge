package com.interview.patchwork.models;

import org.apache.commons.lang3.StringUtils;

public record Book(Author author, String title, String isbn, boolean referenceBook) {

    public boolean isValid() {
        if (StringUtils.isEmpty(this.title)) {
            return false;
        }
        if (StringUtils.isEmpty(this.isbn)) {
            return false;
        }
        return this.author != null && this.author.isValid();
    }
}
