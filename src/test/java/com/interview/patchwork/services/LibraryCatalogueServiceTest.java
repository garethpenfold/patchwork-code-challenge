package com.interview.patchwork.services;

import com.interview.patchwork.models.Author;
import com.interview.patchwork.models.Book;
import com.interview.patchwork.models.BookCopy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryCatalogueServiceTest {

    private LibraryCatalogueService underTest;

    @BeforeEach
    public void setup() {
        underTest = new LibraryCatalogueService();
    }


    @Test
    void addBook_whenBookIsValid_returnsTrue() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());

        boolean response = underTest.addBook(bookCopy);

        assertThat(response).isTrue();
    }

    @Test
    void addBook_whenBookIsAlreadyInCatalogue_returnsFalse() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);

        boolean response = underTest.addBook(bookCopy);

        assertThat(response).isFalse();
    }

    @Test
    void addBook_whenBookIsInvalid_returnsFalse() {
        Book mockBook = mock(Book.class);
        when(mockBook.isValid()).thenReturn(false);
        BookCopy bookCopy = new BookCopy(mockBook, UUID.randomUUID());

        boolean response = underTest.addBook(bookCopy);

        assertThat(response).isFalse();
    }

    @Test
    void copiesOfBook_whenThereAreNoCopies_returnsNoCopies() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);

        Book book2 = new Book(author, "title2", "isbn2", false);
        List<BookCopy> response = underTest.copiesOfBook(book2);

        assertThat(response).isEmpty();
    }

    @Test
    void copiesOfBook_whenThereAreCopies_returnsCopies() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        Book book2 = new Book(author, "title2", "isbn2", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        BookCopy bookCopy2 = new BookCopy(book, UUID.randomUUID());
        BookCopy book2Copy = new BookCopy(book2, UUID.randomUUID());
        underTest.addBook(bookCopy);
        underTest.addBook(book2Copy);
        underTest.addBook(bookCopy2);

        List<BookCopy> response = underTest.copiesOfBook(book);

        assertThat(response).containsExactlyInAnyOrder(bookCopy, bookCopy2);
    }

    @Test
    void booksByTitle_whenNoMatchingTitle_returnsEmptyList() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);

        List<Book> response = underTest.booksByTitle("abc");

        assertThat(response).isEmpty();
    }

    @Test
    void booksByTitle_whenMatchingTitle_returnsMatchingBooks() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        Book book2 = new Book(author, "title2", "isbn2", false);
        BookCopy book2Copy = new BookCopy(book2, UUID.randomUUID());
        Book book3 = new Book(author, "title", "isbn3", false);
        BookCopy book3Copy = new BookCopy(book3, UUID.randomUUID());
        underTest.addBook(bookCopy);
        underTest.addBook(book2Copy);
        underTest.addBook(book3Copy);

        List<Book> response = underTest.booksByTitle("title");

        assertThat(response).containsExactlyInAnyOrder(book, book3);
    }

    @Test
    void booksByAuthor_whenNoMatchingAuthor_returnsEmptyList() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);

        List<Book> response = underTest.booksByAuthor(new Author("another author"));

        assertThat(response).isEmpty();
    }

    @Test
    void booksByAuthor_whenMatchingAuthor_returnsAuthorsBooks() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);
        Book book2 = new Book(author, "title", "isbn", false);
        BookCopy book2Copy = new BookCopy(book2, UUID.randomUUID());
        underTest.addBook(book2Copy);
        Author author2 = new Author("another author");
        Book book3 = new Book(author2, "title", "isbn2", false);
        BookCopy book3Copy = new BookCopy(book3, UUID.randomUUID());
        underTest.addBook(book3Copy);

        List<Book> response = underTest.booksByAuthor(new Author("Test"));

        assertThat(response).containsExactlyInAnyOrder(book, book2);
    }

    @Test
    void booksByIsbn_whenNoMatchingIsbn_returnsNull() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        underTest.addBook(bookCopy);

        Book response = underTest.bookByISBN("isbn2");

        assertThat(response).isNull();
    }

    @Test
    void booksByIsbn_whenMatchingIsbn_returnsMatchingBook() {
        Author author = new Author("Test");
        Book book = new Book(author, "title", "isbn", false);
        BookCopy bookCopy = new BookCopy(book, UUID.randomUUID());
        Book book2 = new Book(author, "title2", "isbn2", false);
        BookCopy book2Copy = new BookCopy(book2, UUID.randomUUID());
        Book book3 = new Book(author, "title", "isbn3", false);
        BookCopy book3Copy = new BookCopy(book3, UUID.randomUUID());
        underTest.addBook(bookCopy);
        underTest.addBook(book2Copy);
        underTest.addBook(book3Copy);

        Book response = underTest.bookByISBN("isbn3");

        assertThat(response).isEqualTo(book3);
    }
}