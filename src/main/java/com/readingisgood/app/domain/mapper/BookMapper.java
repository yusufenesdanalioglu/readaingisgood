package com.readingisgood.app.domain.mapper;

import com.readingisgood.app.domain.dto.BookDto;
import com.readingisgood.app.domain.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto map(Book book);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Book map(BookDto book);
}
