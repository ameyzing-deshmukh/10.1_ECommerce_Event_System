package com.ecommerce.shippingdeliveryservice.events;

import com.ecommerce.common.events.AbstractEvent;

import java.time.LocalDateTime;

public class ShippingInitiatedEvent extends AbstractEvent {
    private String shippingId;
    private String shippingPartnerName;
    private String shippingAddress;
    private LocalDateTime shippingDate;

    public ShippingInitiatedEvent(Long orderId, String userId, String shippingId, String shippingPartnerName, String shippingAddress, LocalDateTime shippingDate) {
        super(orderId, userId);
        this.shippingId = shippingId;
        this.shippingPartnerName = shippingPartnerName;
        this.shippingAddress = shippingAddress;
        this.shippingDate = shippingDate;
    }

    public ShippingInitiatedEvent() {
        super();
    }
}
