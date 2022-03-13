package com.readingisgood.app;

import com.readingisgood.app.domain.dto.BookDto;
import com.readingisgood.app.domain.dto.CustomerDto;
import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.dto.OrderItemDto;
import com.readingisgood.app.domain.entity.Book;
import com.readingisgood.app.domain.entity.Customer;
import com.readingisgood.app.domain.entity.Order;
import com.readingisgood.app.domain.entity.OrderItem;
import com.readingisgood.app.domain.enums.OrderStatus;
import com.readingisgood.app.domain.model.Statistics;
import com.readingisgood.app.domain.request.CreateOrderRequest;
import com.readingisgood.app.domain.request.UpdateBookStockRequest;
import com.readingisgood.app.domain.request.UpdateOrderStatusRequest;
import com.readingisgood.app.domain.response.StatisticsResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@UtilityClass
public class TestUtils {

    public static BookDto getBookDto() {
        return new BookDto(1L, "Book1", 10L, BigDecimal.TEN);
    }

    public static Book getBook() {
        return new Book(1L, 0L, "Book1", 10L, BigDecimal.TEN, null, null);
    }

    public static UpdateBookStockRequest getUpdateBookStockRequest() {
        return new UpdateBookStockRequest(2L);
    }

    public static CustomerDto getCustomerDto() {
        return new CustomerDto(1L, "Customer1");
    }

    public static Customer getCustomer() {
        return new Customer(1L, "Customer1");
    }

    public static CreateOrderRequest getCreateOrderRequest() {
        return new CreateOrderRequest(1L, List.of(getOrderItemDto()));
    }

    public static UpdateOrderStatusRequest getUpdateOrderStatusRequest() {
        return new UpdateOrderStatusRequest(OrderStatus.CANCELLED);
    }

    public static OrderDto getOrderDto() {
        return new OrderDto(1L, 1L, LocalDateTime.now(), OrderStatus.COMPLETED);
    }

    public static Order getOrder() {
        return new Order(1L, 1L, LocalDateTime.now(), OrderStatus.COMPLETED, getCustomer());
    }

    public static OrderItemDto getOrderItemDto() {
        return new OrderItemDto(1L, 1L);
    }

    public static OrderItem getOrderItem() {
        return new OrderItem(1L, 1L, 1L, 1L, getOrder(), getBook());
    }

    public static Statistics getStatistics() {
        return new Statistics() {
            @Override
            public Integer getYear() {
                return 2022;
            }

            @Override
            public Integer getMonth() {
                return 1;
            }

            @Override
            public Long getTotalOrderCount() {
                return 10L;
            }

            @Override
            public Long getTotalBookCount() {
                return 25L;
            }

            @Override
            public BigDecimal getTotalPurchasedAmount() {
                return new BigDecimal(750);
            }
        };
    }

    public static StatisticsResponse getStatisticsResponse() {
        Statistics s = getStatistics();
        return new StatisticsResponse(s.getYear(), Month.of(s.getMonth()).name(), s.getTotalOrderCount(), s.getTotalBookCount(), s.getTotalPurchasedAmount());
    }
}