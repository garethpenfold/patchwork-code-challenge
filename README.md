# Patchwork coding exercise

This is the Patchwork coding challenge. Please see the context and stories section which describes what code challenges we are addressing.

## Getting Started

The application is written in Java 17. To install Java please follow the installation [instructions on the oracle site](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A).

We leverage Apache maven as the software management build tool to facilitate dependency
management and build lifecycle tasks. To install Apache Maven please [follow the official guide.](https://maven.apache.org/install.html)

## Running the Tests

Using the maven package command, maven will install all dependencies, compile the code, run the tests and then package
the code into an executable jar.

```
mvn test
```

## Context and Stories

### Context

I have many books which I would like to share with my community. That sounds like a book-lending
library. Please write some software top help me do that.

### Stories

* As a library user, I would like to be able to find books by my favourite author, so that I know if they are available in the library.
  * *Test cases: `findBooksByAuthor_whenMatchingAuthor_returnsBooks`,`booksByAuthor_whenMatchingAuthor_returnsAuthorsBooks`*
* As a library user, I would like to be able to find books by title so that I know if they are available in the library.
  * *Test cases: `findBooksByTitle_whenMatchingTitle_returnsBooks`,`booksByTitle_whenMatchingTitle_returnsMatchingBooks`*
* As a library user, I would like to be able to find books by ISBN, so that I know if they are available in the library.
  * *Test cases: `findBooksByIsbn_whenMatchingIsbn_returnsBook`,`booksByIsbn_whenMatchingIsbn_returnsMatchingBook`*
* As a library user, I would like to be able to borrow a book, so I can read it at home.
  * *Test cases: `borrowBook_whenBookIsAvailable_returnsSuccessfulBorrowResult`, `checkoutBookCopy_whenCopyIsReturnedLast_returnsTrue`, `checkoutBookCopy_whenCopyHasNeverBeenTaken_returnsTrue`* 
* As a library owner, I would like to know how many books are being borrowed, so I can see how many are outstanding.
  * *Test cases: `numberOfBooksCheckedOut_whenThereAreCheckedOutBooks_returnsCount`,`checkedOutBookCopies_whenSomeBooksCheckedOut_returnsCheckedOutBooks`*
* As a library user, I should be prevented from borrowing reference books so they are always available.
  * *Test cases: `borrowBook_whenBookIsReference_returnsFailToBorrowResult`*

# Where improvements can be made

## Minor

Given more time there are several small items that I would like to spend a bit more time on:

* Addition of code formatting / linting into build
* Code coverage metrics
* Continuous integration - We should build and test the library in a chosen CI tool
* findByIsbn function could return an optional to make it clearer we sometimes expect no matches here.
* setCheckoutState we might want to introduce some form of workflow to ensure that a returned book entry cannot be overridden with a checked out state
* Borrow book functionality is slightly naive (taking first, would need to be revisited when addressing concurrency)
* No interfaces created as we only have single implementations. Would be a small refactor to extract interfaces and rename implementations as appropriate.

## Larger 

Some enhancements we could make going forward to improve the functionality of the system.

* We could introduce BDD testing to have exact tests reflecting the stories.
* The code is not safe for concurrent operations. 
* The search capabilities require exact matching of criteria, this could be expanded to partial matches or fuzzy matching.
* Persistence layer would be required to make this a usable production codebase


