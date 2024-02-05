package com.interview.patchwork.models;

public class BorrowResult {

    private final boolean success;
    private final BookCopy bookCopy;
    private final String message;

    public BorrowResult(boolean success, String message, BookCopy bookCopy) {
        this.success = success;
        this.message = message;
        this.bookCopy = bookCopy;
    }
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BookCopy getCopy() {
        return this.bookCopy;
    }
}
