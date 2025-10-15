package com.library.api.mapper;

import com.library.api.dto.BookRequestDto;
import com.library.api.dto.BookResponseDto;
import com.library.api.entity.Book;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper interface for converting between Book entities and DTOs.
 *
 * This mapper provides bidirectional mapping capabilities while maintaining
 * clean separation between persistence and presentation layers.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookMapper {

    /**
     * Converts a Book entity to a BookResponseDto.
     *
     * @param book the Book entity to convert
     * @return the corresponding BookResponseDto
     */
    BookResponseDto toResponseDto(Book book);

    /**
     * Converts a list of Book entities to a list of BookResponseDto.
     *
     * @param books the list of Book entities
     * @return the corresponding list of BookResponseDto
     */
    List<BookResponseDto> toResponseDtoList(List<Book> books);

    /**
     * Converts a BookRequestDto to a Book entity.
     *
     * @param requestDto the BookRequestDto to convert
     * @return the corresponding Book entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Book toEntity(BookRequestDto requestDto);

    /**
     * Updates an existing Book entity with data from a BookRequestDto.
     * Only non-null values from the DTO will be applied to the entity.
     *
     * @param requestDto the source DTO containing update data
     * @param book the target Book entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(BookRequestDto requestDto, @MappingTarget Book book);
}
