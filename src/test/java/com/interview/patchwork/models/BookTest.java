package com.interview.patchwork.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookTest {

    private Author mockAuthor;
    @BeforeEach
    void setup() {
        mockAuthor = mock(Author.class);
    }
    @Test
    void isValid_withEmptyTitle_returnsFalse() {
        assertThat(new Book(mockAuthor, "", "isbn", true).isValid()).isFalse();
        assertThat(new Book(mockAuthor, null, "isbn", true).isValid()).isFalse();
    }

    @Test
    void isValid_withEmptyIsbn_returnsFalse() {
        assertThat(new Book(mockAuthor, "title", "", true).isValid()).isFalse();
        assertThat(new Book(mockAuthor, "title", null, true).isValid()).isFalse();
    }

    @Test
    void isValid_withNullAuthor_returnsFalse() {
        assertThat(new Book(null, "title", "isbn", true).isValid()).isFalse();
    }

    @Test
    void isValid_withInvalidAuthor_returnsFalse() {
        when(mockAuthor.isValid()).thenReturn(false);
        assertThat(new Book(mockAuthor, "title", "isbn", true).isValid()).isFalse();
    }

    @Test
    void isValid_withAllContent_returnsTrue() {
        when(mockAuthor.isValid()).thenReturn(true);
        assertThat(new Book(mockAuthor, "title", "isbn", true).isValid()).isTrue();
    }
}