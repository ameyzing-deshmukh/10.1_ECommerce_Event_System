package com.ecommerce.orderservice.entity;

import com.ecommerce.orderservice.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class OrderEntity {

    @Id
    @GeneratedValue
    private Integer orderId;
    private String userId;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderEntity(String userId, BigDecimal totalCost, LocalDateTime createdAt, OrderStatus orderStatus) {
        this.userId = userId;
        this.totalCost = totalCost;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }
}
