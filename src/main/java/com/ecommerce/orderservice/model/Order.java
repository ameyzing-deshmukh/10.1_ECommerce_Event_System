package com.ecommerce.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Order {
    private int orderId;
    private String userId;
    private BigDecimal totalCost;
    private LocalDateTime createdAt;
    private Map<String, Integer> itemsCountMap;
}
