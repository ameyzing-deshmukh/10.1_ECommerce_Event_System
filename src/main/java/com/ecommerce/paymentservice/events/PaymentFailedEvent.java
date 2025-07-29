package com.ecommerce.paymentservice.events;

import com.ecommerce.common.events.AbstractEvent;

public class PaymentFailedEvent extends AbstractEvent {
    private String paymentId;
}
