package com.readingisgood.app.repository;

import com.readingisgood.app.domain.entity.Order;
import com.readingisgood.app.domain.model.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);

    Page<Order> findAllByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT EXTRACT(YEAR FROM O.ORDER_DATE)  AS year,\n" +
            "       EXTRACT(MONTH FROM O.ORDER_DATE) AS month,\n" +
            "       COUNT(DISTINCT O.ID)               AS totalOrderCount,\n" +
            "       SUM(OI.QUANTITY)                   AS totalBookCount,\n" +
            "       SUM(OI.QUANTITY * B.UNIT_PRICE)    AS totalPurchasedAmount\n" +
            "from ORDERS O\n" +
            "         inner join ORDER_ITEMS OI on O.ID = OI.ORDER_ID\n" +
            "         INNER JOIN BOOKS B on OI.BOOK_ID = B.ID\n" +
            "WHERE CUSTOMER_ID = :customerId\n" +
            "GROUP BY year, month\n" +
            "ORDER BY year, month DESC", nativeQuery = true)
    List<Statistics> calculateStatistics(Long customerId);
}



