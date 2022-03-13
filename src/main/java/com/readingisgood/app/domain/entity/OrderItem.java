package com.readingisgood.app.domain.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Long orderId;

    @Column(name = "book_id", nullable = false, insertable = false, updatable = false)
    private Long bookId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ToString.Exclude
    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ToString.Exclude
    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
}
