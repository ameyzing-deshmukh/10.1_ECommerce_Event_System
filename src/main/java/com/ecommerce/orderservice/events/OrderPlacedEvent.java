package com.ecommerce.orderservice.events;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPlacedEvent {
    private int orderId;
    private int userId;
    private BigDecimal totalCost;
    private Date createdAt;
}
