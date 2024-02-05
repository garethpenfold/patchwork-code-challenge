package com.interview.patchwork.services;

import com.interview.patchwork.models.Author;
import com.interview.patchwork.models.Book;
import com.interview.patchwork.models.BookCopy;
import com.interview.patchwork.models.BorrowResult;
import com.interview.patchwork.models.User;

import java.util.List;

public class PublicLibraryService {
    private static final String UNAVAILABLE_BOOK_ERROR_MESSAGE = "This book is unavailable.";
    private static final String REFERENCE_BOOK_ERROR_MESSAGE = "Reference books cannot be borrowed.";
    private static final String UNKNOWN_ERROR_MESSAGE = "Unknown error has occurred, unable to borrow.";
    private final LibraryCatalogueService library;
    private final CheckoutService checkoutService;

    public PublicLibraryService(LibraryCatalogueService library, CheckoutService checkoutService) {
        this.library = library;
        this.checkoutService = checkoutService;
    }

    public List<Book> findBooksByTitle(String title) {
        return library.booksByTitle(title);
    }

    public List<Book> findBooksByAuthor(Author author) {
        return library.booksByAuthor(author);
    }

    public Book findBooksByISBN(String isbn) {
        return library.bookByISBN(isbn);
    }

    public BorrowResult borrowBook(Book book, User user) {
        if (book.referenceBook()) {
            return failingResult(REFERENCE_BOOK_ERROR_MESSAGE);
        }

        List<BookCopy> copies = library.copiesOfBook(book);
        List<BookCopy> availableCopies = checkoutService.availableCopies(copies);
        if (availableCopies.isEmpty()) {
            return failingResult(UNAVAILABLE_BOOK_ERROR_MESSAGE);
        }

        BookCopy targetCopy = availableCopies.get(0);
        boolean result = checkoutService.checkoutBookCopy(targetCopy, user);
        if (result) {
            return new BorrowResult(true, null, targetCopy);
        } else {
            return failingResult(UNKNOWN_ERROR_MESSAGE);
        }
    }

    public boolean returnBook(BookCopy bookCopy, User user) {
        return checkoutService.returnBookCopy(bookCopy, user);
    }

    private BorrowResult failingResult(String failureMessage) {
        return new BorrowResult(false, failureMessage, null);
    }
}
