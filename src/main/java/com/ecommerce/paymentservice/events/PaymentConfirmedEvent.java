package com.ecommerce.paymentservice.events;

import com.ecommerce.common.events.AbstractEvent;
import lombok.Data;

@Data
public class PaymentConfirmedEvent extends AbstractEvent {
    private String paymentId;

    public PaymentConfirmedEvent(Long orderId, String userId, String paymentId) {
        super(orderId, userId);
        this.paymentId = paymentId;
    }

    public PaymentConfirmedEvent() {
        super();
    }
}
