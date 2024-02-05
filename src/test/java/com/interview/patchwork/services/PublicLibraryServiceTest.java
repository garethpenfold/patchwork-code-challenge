package com.interview.patchwork.services;

import com.interview.patchwork.models.Author;
import com.interview.patchwork.models.Book;
import com.interview.patchwork.models.BookCopy;
import com.interview.patchwork.models.BorrowResult;
import com.interview.patchwork.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicLibraryServiceTest {
    private LibraryCatalogueService libraryCatalogueServiceMock;
    private CheckoutService checkoutServiceMock;
    private PublicLibraryService underTest;

    @BeforeEach
    public void setup() {
        libraryCatalogueServiceMock = mock(LibraryCatalogueService.class);
        checkoutServiceMock = mock(CheckoutService.class);

        underTest = new PublicLibraryService(libraryCatalogueServiceMock, checkoutServiceMock);
    }


    @Test
    void findBooksByTitle_whenMatchingTitles_returnsBooks() {
        String input = "input";
        Book mockBook1 = mock(Book.class);
        Book mockBook2 = mock(Book.class);
        when(libraryCatalogueServiceMock.booksByTitle(input)).thenReturn(List.of(mockBook1, mockBook2));

        List<Book> response = underTest.findBooksByTitle(input);

        assertThat(response).containsExactly(mockBook1, mockBook2);
    }

    @Test
    void findBooksByAuthor_whenMatchingAuthor_returnsBooks() {
        Author mockInput = mock(Author.class);
        Book mockBook1 = mock(Book.class);
        Book mockBook2 = mock(Book.class);
        when(libraryCatalogueServiceMock.booksByAuthor(mockInput)).thenReturn(List.of(mockBook1, mockBook2));

        List<Book> response = underTest.findBooksByAuthor(mockInput);

        assertThat(response).containsExactly(mockBook1, mockBook2);
    }

    @Test
    void findBooksByIsbn_whenMatchingIsbn_returnsBook() {
        String input = "input";
        Book mockBook1 = mock(Book.class);
        when(libraryCatalogueServiceMock.bookByISBN(input)).thenReturn(mockBook1);

        Book response = underTest.findBooksByISBN(input);

        assertThat(response).isEqualTo(mockBook1);
    }

    @Test
    void borrowBook_whenBookIsReference_returnsFailToBorrowResult() {
        Book book = new Book(new Author("name"), "title", "isbn", true);
        User mockUser = mock(User.class);

        BorrowResult response = underTest.borrowBook(book, mockUser);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Reference books cannot be borrowed.");
    }

    @Test
    void borrowBook_whenBookHasNoAvailableCopies_returnsFailToBorrowResult() {
        Book book = new Book(new Author("name"), "title", "isbn", false);
        User mockUser = mock(User.class);

        BookCopy mockCopy1 = mock(BookCopy.class);
        BookCopy mockCopy2 = mock(BookCopy.class);

        when(libraryCatalogueServiceMock.copiesOfBook(book)).thenReturn(List.of(mockCopy1, mockCopy2));
        when(checkoutServiceMock.availableCopies(List.of(mockCopy1, mockCopy2))).thenReturn(emptyList());

        BorrowResult response = underTest.borrowBook(book, mockUser);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("This book is unavailable.");
    }

    @Test
    void borrowBook_whenCheckoutServiceFailsToCheckoutBook_returnsFailToBorrowResult() {
        Book book = new Book(new Author("name"), "title", "isbn", false);
        User mockUser = mock(User.class);

        BookCopy mockCopy1 = mock(BookCopy.class);
        BookCopy mockCopy2 = mock(BookCopy.class);

        when(libraryCatalogueServiceMock.copiesOfBook(book)).thenReturn(List.of(mockCopy1, mockCopy2));
        when(checkoutServiceMock.availableCopies(List.of(mockCopy1, mockCopy2))).thenReturn(List.of(mockCopy1));
        when(checkoutServiceMock.checkoutBookCopy(mockCopy1, mockUser)).thenReturn(false);

        BorrowResult response = underTest.borrowBook(book, mockUser);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Unknown error has occurred, unable to borrow.");
    }

    @Test
    void borrowBook_whenBookIsAvailable_returnsSuccessfulBorrowResult() {
        Book book = new Book(new Author("name"), "title", "isbn", false);
        User mockUser = mock(User.class);

        BookCopy mockCopy1 = mock(BookCopy.class);
        BookCopy mockCopy2 = mock(BookCopy.class);

        when(libraryCatalogueServiceMock.copiesOfBook(book)).thenReturn(List.of(mockCopy1, mockCopy2));
        when(checkoutServiceMock.availableCopies(List.of(mockCopy1, mockCopy2))).thenReturn(List.of(mockCopy1, mockCopy2));
        when(checkoutServiceMock.checkoutBookCopy(mockCopy1, mockUser)).thenReturn(true);

        BorrowResult response = underTest.borrowBook(book, mockUser);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCopy()).isEqualTo(mockCopy1);
    }

    @Test
    void returnBook_whenReturningBook_returnsCheckoutServiceBoolean() {
        BookCopy mockBook = mock(BookCopy.class);
        User mockUser = mock(User.class);
        boolean checkoutServiceResponse = true;
        when(checkoutServiceMock.returnBookCopy(mockBook, mockUser)).thenReturn(checkoutServiceResponse);

        boolean response = underTest.returnBook(mockBook, mockUser);

        assertThat(response).isEqualTo(checkoutServiceResponse);
    }
}