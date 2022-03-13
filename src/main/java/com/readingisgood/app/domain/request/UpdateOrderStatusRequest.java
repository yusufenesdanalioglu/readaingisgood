package com.readingisgood.app.domain.request;

import com.readingisgood.app.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status;
}
