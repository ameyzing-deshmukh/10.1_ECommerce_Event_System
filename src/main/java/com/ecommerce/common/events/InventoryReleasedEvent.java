package com.ecommerce.common.events;

import lombok.Data;

@Data
public class InventoryReleasedEvent extends AbstractEvent {
    public InventoryReleasedEvent(Long orderId, String userId) {
        super(orderId, userId);
    }

    public InventoryReleasedEvent() {
    }
}
