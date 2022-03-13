package com.readingisgood.app.domain.entity;

import com.readingisgood.app.domain.enums.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, insertable = false, updatable = false)
    private Long customerId;

    @CreatedDate
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ToString.Exclude
    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
}
