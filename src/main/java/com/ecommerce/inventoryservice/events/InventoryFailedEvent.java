package com.ecommerce.inventoryservice.events;

import com.ecommerce.common.events.AbstractEvent;
import lombok.Data;

@Data
public class InventoryFailedEvent extends AbstractEvent {
    private Long orderId;
    private String userId;

    public InventoryFailedEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
