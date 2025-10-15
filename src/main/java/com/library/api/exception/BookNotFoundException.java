package com.library.api.exception;

/**
 * Exception thrown when a requested book is not found in the system.
 *
 * This exception is typically thrown when attempting to retrieve, update,
 * or delete a book that doesn't exist in the database.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with a detail message.
     *
     * @param message the detail message
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new BookNotFoundException for a specific book ID.
     *
     * @param id the ID of the book that was not found
     * @return a new BookNotFoundException with an appropriate message
     */
    public static BookNotFoundException forId(Long id) {
        return new BookNotFoundException("Book not found with ID: " + id);
    }

    /**
     * Constructs a new BookNotFoundException for a specific ISBN.
     *
     * @param isbn the ISBN of the book that was not found
     * @return a new BookNotFoundException with an appropriate message
     */
    public static BookNotFoundException forIsbn(String isbn) {
        return new BookNotFoundException("Book not found with ISBN: " + isbn);
    }
}
