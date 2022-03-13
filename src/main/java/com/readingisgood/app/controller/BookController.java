package com.readingisgood.app.controller;

import com.readingisgood.app.domain.dto.BookDto;
import com.readingisgood.app.domain.request.UpdateBookStockRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookController {

    private final BookService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto create(@Valid @RequestBody BookDto book) {
        return service.create(book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}")
    public void updateStock(@PathVariable Long id, @Valid @RequestBody UpdateBookStockRequest request) {
        service.updateStock(id, request);
    }

    @GetMapping
    public PageableResponse<BookDto> list(@PageableDefault Pageable pageable) {
        return service.list(pageable);
    }
}
