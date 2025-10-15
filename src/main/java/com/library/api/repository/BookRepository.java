package com.library.api.repository;

import com.library.api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity persistence operations.
 *
 * Extends JpaRepository to provide standard CRUD operations and
 * includes custom query methods for specific business requirements.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Checks if a book with the given ISBN exists in the database.
     *
     * @param isbn the ISBN to check
     * @return true if a book with the ISBN exists, false otherwise
     */
    boolean existsByIsbn(String isbn);

    /**
     * Checks if a book with the given ISBN exists, excluding a specific book ID.
     * Useful for update operations to allow keeping the same ISBN.
     *
     * @param isbn the ISBN to check
     * @param id the book ID to exclude from the check
     * @return true if another book with the ISBN exists, false otherwise
     */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return an Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Searches for books by title containing the given keyword (case-insensitive).
     *
     * @param title the title keyword to search for
     * @return list of books matching the search criteria
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitle(@Param("title") String title);

    /**
     * Searches for books by author name containing the given keyword (case-insensitive).
     *
     * @param author the author keyword to search for
     * @return list of books matching the search criteria
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> searchByAuthor(@Param("author") String author);

    /**
     * Find books by title containing the given string (case-insensitive).
     *
     * @param title the title search term
     * @return list of books matching the title
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find books by author containing the given string (case-insensitive).
     *
     * @param author the author search term
     * @return list of books matching the author
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
