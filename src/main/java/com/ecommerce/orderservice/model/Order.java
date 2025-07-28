package com.ecommerce.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class Order {
    private String userId;
    private BigDecimal totalCost;
    private Map<String, Integer> itemsCountMap;
}
