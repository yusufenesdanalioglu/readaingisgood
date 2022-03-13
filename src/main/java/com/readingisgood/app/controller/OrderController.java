package com.readingisgood.app.controller;

import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.dto.OrderItemDto;
import com.readingisgood.app.domain.request.CreateOrderRequest;
import com.readingisgood.app.domain.request.UpdateOrderStatusRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto create(@RequestBody @Valid CreateOrderRequest request) {
        return service.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}")
    public void updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        service.updateStatus(id, request);
    }

    @GetMapping
    public PageableResponse<OrderDto> list(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime endDate, @PageableDefault Pageable pageable) {
        return service.list(startDate, endDate, pageable);
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{id}/items")
    public List<OrderItemDto> getItems(@PathVariable Long id) {
        return service.getItems(id);
    }
}
