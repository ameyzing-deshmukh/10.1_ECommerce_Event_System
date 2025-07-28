package com.ecommerce.inventoryservice.events;

import com.ecommerce.common.events.AbstractEvent;
import lombok.Data;

@Data
public class InventoryFailedEvent extends AbstractEvent {

    public InventoryFailedEvent() {
        super();
    }

    public InventoryFailedEvent(Long orderId, String userId) {
        super(orderId, userId);
    }
}
