package com.library.api.controller;

import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;
import com.library.api.dto.ErrorResponseDto;
import com.library.api.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for book management operations.
 *
 * This controller exposes RESTful endpoints for CRUD operations on books,
 * following REST best practices and providing comprehensive API documentation.
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books", description = "Book management API")
public class BookController {

    private final BookService bookService;

    /**
     * Creates a new book in the library.
     *
     * @param requestDto the book data
     * @return the created book with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new book", description = "Adds a new book to the library system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book successfully created",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or duplicate ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<BookResponseDto> createBook(
            @Valid @RequestBody BookRequestDto requestDto) {

        log.info("Received request to create book with ISBN: {}", requestDto.getIsbn());

        BookResponseDto response = bookService.createBook(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return the book details with HTTP 200 status
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves detailed information about a specific book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<BookResponseDto> getBookById(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id) {

        log.info("Received request to get book with ID: {}", id);

        BookResponseDto response = bookService.getBookById(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all books in the library.
     *
     * @return list of all books with HTTP 200 status
     */
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the library")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            )
    })
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        log.info("Received request to get all books");

        List<BookResponseDto> response = bookService.getAllBooks();

        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing book.
     *
     * @param id the book ID
     * @param requestDto the updated book data
     * @return the updated book with HTTP 200 status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book's information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book successfully updated",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or duplicate ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<BookResponseDto> updateBook(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto requestDto) {

        log.info("Received request to update book with ID: {}", id);

        BookResponseDto response = bookService.updateBook(id, requestDto);

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a book from the library.
     *
     * @param id the book ID
     * @return HTTP 204 status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Removes a book from the library system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Book successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id) {

        log.info("Received request to delete book with ID: {}", id);

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Searches books by title keyword.
     *
     * @param title the title search keyword
     * @return list of matching books with HTTP 200 status
     */
    @GetMapping("/search/title")
    @Operation(summary = "Search books by title", description = "Searches for books containing the specified title keyword")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            )
    })
    public ResponseEntity<List<BookResponseDto>> searchBooksByTitle(
            @Parameter(description = "Title search keyword", required = true)
            @RequestParam String title) {

        log.info("Received request to search books by title: {}", title);

        List<BookResponseDto> response = bookService.searchBooksByTitle(title);

        return ResponseEntity.ok(response);
    }

    /**
     * Searches books by author keyword.
     *
     * @param author the author search keyword
     * @return list of matching books with HTTP 200 status
     */
    @GetMapping("/search/author")
    @Operation(summary = "Search books by author", description = "Searches for books by the specified author keyword")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))
            )
    })
    public ResponseEntity<List<BookResponseDto>> searchBooksByAuthor(
            @Parameter(description = "Author search keyword", required = true)
            @RequestParam String author) {

        log.info("Received request to search books by author: {}", author);

        List<BookResponseDto> response = bookService.searchBooksByAuthor(author);

        return ResponseEntity.ok(response);
    }
}
