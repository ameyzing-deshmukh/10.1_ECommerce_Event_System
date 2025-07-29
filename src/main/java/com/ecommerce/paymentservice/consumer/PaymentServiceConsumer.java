package com.ecommerce.paymentservice.consumer;

import com.ecommerce.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentServiceConsumer {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = "${topic.inventory-reserved}", groupId = "inventory-topics")
    public void consumeInventoryReservedEvent(String inventoryReservedEvent) {
        log.info("Inventory Reserved event is getting consumed. Message: {}", inventoryReservedEvent);
        paymentService.validateAndInitiatePaymentProcess(inventoryReservedEvent);
    }
}
