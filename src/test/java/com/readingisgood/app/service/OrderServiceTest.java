package com.readingisgood.app.service;

import com.readingisgood.app.TestUtils;
import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.dto.OrderItemDto;
import com.readingisgood.app.domain.entity.Order;
import com.readingisgood.app.domain.entity.OrderItem;
import com.readingisgood.app.domain.mapper.OrderItemMapper;
import com.readingisgood.app.domain.mapper.OrderMapper;
import com.readingisgood.app.domain.model.Statistics;
import com.readingisgood.app.domain.request.CreateOrderRequest;
import com.readingisgood.app.domain.request.UpdateOrderStatusRequest;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.domain.response.StatisticsResponse;
import com.readingisgood.app.repository.OrderItemRepository;
import com.readingisgood.app.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderItemRepository itemRepository;

    @Mock
    private BookService bookService;

    private OrderService service;

    @BeforeEach
    public void init() {
        service = new OrderService(repository, itemRepository, bookService, Mappers.getMapper(OrderMapper.class), Mappers.getMapper(OrderItemMapper.class));
    }

    @Test
    public void create_WhenInputGiven_ThenShouldReturnSavedOrder() {
        //Given
        Order order = TestUtils.getOrder();
        CreateOrderRequest request = TestUtils.getCreateOrderRequest();
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(order);
        //When
        OrderDto savedOrder = service.create(request);
        //Then
        Mockito.verify(repository).save(Mockito.any());
        Mockito.verify(itemRepository).saveAll(Mockito.any());
        Mockito.verify(bookService).decreaseStock(Mockito.any(), Mockito.any());
        Assertions.assertThat(savedOrder).usingRecursiveComparison().isEqualTo(order);
    }

    @Test
    public void list_WhenInputGiven_ThenShouldReturnListOfOrders() {
        //Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        Page<Order> page = new PageImpl(List.of(TestUtils.getOrder()), pageable, 1);
        PageableResponse<Order> expected = PageableResponse.from(page);
        Mockito.when(repository.findAllByOrderDateBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class))).thenReturn(page);
        //When
        PageableResponse<OrderDto> orders = service.list(LocalDateTime.now(), LocalDateTime.now(), pageable);
        //Then
        Mockito.verify(repository).findAllByOrderDateBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        Assertions.assertThat(orders).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateStatus_WhenInputGivenAndOrderFound_ThenShouldUpdateStatus() {
        //Given
        Order order = TestUtils.getOrder();
        UpdateOrderStatusRequest updateOrderStatusRequest = TestUtils.getUpdateOrderStatusRequest();
        Mockito.when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(order);
        //When
        service.updateStatus(order.getId(), updateOrderStatusRequest);
        //Then
        Mockito.verify(repository).findById(order.getId());
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    public void updateStatus_WhenInputGivenAndOrderNotFound_ThenShouldNothing() {
        //Given
        Order order = TestUtils.getOrder();
        UpdateOrderStatusRequest updateOrderStatusRequest = TestUtils.getUpdateOrderStatusRequest();
        Mockito.when(repository.findById(order.getId())).thenReturn(Optional.empty());
        //When
        service.updateStatus(order.getId(), updateOrderStatusRequest);
        //Then
        Mockito.verify(repository).findById(order.getId());
    }

    @Test
    public void get_WhenInputGivenAndOrderFound_ThenShouldReturnOrder() {
        //Given
        Order expected = TestUtils.getOrder();
        Mockito.when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
        //When
        OrderDto actual = service.get(expected.getId());
        //Then
        Mockito.verify(repository).findById(expected.getId());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getItems_WhenInputGivenAndOrderFound_ThenShouldReturnOrder() {
        //Given
        Order order = TestUtils.getOrder();
        List<OrderItem> expected = List.of(TestUtils.getOrderItem());
        Mockito.when(itemRepository.findAllByOrderId(order.getId())).thenReturn(expected);
        //When
        List<OrderItemDto> actual = service.getItems(order.getId());
        //Then
        Mockito.verify(itemRepository).findAllByOrderId(order.getId());
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void listByCustomerId_WhenInputGiven_ThenShouldReturnListOfOrders() {
        //Given
        long customerId = 1L;
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        Page<Order> page = new PageImpl(List.of(TestUtils.getOrder()), pageable, 1);
        PageableResponse<Order> expected = PageableResponse.from(page);
        Mockito.when(repository.findAllByCustomerId(Mockito.any(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
        //When
        PageableResponse<OrderDto> orders = service.listByCustomerId(customerId, pageable);
        //Then
        Mockito.verify(repository).findAllByCustomerId(Mockito.any(Long.class), Mockito.any(Pageable.class));
        Assertions.assertThat(orders).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void calculateStatistics_WhenInputGiven_ThenShouldReturnListOfOrders() {
        //Given
        Long customerId = 1L;
        List<Statistics> statistics = List.of(TestUtils.getStatistics());
        List<StatisticsResponse> expected = List.of(TestUtils.getStatisticsResponse());
        Mockito.when(repository.calculateStatistics(customerId)).thenReturn(statistics);
        //When
        List<StatisticsResponse> actual = service.calculateStatistics(customerId);
        //Then
        Mockito.verify(repository).calculateStatistics(Mockito.any(Long.class));
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
