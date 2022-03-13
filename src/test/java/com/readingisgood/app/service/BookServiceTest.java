package com.readingisgood.app.service;

import com.readingisgood.app.TestUtils;
import com.readingisgood.app.domain.dto.BookDto;
import com.readingisgood.app.domain.entity.Book;
import com.readingisgood.app.domain.exception.StockNotEnoughException;
import com.readingisgood.app.domain.mapper.BookMapper;
import com.readingisgood.app.domain.request.UpdateBookStockRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    private BookService service;

    @Captor
    private ArgumentCaptor<Book> captor;

    @BeforeEach
    public void init() {
        service = new BookService(repository, Mappers.getMapper(BookMapper.class));
    }

    @Test
    public void create_WhenInputGiven_ThenShouldReturnSavedBook() {
        //Given
        Book book = TestUtils.getBook();
        BookDto bookDto = TestUtils.getBookDto();
        Mockito.when(repository.save(Mockito.any(Book.class))).thenReturn(book);
        //When
        BookDto savedBook = service.create(bookDto);
        //Then
        Mockito.verify(repository).save(Mockito.any());
        Assertions.assertThat(savedBook).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    public void list_WhenInputGiven_ThenShouldReturnListOfBooks() {
        //Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        Page<Book> page = new PageImpl(List.of(TestUtils.getBook()), pageable, 1);
        PageableResponse<Book> expected = PageableResponse.from(page);
        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        //When
        PageableResponse<BookDto> books = service.list(pageable);
        //Then
        Mockito.verify(repository).findAll(Mockito.any(Pageable.class));
        Assertions.assertThat(books).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateStock_WhenInputGivenAndBookFound_ThenShouldUpdateStock() {
        //Given
        Book book = TestUtils.getBook();
        UpdateBookStockRequest updateBookStockRequest = TestUtils.getUpdateBookStockRequest();
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(repository.save(Mockito.any(Book.class))).thenReturn(book);
        //When
        service.updateStock(book.getId(), updateBookStockRequest);
        //Then
        Mockito.verify(repository).findById(book.getId());
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    public void updateStock_WhenInputGivenAndBookNotFound_ThenShouldNothing() {
        //Given
        Book book = TestUtils.getBook();
        UpdateBookStockRequest updateBookStockRequest = TestUtils.getUpdateBookStockRequest();
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.empty());
        //When
        service.updateStock(book.getId(), updateBookStockRequest);
        //Then
        Mockito.verify(repository).findById(book.getId());
    }

    @Test
    public void decreaseStock_WhenInputGivenAndBookFound_ThenShouldDecreaseStock() {
        //Given
        Book book = TestUtils.getBook();
        Long quantity = book.getQuantity();
        Long amount = quantity - 1;
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(repository.save(Mockito.any(Book.class))).thenReturn(book);
        //When
        service.decreaseStock(book.getId(), amount);
        //Then
        Mockito.verify(repository).findById(book.getId());
        Mockito.verify(repository).save(captor.capture());
        Book capturedBook = captor.getValue();
        Assertions.assertThat(capturedBook.getQuantity()).isEqualTo(quantity - amount);
    }

    @Test
    public void decreaseStock_WhenInputGivenAndBookFound_ThenShouldThrowException() {
        //Given
        Book book = TestUtils.getBook();
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.of(book));
        //Then
        Assertions.assertThatThrownBy(() -> {
            //When
            service.decreaseStock(book.getId(), book.getQuantity() + 10);
        }).isInstanceOf(StockNotEnoughException.class);
    }

    @Test
    public void decreaseStock_WhenInputGivenAndBookNotFound_ThenShouldNothing() {
        //Given
        Book book = TestUtils.getBook();
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.empty());
        //When
        service.decreaseStock(book.getId(), 1L);
        //Then
        Mockito.verify(repository).findById(book.getId());
    }
}
