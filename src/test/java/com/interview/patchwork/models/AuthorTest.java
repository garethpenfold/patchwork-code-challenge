package com.interview.patchwork.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest {
    @Test
    void isValid_withEmptyName_returnsFalse() {
        assertThat(new Author("").isValid()).isFalse();
        assertThat(new Author(null).isValid()).isFalse();
    }

    @Test
    void isValid_withContentInName_returnsTrue() {
        assertThat(new Author(" ").isValid()).isTrue();
        assertThat(new Author("abc").isValid()).isTrue();
    }
}