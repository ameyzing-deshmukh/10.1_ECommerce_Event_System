package com.ecommerce.inventoryservice.events;

import lombok.Data;

@Data
public class InventoryFailedEvent {
    private Long orderId;
    private String userId;

    public InventoryFailedEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
