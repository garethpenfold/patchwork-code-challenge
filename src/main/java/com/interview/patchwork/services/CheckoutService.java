package com.interview.patchwork.services;

import com.interview.patchwork.models.BookCopy;
import com.interview.patchwork.models.Checkout;
import com.interview.patchwork.models.CheckoutState;
import com.interview.patchwork.models.User;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutService {
    private final Map<BookCopy, Deque<Checkout>> bookUUIDtoCheckoutMap = new HashMap<>();

    public List<BookCopy> checkedOutBookCopies() {
        return bookUUIDtoCheckoutMap.values().stream().filter(this::isCheckedOut).map(dq -> dq.peekLast().getBookCopy()).collect(Collectors.toList());
    }

    public List<BookCopy> availableCopies(List<BookCopy> bookCopies) {
        return bookCopies.stream().filter(this::bookCopyIsAvailable).collect(Collectors.toList());
    }

    public boolean checkoutBookCopy(BookCopy bookCopy, User user) {
        if (!bookCopyIsAvailable(bookCopy)) {
            return false;
        }

        Checkout newCheckout = new Checkout(bookCopy, user);

        if (bookUUIDtoCheckoutMap.containsKey(bookCopy)) {
            bookUUIDtoCheckoutMap.get(bookCopy).addLast(newCheckout);
        } else {
            Deque<Checkout> checkouts = new LinkedList<>();
            checkouts.addLast(newCheckout);
            bookUUIDtoCheckoutMap.put(bookCopy, checkouts);
        }

        return true;
    }


    public boolean returnBookCopy(BookCopy bookCopy, User user) {
        if (!bookUUIDtoCheckoutMap.containsKey(bookCopy)) {
            return false;
        }

        Checkout lastCheckout = bookUUIDtoCheckoutMap.get(bookCopy).peekLast();

        if (lastCheckout == null) {
            return false;
        }

        if (!lastCheckout.getUser().equals(user) || !lastCheckout.getCheckoutState().equals(CheckoutState.CHECKED_OUT)) {
            return false;
        }

        lastCheckout.setCheckoutState(CheckoutState.RETURNED);

        return true;
    }

    private boolean bookCopyIsAvailable(BookCopy bookCopy) {
        if (!bookUUIDtoCheckoutMap.containsKey(bookCopy)) {
            return true;
        }

        return CheckoutState.RETURNED.equals(bookUUIDtoCheckoutMap.get(bookCopy).peekLast().getCheckoutState());
    }

    private boolean isCheckedOut(Deque<Checkout> checkouts) {
        if (checkouts == null || checkouts.isEmpty()) {
            return false;
        }

        return checkouts.peekLast().getCheckoutState().equals(CheckoutState.CHECKED_OUT);
    }
}
