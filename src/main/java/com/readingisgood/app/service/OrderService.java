package com.readingisgood.app.service;

import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.dto.OrderItemDto;
import com.readingisgood.app.domain.entity.Order;
import com.readingisgood.app.domain.entity.OrderItem;
import com.readingisgood.app.domain.enums.OrderStatus;
import com.readingisgood.app.domain.mapper.OrderItemMapper;
import com.readingisgood.app.domain.mapper.OrderMapper;
import com.readingisgood.app.domain.request.CreateOrderRequest;
import com.readingisgood.app.domain.request.UpdateOrderStatusRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.domain.response.StatisticsResponse;
import com.readingisgood.app.repository.OrderItemRepository;
import com.readingisgood.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repository;

    private final OrderItemRepository itemRepository;

    private final BookService bookService;

    private final OrderMapper mapper;

    private final OrderItemMapper itemMapper;

    @Transactional(rollbackFor = Exception.class)
    public OrderDto create(CreateOrderRequest request) {
        Order order = repository.save(Order.builder().customerId(request.getCustomerId()).status(OrderStatus.COMPLETED).build());
        List<OrderItem> items = request.getItems().stream().map(item ->
                OrderItem.builder().orderId(order.getId()).bookId(item.getBookId()).quantity(item.getQuantity()).build()
        ).collect(Collectors.toList());
        itemRepository.saveAll(items);
        items.forEach((item) -> bookService.decreaseStock(item.getBookId(), item.getQuantity()));
        return mapper.map(order);
    }

    public PageableResponse<OrderDto> list(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return PageableResponse.from(repository.findAllByOrderDateBetween(startDate, endDate, pageable).map(mapper::map));
    }

    public void updateStatus(Long id, UpdateOrderStatusRequest request) {
        repository.findById(id).ifPresent((order) -> {
            order.setStatus(request.getStatus());
            repository.save(order);
        });
    }

    public OrderDto get(Long id) {
        return mapper.map(repository.findById(id).orElse(null));
    }

    public List<OrderItemDto> getItems(Long id) {
        return itemRepository.findAllByOrderId(id).stream().map(itemMapper::map).collect(Collectors.toList());
    }

    public PageableResponse<OrderDto> listByCustomerId(Long id, Pageable pageable) {
        return PageableResponse.from(repository.findAllByCustomerId(id, pageable).map(mapper::map));
    }

    public List<StatisticsResponse> calculateStatistics(Long customerId) {
        return repository.calculateStatistics(customerId).stream()
                .map(s -> new StatisticsResponse(s.getYear(), Month.of(s.getMonth()).name(), s.getTotalOrderCount(), s.getTotalBookCount(), s.getTotalPurchasedAmount()))
                .collect(Collectors.toList());
    }
}
