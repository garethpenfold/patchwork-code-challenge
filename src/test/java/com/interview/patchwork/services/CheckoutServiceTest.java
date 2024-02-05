package com.interview.patchwork.services;

import com.interview.patchwork.models.Book;
import com.interview.patchwork.models.BookCopy;
import com.interview.patchwork.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CheckoutServiceTest {

    private CheckoutService underTest;

    @BeforeEach
    public void setup() {
        underTest = new CheckoutService();
    }

    @Test
    void checkedOutBookCopies_whenNoBooksCheckedOut_returnsEmptyList() {
        List<BookCopy> response = underTest.checkedOutBookCopies();

        assertThat(response).isEmpty();
    }

    @Test
    void checkedOutBookCopies_whenSomeBooksCheckedOut_returnsCheckedOutBooks() {
        BookCopy book1 = createBookCopy();
        BookCopy book2 = createBookCopy();
        BookCopy book3 = createBookCopy();
        User user = createUser();
        underTest.checkoutBookCopy(book1, user);
        underTest.checkoutBookCopy(book2, user);
        underTest.checkoutBookCopy(book3, user);
        underTest.returnBookCopy(book3, user);

        List<BookCopy> response = underTest.checkedOutBookCopies();

        assertThat(response).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void availableCopies_whenNoCopiesAlreadyTaken_returnsAllCopies() {
        List<BookCopy> copies = List.of(createBookCopy(), createBookCopy(), createBookCopy());
        List<BookCopy> response = underTest.availableCopies(copies);

        assertThat(response).containsExactlyInAnyOrderElementsOf(copies);
    }

    @Test
    void availableCopies_whenCopyIsTaken_returnsAllButTakenCopies() {
        BookCopy book1 = createBookCopy();
        BookCopy book2 = createBookCopy();
        BookCopy book3 = createBookCopy();
        User user = createUser();
        underTest.checkoutBookCopy(book1, user);

        List<BookCopy> copies = List.of(book1, book2, book3);
        List<BookCopy> response = underTest.availableCopies(copies);

        assertThat(response).containsExactlyInAnyOrder(book2, book3);
    }

    @Test
    void availableCopies_whenCopyIsReturned_returnsAllCopies() {
        BookCopy book1 = createBookCopy();
        BookCopy book2 = createBookCopy();
        BookCopy book3 = createBookCopy();
        User user = createUser();
        underTest.checkoutBookCopy(book1, user);
        underTest.returnBookCopy(book1, user);

        List<BookCopy> copies = List.of(book1, book2, book3);
        List<BookCopy> response = underTest.availableCopies(copies);

        assertThat(response).containsExactlyInAnyOrder(book1, book2, book3);
    }

    @Test
    void checkoutBookCopy_whenCopyHasNeverBeenTaken_returnsTrue() {
        BookCopy book = createBookCopy();
        User user = createUser();

        boolean response = underTest.checkoutBookCopy(book, user);

        assertThat(response).isTrue();
    }

    @Test
    void checkoutBookCopy_whenCopyIsCheckedOutLast_returnsFalse() {
        BookCopy book = createBookCopy();
        User user = createUser();

        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);
        underTest.checkoutBookCopy(book, user);

        boolean response = underTest.checkoutBookCopy(book, user);

        assertThat(response).isFalse();
    }

    @Test
    void checkoutBookCopy_whenCopyIsReturnedLast_returnsTrue() {
        BookCopy book = createBookCopy();
        User user = createUser();

        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);
        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);

        boolean response = underTest.checkoutBookCopy(book, user);

        assertThat(response).isTrue();
    }

    @Test
    void returnBookCopy_whenCopyHasNeverBeenTaken_returnsFalse() {
        BookCopy book = createBookCopy();
        User user = createUser();

        boolean response = underTest.returnBookCopy(book, user);

        assertThat(response).isFalse();
    }

    @Test
    void returnBookCopy_whenCopyWasTakenByDifferentUser_returnsFalse() {
        BookCopy book = createBookCopy();
        User user = createUser();
        User user2 = createUser();
        underTest.checkoutBookCopy(book, user2);

        boolean response = underTest.returnBookCopy(book, user);

        assertThat(response).isFalse();
    }

    @Test
    void returnBookCopy_whenCopyWasLastReturned_returnsFalse() {
        BookCopy book = createBookCopy();
        User user = createUser();
        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);
        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);

        boolean response = underTest.returnBookCopy(book, user);

        assertThat(response).isFalse();
    }

    @Test
    void returnBookCopy_whenCopyWasLastCheckoutOut_returnsTure() {
        BookCopy book = createBookCopy();
        User user = createUser();
        underTest.checkoutBookCopy(book, user);
        underTest.returnBookCopy(book, user);
        underTest.checkoutBookCopy(book, user);

        boolean response = underTest.returnBookCopy(book, user);

        assertThat(response).isTrue();
    }

    private BookCopy createBookCopy() {
        Book mockBook = mock(Book.class);
        return new BookCopy(mockBook, UUID.randomUUID());
    }

    private User createUser() {
        return new User(UUID.randomUUID());
    }
}