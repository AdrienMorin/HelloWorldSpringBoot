package com.library.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for book creation and update requests.
 *
 * This DTO encapsulates the data required to create or update a book,
 * with appropriate validation constraints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Book request payload for create and update operations")
public class BookRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(description = "Book title", example = "Clean Code", required = true)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    @Schema(description = "Book author", example = "Robert C. Martin", required = true)
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(
            regexp = "^(978|979)-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1}$",
            message = "ISBN must be in format: 978-X-XXXX-XXXX-X or 979-X-XXXX-XXXX-X (e.g., 978-0-13235-088-4)"
    )
    @Schema(description = "International Standard Book Number", example = "978-0132350884", required = true)
    private String isbn;

    @NotNull(message = "Publication date is required")
    @PastOrPresent(message = "Publication date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Book publication date", example = "2008-08-01", required = true)
    private LocalDate publicationDate;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    @Schema(description = "Book price", example = "45.99", required = true)
    private BigDecimal price;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Book description", example = "A handbook of agile software craftsmanship")
    private String description;

    @Min(value = 1, message = "Number of pages must be at least 1")
    @Schema(description = "Number of pages", example = "464")
    private Integer pages;

    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    @Schema(description = "Publisher name", example = "Prentice Hall")
    private String publisher;
}
