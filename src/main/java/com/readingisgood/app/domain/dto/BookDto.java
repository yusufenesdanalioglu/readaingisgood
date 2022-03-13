package com.readingisgood.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {

    private Long id;

    @Size(min = 1, max = 100)
    @NotNull
    private String name;

    @Min(0)
    private Long quantity;

    @Min(0)
    private BigDecimal unitPrice;
}
