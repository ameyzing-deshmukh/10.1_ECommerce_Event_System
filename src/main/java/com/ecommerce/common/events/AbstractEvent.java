package com.ecommerce.common.events;

import lombok.Data;

@Data
public abstract class AbstractEvent {
    private Long orderId;
    private String userId;

    public AbstractEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public AbstractEvent() {

    }
}
