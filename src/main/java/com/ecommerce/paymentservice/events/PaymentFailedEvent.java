package com.ecommerce.paymentservice.events;

import com.ecommerce.common.events.AbstractEvent;
import com.ecommerce.paymentservice.model.PaymentFailedReason;

public class PaymentFailedEvent extends AbstractEvent {
    private PaymentFailedReason reason;
    
    public PaymentFailedEvent(Long orderId, String userId, PaymentFailedReason reason) {
        super(orderId, userId);
        this.reason = reason;
    }

    public PaymentFailedEvent() {
        super();
    }
}
