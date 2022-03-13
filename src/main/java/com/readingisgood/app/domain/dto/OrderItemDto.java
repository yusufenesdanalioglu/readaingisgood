package com.readingisgood.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {

    @NotNull
    private Long bookId;

    @Min(1)
    @NotNull
    private Long quantity;
}
