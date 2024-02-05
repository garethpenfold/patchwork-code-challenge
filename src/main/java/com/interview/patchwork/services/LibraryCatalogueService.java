package com.interview.patchwork.services;

import com.interview.patchwork.models.Author;
import com.interview.patchwork.models.Book;
import com.interview.patchwork.models.BookCopy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryCatalogueService {
    private final Map<String, List<Book>> titleBookMap = new HashMap<>();
    private final Map<String, Book> isbnBookMap = new HashMap<>();
    private final Map<Author, List<Book>> authorBookMap = new HashMap<>();
    private final Map<Book, List<BookCopy>> copies = new HashMap<>();

    public boolean addBook(BookCopy bookCopy) {
        if (!bookCopy.book().isValid()) {
            return false;
        }

        if (copies.getOrDefault(bookCopy.book(), new ArrayList<>()).contains(bookCopy)) {
            return false;
        }

        addBookToCatalogue(bookCopy.book());

        addBookCopy(bookCopy);

        return true;
    }

    public List<BookCopy> copiesOfBook(Book book) {
        List<BookCopy> bookCopies = copies.getOrDefault(book, new ArrayList<>());
        return Collections.unmodifiableList(bookCopies);
    }

    public List<Book> booksByTitle(String title) {
        return this.titleBookMap.getOrDefault(title, new ArrayList<>());
    }

    public List<Book> booksByAuthor(Author author) {
        return this.authorBookMap.getOrDefault(author, new ArrayList<>());
    }

    public Book bookByISBN(String isbn) {
        return this.isbnBookMap.getOrDefault(isbn, null);
    }

    private void addBookCopy(BookCopy bookCopy) {
        if (copies.containsKey(bookCopy.book())) {
            copies.get(bookCopy.book()).add(bookCopy);
        } else {
            List<BookCopy> books = new ArrayList<>();
            books.add(bookCopy);
            copies.put(bookCopy.book(), books);
        }
    }

    private void addBookToCatalogue(Book book) {
        if (titleBookMap.containsKey(book.title())) {
            titleBookMap.get(book.title()).add(book);
        } else {
            List<Book> books = new ArrayList<>();
            books.add(book);
            titleBookMap.put(book.title(), books);
        }

        isbnBookMap.put(book.isbn(), book);

        if (authorBookMap.containsKey(book.author())) {
            authorBookMap.get(book.author()).add(book);
        } else {
            List<Book> books = new ArrayList<>();
            books.add(book);
            authorBookMap.put(book.author(), books);
        }
    }

}
