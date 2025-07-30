package com.ecommerce.shippingdeliveryservice.events;

import com.ecommerce.common.events.AbstractEvent;
import com.ecommerce.shippingdeliveryservice.model.ShippingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingCompletedEvent extends AbstractEvent {
    private String shippingId;
    private String shippingPartnerName;
    private String shippingAddress;
    private LocalDateTime shippingDate;
    private ShippingStatus status;

    public ShippingCompletedEvent(Long orderId, String userId, String shippingId, String shippingPartnerName, String shippingAddress, LocalDateTime shippingDate, ShippingStatus status) {
        super(orderId, userId);
        this.shippingId = shippingId;
        this.shippingPartnerName = shippingPartnerName;
        this.shippingAddress = shippingAddress;
        this.shippingDate = shippingDate;
        this.status = status;
    }

    public ShippingCompletedEvent() {
        super();
    }
}
