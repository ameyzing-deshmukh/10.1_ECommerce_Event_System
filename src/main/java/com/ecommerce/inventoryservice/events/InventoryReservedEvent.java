package com.ecommerce.inventoryservice.events;

import com.ecommerce.common.events.AbstractEvent;
import lombok.Data;

@Data
public class InventoryReservedEvent extends AbstractEvent {

    public InventoryReservedEvent() {
        super();
    }

    public InventoryReservedEvent(Long orderId, String userId) {
        super(orderId, userId);
    }
}
