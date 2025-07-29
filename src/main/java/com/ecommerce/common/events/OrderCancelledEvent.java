package com.ecommerce.common.events;

import com.ecommerce.common.model.OrderCancellationReason;
import lombok.Data;

@Data
public class OrderCancelledEvent extends AbstractEvent {

    private OrderCancellationReason reason;

    public OrderCancelledEvent(Long orderId, String userId, OrderCancellationReason reason) {
        super(orderId, userId);
        this.reason = reason;
    }

    public OrderCancelledEvent() {
    }
}
