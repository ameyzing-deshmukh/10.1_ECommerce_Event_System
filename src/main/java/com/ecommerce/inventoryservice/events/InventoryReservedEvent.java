package com.ecommerce.inventoryservice.events;

import lombok.Data;

@Data
public class InventoryReservedEvent {
    private Long orderId;
    private String userId;

    public InventoryReservedEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
