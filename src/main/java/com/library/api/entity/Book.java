package com.library.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a book in the library system.
 *
 * This entity uses JPA annotations for persistence and includes
 * audit fields for tracking creation and update timestamps.
 */
@Entity
@Table(
        name = "books",
        indexes = {
                @Index(name = "idx_isbn", columnList = "isbn", unique = true),
                @Index(name = "idx_title", columnList = "title")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    @Column(nullable = false, length = 255)
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(
            regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "Invalid ISBN format"
    )
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @NotNull(message = "Publication date cannot be null")
    @PastOrPresent(message = "Publication date cannot be in the future")
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @Min(value = 0, message = "Number of pages cannot be negative")
    @Column(name = "pages")
    private Integer pages;

    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    @Column(length = 100)
    private String publisher;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
