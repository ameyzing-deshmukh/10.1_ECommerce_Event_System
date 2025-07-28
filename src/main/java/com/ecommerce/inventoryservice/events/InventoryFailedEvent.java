package com.ecommerce.inventoryservice.events;

public class InventoryFailedEvent {
    private Long orderId;
    private String userId;

    public void InventoryFailedEvent(Long orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
