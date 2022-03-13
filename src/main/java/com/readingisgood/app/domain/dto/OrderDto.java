package com.readingisgood.app.domain.dto;

import com.readingisgood.app.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private Long id;

    @NotNull
    private Long customerId;

    private LocalDateTime orderDate;

    private OrderStatus status;
}
