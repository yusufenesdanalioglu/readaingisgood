package com.readingisgood.app.domain.request;

import com.readingisgood.app.domain.dto.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<OrderItemDto> items;
}
