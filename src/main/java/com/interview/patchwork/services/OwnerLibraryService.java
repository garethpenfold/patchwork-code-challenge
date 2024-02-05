package com.interview.patchwork.services;

import com.interview.patchwork.models.BookCopy;

public class OwnerLibraryService {
    private final CheckoutService checkoutService;
    private final LibraryCatalogueService library;

    public OwnerLibraryService(LibraryCatalogueService library, CheckoutService checkoutService) {
        this.library = library;
        this.checkoutService = checkoutService;
    }

    public int numberOfBooksCheckedOut() {
        return checkoutService.checkedOutBookCopies().size();
    }

    public boolean addBookCopy(BookCopy bookCopy) {
        return library.addBook(bookCopy);
    }

}
