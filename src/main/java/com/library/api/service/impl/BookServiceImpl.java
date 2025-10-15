package com.library.api.service.impl;

import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;
import com.library.api.entity.Book;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.DuplicateIsbnException;
import com.library.api.mapper.BookMapper;
import com.library.api.repository.BookRepository;
import com.library.api.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the BookService interface.
 *
 * This service handles all business logic related to book management,
 * including validation, ISBN uniqueness checks, and coordination between
 * the repository and mapper layers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BookResponseDto createBook(BookRequestDto requestDto) {
        log.debug("Creating new book with ISBN: {}", requestDto.getIsbn());

        validateIsbnDoesNotExist(requestDto.getIsbn());

        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);

        log.info("Successfully created book with ID: {} and ISBN: {}",
                savedBook.getId(), savedBook.getIsbn());

        return bookMapper.toResponseDto(savedBook);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookResponseDto getBookById(Long id) {
        log.debug("Retrieving book with ID: {}", id);

        Book book = findBookByIdOrThrow(id);

        return bookMapper.toResponseDto(book);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponseDto> getAllBooks() {
        log.debug("Retrieving all books");

        List<Book> books = bookRepository.findAll();

        log.debug("Found {} books", books.size());

        return bookMapper.toResponseDtoList(books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto requestDto) {
        log.debug("Updating book with ID: {}", id);

        Book existingBook = findBookByIdOrThrow(id);

        // Check ISBN uniqueness if it's being changed
        if (!existingBook.getIsbn().equals(requestDto.getIsbn())) {
            validateIsbnDoesNotExistForUpdate(requestDto.getIsbn(), id);
        }

        bookMapper.updateEntityFromDto(requestDto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);

        log.info("Successfully updated book with ID: {}", id);

        return bookMapper.toResponseDto(updatedBook);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteBook(Long id) {
        log.debug("Deleting book with ID: {}", id);

        Book book = findBookByIdOrThrow(id);
        bookRepository.delete(book);

        log.info("Successfully deleted book with ID: {}", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponseDto> searchBooksByTitle(String title) {
        log.debug("Searching books by title: {}", title);

        List<Book> books = bookRepository.searchByTitle(title);

        log.debug("Found {} books matching title: {}", books.size(), title);

        return bookMapper.toResponseDtoList(books);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BookResponseDto> searchBooksByAuthor(String author) {
        log.debug("Searching books by author: {}", author);

        List<Book> books = bookRepository.searchByAuthor(author);

        log.debug("Found {} books matching author: {}", books.size(), author);

        return bookMapper.toResponseDtoList(books);
    }

    /**
     * Finds a book by ID or throws BookNotFoundException.
     *
     * @param id the book ID
     * @return the found book
     * @throws BookNotFoundException if book not found
     */
    private Book findBookByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.forId(id));
    }

    /**
     * Validates that no book exists with the given ISBN.
     *
     * @param isbn the ISBN to validate
     * @throws DuplicateIsbnException if a book with the ISBN already exists
     */
    private void validateIsbnDoesNotExist(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            log.warn("Attempt to create book with duplicate ISBN: {}", isbn);
            throw DuplicateIsbnException.forIsbn(isbn);
        }
    }

    /**
     * Validates that no other book exists with the given ISBN (for updates).
     *
     * @param isbn the ISBN to validate
     * @param currentBookId the ID of the book being updated
     * @throws DuplicateIsbnException if another book with the ISBN exists
     */
    private void validateIsbnDoesNotExistForUpdate(String isbn, Long currentBookId) {
        if (bookRepository.existsByIsbnAndIdNot(isbn, currentBookId)) {
            log.warn("Attempt to update book with duplicate ISBN: {}", isbn);
            throw DuplicateIsbnException.forIsbn(isbn);
        }
    }
}
