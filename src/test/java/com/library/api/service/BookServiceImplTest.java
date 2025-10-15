package com.library.api.service;

import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;
import com.library.api.entity.Book;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.DuplicateIsbnException;
import com.library.api.mapper.BookMapper;
import com.library.api.repository.BookRepository;
import com.library.api.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookServiceImpl.
 *
 * These tests verify the business logic implementation,
 * including validation and error handling.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookServiceImpl Tests")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequestDto bookRequestDto;
    private BookResponseDto bookResponseDto;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publicationDate(LocalDate.of(2008, 8, 1))
                .price(new BigDecimal("45.99"))
                .description("A handbook of agile software craftsmanship")
                .pages(464)
                .publisher("Prentice Hall")
                .build();

        bookRequestDto = BookRequestDto.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publicationDate(LocalDate.of(2008, 8, 1))
                .price(new BigDecimal("45.99"))
                .description("A handbook of agile software craftsmanship")
                .pages(464)
                .publisher("Prentice Hall")
                .build();

        bookResponseDto = BookResponseDto.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("978-0132350884")
                .publicationDate(LocalDate.of(2008, 8, 1))
                .price(new BigDecimal("45.99"))
                .description("A handbook of agile software craftsmanship")
                .pages(464)
                .publisher("Prentice Hall")
                .build();
    }

    @Test
    @DisplayName("Should create book successfully")
    void shouldCreateBookSuccessfully() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookMapper.toEntity(any(BookRequestDto.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toResponseDto(any(Book.class))).thenReturn(bookResponseDto);

        BookResponseDto result = bookService.createBook(bookRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getIsbn()).isEqualTo("978-0132350884");

        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).toEntity(any(BookRequestDto.class));
        verify(bookMapper, times(1)).toResponseDto(any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when creating book with duplicate ISBN")
    void shouldThrowExceptionWhenCreatingBookWithDuplicateIsbn() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThatThrownBy(() -> bookService.createBook(bookRequestDto))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("already exists");

        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should get book by ID successfully")
    void shouldGetBookByIdSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponseDto(any(Book.class))).thenReturn(bookResponseDto);

        BookResponseDto result = bookService.getBookById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Clean Code");

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toResponseDto(any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when book not found by ID")
    void shouldThrowExceptionWhenBookNotFoundById() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(999L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("not found");

        verify(bookRepository, times(1)).findById(999L);
        verify(bookMapper, never()).toResponseDto(any(Book.class));
    }

    @Test
    @DisplayName("Should get all books successfully")
    void shouldGetAllBooksSuccessfully() {
        List<Book> books = Arrays.asList(book, book);
        List<BookResponseDto> responseDtos = Arrays.asList(bookResponseDto, bookResponseDto);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toResponseDtoList(anyList())).thenReturn(responseDtos);

        List<BookResponseDto> result = bookService.getAllBooks();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toResponseDtoList(anyList());
    }

    @Test
    @DisplayName("Should update book successfully")
    void shouldUpdateBookSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbnAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toResponseDto(any(Book.class))).thenReturn(bookResponseDto);
        doNothing().when(bookMapper).updateEntityFromDto(any(BookRequestDto.class), any(Book.class));

        BookResponseDto result = bookService.updateBook(1L, bookRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).existsByIsbnAndIdNot(anyString(), anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).updateEntityFromDto(any(BookRequestDto.class), any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with duplicate ISBN")
    void shouldThrowExceptionWhenUpdatingWithDuplicateIsbn() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbnAndIdNot(anyString(), anyLong())).thenReturn(true);

        assertThatThrownBy(() -> bookService.updateBook(1L, bookRequestDto))
                .isInstanceOf(DuplicateIsbnException.class)
                .hasMessageContaining("already exists");

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).existsByIsbnAndIdNot(anyString(), anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        assertThatCode(() -> bookService.deleteBook(1L))
                .doesNotThrowAnyException();

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing book")
    void shouldThrowExceptionWhenDeletingNonExistingBook() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(999L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("not found");

        verify(bookRepository, times(1)).existsById(999L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should search books by title successfully")
    void shouldSearchBooksByTitleSuccessfully() {
        List<Book> books = List.of(book);
        List<BookResponseDto> responseDtos = List.of(bookResponseDto);

        when(bookRepository.findByTitleContainingIgnoreCase("Clean")).thenReturn(books);
        when(bookMapper.toResponseDtoList(anyList())).thenReturn(responseDtos);

        List<BookResponseDto> result = bookService.searchBooksByTitle("Clean");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Clean");

        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Clean");
        verify(bookMapper, times(1)).toResponseDtoList(anyList());
    }

    @Test
    @DisplayName("Should search books by author successfully")
    void shouldSearchBooksByAuthorSuccessfully() {
        List<Book> books = List.of(book);
        List<BookResponseDto> responseDtos = List.of(bookResponseDto);

        when(bookRepository.findByAuthorContainingIgnoreCase("Martin")).thenReturn(books);
        when(bookMapper.toResponseDtoList(anyList())).thenReturn(responseDtos);

        List<BookResponseDto> result = bookService.searchBooksByAuthor("Martin");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).contains("Martin");

        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("Martin");
        verify(bookMapper, times(1)).toResponseDtoList(anyList());
    }

    @Test
    @DisplayName("Should return empty list when no books match title search")
    void shouldReturnEmptyListWhenNoBooksMatchTitleSearch() {
        when(bookRepository.findByTitleContainingIgnoreCase("NonExistent")).thenReturn(List.of());
        when(bookMapper.toResponseDtoList(anyList())).thenReturn(List.of());

        List<BookResponseDto> result = bookService.searchBooksByTitle("NonExistent");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("NonExistent");
    }

    @Test
    @DisplayName("Should return empty list when no books match author search")
    void shouldReturnEmptyListWhenNoBooksMatchAuthorSearch() {
        when(bookRepository.findByAuthorContainingIgnoreCase("NonExistent")).thenReturn(List.of());
        when(bookMapper.toResponseDtoList(anyList())).thenReturn(List.of());

        List<BookResponseDto> result = bookService.searchBooksByAuthor("NonExistent");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("NonExistent");
    }
}
