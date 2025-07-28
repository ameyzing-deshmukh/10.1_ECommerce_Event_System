package com.ecommerce.orderservice.events;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class OrderPlacedEvent {
    private int orderId;
    private String userId;
    private BigDecimal totalCost;
    private Date createdAt;
    private String orderStatus;
    private Map<String, Integer> itemsCountMap;
}
