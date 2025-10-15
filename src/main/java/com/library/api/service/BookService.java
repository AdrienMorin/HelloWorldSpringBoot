package com.library.api.service;

import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;

import java.util.List;

/**
 * Service interface defining business operations for book management.
 *
 * This interface establishes the contract for book-related business logic,
 * promoting loose coupling and facilitating testing through mocking.
 */
public interface BookService {

    /**
     * Creates a new book in the library system.
     *
     * @param requestDto the book data to create
     * @return the created book with generated ID and timestamps
     * @throws DuplicateIsbnException if a book with the same ISBN already exists
     */
    BookResponseDto createBook(BookRequestDto requestDto);

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id the book ID
     * @return the book details
     * @throws BookNotFoundException if no book exists with the given ID
     */
    BookResponseDto getBookById(Long id);

    /**
     * Retrieves all books in the library.
     *
     * @return list of all books, empty list if no books exist
     */
    List<BookResponseDto> getAllBooks();

    /**
     * Updates an existing book with new information.
     * Only non-null fields in the request will be updated.
     *
     * @param id the ID of the book to update
     * @param requestDto the updated book data
     * @return the updated book
     * @throws BookNotFoundException if no book exists with the given ID
     * @throws DuplicateIsbnException if the updated ISBN conflicts with another book
     */
    BookResponseDto updateBook(Long id, BookRequestDto requestDto);

    /**
     * Deletes a book from the library system.
     *
     * @param id the ID of the book to delete
     * @throws BookNotFoundException if no book exists with the given ID
     */
    void deleteBook(Long id);

    /**
     * Searches for books by title keyword.
     *
     * @param title the title keyword (case-insensitive)
     * @return list of matching books
     */
    List<BookResponseDto> searchBooksByTitle(String title);

    /**
     * Searches for books by author keyword.
     *
     * @param author the author keyword (case-insensitive)
     * @return list of matching books
     */
    List<BookResponseDto> searchBooksByAuthor(String author);
}
