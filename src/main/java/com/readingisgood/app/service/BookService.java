package com.readingisgood.app.service;

import com.readingisgood.app.domain.dto.BookDto;
import com.readingisgood.app.domain.exception.StockNotEnoughException;
import com.readingisgood.app.domain.mapper.BookMapper;
import com.readingisgood.app.domain.request.UpdateBookStockRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository repository;

    private final BookMapper mapper;

    public BookDto create(BookDto book) {
        return mapper.map(repository.save(mapper.map(book)));
    }

    public PageableResponse<BookDto> list(Pageable pageable) {
        return PageableResponse.from(repository.findAll(pageable).map(mapper::map));
    }

    public void updateStock(Long id, UpdateBookStockRequest request) {
        repository.findById(id).ifPresent((book) -> {
            book.setQuantity(request.getQuantity());
            repository.save(book);
        });
    }

    public void decreaseStock(Long id, Long amount) {
        repository.findById(id).ifPresent((book) -> {
            if (book.getQuantity() < amount) {
                throw new StockNotEnoughException(book.getName() + "'s stock not enough");
            }
            book.setQuantity(book.getQuantity() - amount);
            repository.save(book);
        });
    }
}
