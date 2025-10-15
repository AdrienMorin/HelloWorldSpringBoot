package com.library.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.DuplicateIsbnException;
import com.library.api.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for BookController.
 *
 * These tests verify the REST API endpoints' behavior, including
 * successful scenarios and error handling.
 */
@WebMvcTest(BookController.class)
@DisplayName("BookController Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookRequestDto validBookRequest;
    private BookResponseDto bookResponse;

    @BeforeEach
    void setUp() {
        validBookRequest = BookRequestDto.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publicationDate(LocalDate.of(2008, 8, 1))
                .price(new BigDecimal("45.99"))
                .description("A handbook of agile software craftsmanship")
                .pages(464)
                .publisher("Prentice Hall")
                .build();

        bookResponse = BookResponseDto.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publicationDate(LocalDate.of(2008, 8, 1))
                .price(new BigDecimal("45.99"))
                .description("A handbook of agile software craftsmanship")
                .pages(464)
                .publisher("Prentice Hall")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create book successfully")
    void shouldCreateBookSuccessfully() throws Exception {
        when(bookService.createBook(any(BookRequestDto.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Clean Code")))
                .andExpect(jsonPath("$.isbn", is("978-0132350884")));

        verify(bookService, times(1)).createBook(any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating book with invalid data")
    void shouldReturn400WhenCreatingBookWithInvalidData() throws Exception {
        BookRequestDto invalidRequest = BookRequestDto.builder()
                .title("")
                .author("")
                .isbn("invalid-isbn")
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", hasSize(greaterThan(0))));

        verify(bookService, never()).createBook(any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating book with duplicate ISBN")
    void shouldReturn400WhenCreatingBookWithDuplicateIsbn() throws Exception {
        when(bookService.createBook(any(BookRequestDto.class)))
                .thenThrow(DuplicateIsbnException.forIsbn("978-0132350884"));

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    @DisplayName("Should get book by ID successfully")
    void shouldGetBookByIdSuccessfully() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(bookResponse);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Clean Code")));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    @DisplayName("Should return 404 when book not found")
    void shouldReturn404WhenBookNotFound() throws Exception {
        when(bookService.getBookById(999L))
                .thenThrow(BookNotFoundException.forId(999L));

        mockMvc.perform(get("/api/v1/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    @DisplayName("Should get all books successfully")
    void shouldGetAllBooksSuccessfully() throws Exception {
        List<BookResponseDto> books = Arrays.asList(bookResponse, bookResponse);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    @DisplayName("Should update book successfully")
    void shouldUpdateBookSuccessfully() throws Exception {
        when(bookService.updateBook(eq(1L), any(BookRequestDto.class)))
                .thenReturn(bookResponse);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(bookService, times(1)).updateBook(eq(1L), any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    @DisplayName("Should search books by title successfully")
    void shouldSearchBooksByTitleSuccessfully() throws Exception {
        List<BookResponseDto> books = List.of(bookResponse);
        when(bookService.searchBooksByTitle("Clean")).thenReturn(books);

        mockMvc.perform(get("/api/v1/books/search/title")
                        .param("title", "Clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(bookService, times(1)).searchBooksByTitle("Clean");
    }

    @Test
    @DisplayName("Should search books by author successfully")
    void shouldSearchBooksByAuthorSuccessfully() throws Exception {
        List<BookResponseDto> books = List.of(bookResponse);
        when(bookService.searchBooksByAuthor("Martin")).thenReturn(books);

        mockMvc.perform(get("/api/v1/books/search/author")
                        .param("author", "Martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(bookService, times(1)).searchBooksByAuthor("Martin");
    }
}
