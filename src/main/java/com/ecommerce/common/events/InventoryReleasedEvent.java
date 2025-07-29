package com.ecommerce.common.events;

import com.ecommerce.common.model.InventoryReleaseReason;
import lombok.Data;

@Data
public class InventoryReleasedEvent extends AbstractEvent {

    private InventoryReleaseReason reason;

    public InventoryReleasedEvent(Long orderId, String userId, InventoryReleaseReason reason) {
        super(orderId, userId);
        this.reason = reason;
    }

    public InventoryReleasedEvent() {
    }
}
