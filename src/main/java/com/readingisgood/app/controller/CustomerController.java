package com.readingisgood.app.controller;

import com.readingisgood.app.domain.dto.CustomerDto;
import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.service.CustomerService;
import com.readingisgood.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/customers")
@RestController
public class CustomerController {

    private final CustomerService service;

    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerDto create(@Valid @RequestBody CustomerDto customer) {
        return service.create(customer);
    }

    @GetMapping
    public PageableResponse<CustomerDto> list(@PageableDefault Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}/orders")
    public PageableResponse<OrderDto> listOrders(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return orderService.listByCustomerId(id, pageable);
    }
}
