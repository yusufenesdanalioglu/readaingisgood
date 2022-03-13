package com.readingisgood.app.domain.model;

import java.math.BigDecimal;

public interface Statistics {

    Integer getYear();

    Integer getMonth();

    Long getTotalOrderCount();

    Long getTotalBookCount();

    BigDecimal getTotalPurchasedAmount();
}
