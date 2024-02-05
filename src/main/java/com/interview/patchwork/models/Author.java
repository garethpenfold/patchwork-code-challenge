package com.interview.patchwork.models;

import org.apache.commons.lang3.StringUtils;

public record Author(String name) {
    public boolean isValid() {
        return !StringUtils.isEmpty(this.name);
    }
}
