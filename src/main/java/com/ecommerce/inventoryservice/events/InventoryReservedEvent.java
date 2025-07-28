package com.ecommerce.inventoryservice.events;

import com.ecommerce.common.events.AbstractEvent;
import lombok.Data;

@Data
public class InventoryReservedEvent extends AbstractEvent {
    private Long orderId;
    private String userId;

    public InventoryReservedEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
