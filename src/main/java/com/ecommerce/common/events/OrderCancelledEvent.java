package com.ecommerce.common.events;

import lombok.Data;

@Data
public class OrderCancelledEvent extends AbstractEvent {
    public OrderCancelledEvent(Long orderId, String userId) {
        super(orderId, userId);
    }

    public OrderCancelledEvent() {
    }
}
