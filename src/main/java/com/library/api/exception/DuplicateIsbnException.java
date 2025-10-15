package com.library.api.exception;

/**
 * Exception thrown when attempting to create or update a book with an ISBN
 * that already exists in the system.
 *
 * This ensures ISBN uniqueness across the library system.
 */
public class DuplicateIsbnException extends RuntimeException {

    /**
     * Constructs a new DuplicateIsbnException with a detail message.
     *
     * @param message the detail message
     */
    public DuplicateIsbnException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateIsbnException for a specific ISBN.
     *
     * @param isbn the duplicate ISBN
     * @return a new DuplicateIsbnException with an appropriate message
     */
    public static DuplicateIsbnException forIsbn(String isbn) {
        return new DuplicateIsbnException("A book with ISBN '" + isbn + "' already exists");
    }
}
