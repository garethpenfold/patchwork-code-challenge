package com.interview.patchwork.models;

import java.util.UUID;

public class Checkout {
    private final BookCopy bookCopy;

    private final User user;

    private CheckoutState checkoutState;

    public Checkout(BookCopy bookCopy, User user) {
        this.bookCopy = bookCopy;
        this.user = user;
        this.checkoutState = CheckoutState.CHECKED_OUT;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public User getUser() {
        return user;
    }

    public CheckoutState getCheckoutState() {
        return checkoutState;
    }

    public void setCheckoutState(CheckoutState checkoutState) {
        this.checkoutState = checkoutState;
    }

}
