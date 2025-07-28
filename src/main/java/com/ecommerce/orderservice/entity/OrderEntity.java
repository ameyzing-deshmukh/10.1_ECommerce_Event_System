package com.ecommerce.orderservice.entity;

import com.ecommerce.orderservice.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
public class OrderEntity {

    @Id
    @GeneratedValue
    private Integer orderId;
    private String userId;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Integer> itemsCountMap;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderEntity(String userId, BigDecimal totalCost, LocalDateTime createdAt, Map<String, Integer> itemsCountMap, OrderStatus orderStatus) {
        this.userId = userId;
        this.totalCost = totalCost;
        this.createdAt = createdAt;
        this.itemsCountMap = itemsCountMap;
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId=" + orderId +
                ", userId='" + userId + '\'' +
                ", totalCost=" + totalCost +
                ", createdAt=" + createdAt +
                ", itemsCountMap=" + itemsCountMap +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
