package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for book response data.
 *
 * This DTO represents the complete book information returned to clients,
 * including system-generated fields like ID and timestamps.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Book response containing complete book information")
public class BookResponseDto {

    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "Book title", example = "Clean Code")
    private String title;

    @Schema(description = "Book author", example = "Robert C. Martin")
    private String author;

    @Schema(description = "International Standard Book Number", example = "978-0132350884")
    private String isbn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Book publication date", example = "2008-08-01")
    private LocalDate publicationDate;

    @Schema(description = "Book price", example = "45.99")
    private BigDecimal price;

    @Schema(description = "Book description", example = "A handbook of agile software craftsmanship")
    private String description;

    @Schema(description = "Number of pages", example = "464")
    private Integer pages;

    @Schema(description = "Publisher name", example = "Prentice Hall")
    private String publisher;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the book was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the book was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
