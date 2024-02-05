package com.interview.patchwork.services;

import com.interview.patchwork.models.BookCopy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OwnerLibraryServiceTest {

    private LibraryCatalogueService libraryCatalogueServiceMock;
    private CheckoutService checkoutServiceMock;
    private OwnerLibraryService underTest;

    @BeforeEach
    public void setup() {
        libraryCatalogueServiceMock = mock(LibraryCatalogueService.class);
        checkoutServiceMock = mock(CheckoutService.class);

        underTest = new OwnerLibraryService(libraryCatalogueServiceMock, checkoutServiceMock);
    }

    @Test
    void numberOfBooksCheckedOut_whenThereAreCheckedOutBooks_returnsCount() {
        BookCopy mockBook = mock(BookCopy.class);
        BookCopy mockBook1 = mock(BookCopy.class);
        when(checkoutServiceMock.checkedOutBookCopies()).thenReturn(List.of(mockBook1, mockBook));

        int response = underTest.numberOfBooksCheckedOut();

        assertThat(response).isEqualTo(2);
    }

    @Test
    void addBookCopy_whenAddingBooks_returnsLibraryServiceBoolean() {
        BookCopy mockBook = mock(BookCopy.class);
        boolean libraryServiceResponse = true;
        when(libraryCatalogueServiceMock.addBook(mockBook)).thenReturn(libraryServiceResponse);

        boolean response = underTest.addBookCopy(mockBook);

        assertThat(response).isEqualTo(libraryServiceResponse);
    }
}