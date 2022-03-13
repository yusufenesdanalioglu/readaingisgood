package com.readingisgood.app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsResponse {

    private Integer year;

    private String month;

    private Long totalOrderCount;

    private Long totalBookCount;

    private BigDecimal totalPurchasedAmount;
}
