package com.ecommerce.shippingdeliveryservice.consumer;

import com.ecommerce.shippingdeliveryservice.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShippingDeliveryConsumer {

    @Autowired
    private ShippingService shippingService;

    @KafkaListener(topics = "${topic.payment-confirmed}", groupId = "shipping-delivery-topics")
    public void consumePaymentConfirmationEvent(String paymentConfirmedEvent) {
        log.info("Payment Confirmation event is getting consumed. Message: {}", paymentConfirmedEvent);
        shippingService.processShippingEvent(paymentConfirmedEvent);
    }
}
