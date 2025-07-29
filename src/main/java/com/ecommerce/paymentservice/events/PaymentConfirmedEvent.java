package com.ecommerce.paymentservice.events;

import com.ecommerce.common.events.AbstractEvent;

public class PaymentConfirmedEvent extends AbstractEvent {
    private String paymentId;
}
