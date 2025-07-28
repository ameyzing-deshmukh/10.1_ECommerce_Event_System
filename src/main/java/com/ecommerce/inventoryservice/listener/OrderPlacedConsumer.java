package com.ecommerce.inventoryservice.listener;

import com.ecommerce.inventoryservice.services.InventoryService;
import com.ecommerce.orderservice.events.OrderPlacedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPlacedConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "${topic.order-placed}", groupId = "inventory-topics")
    public void consumeOrderPlacedEvent(String orderPlacedEvent) {
        log.info("Order created event is getting consumed.");
        log.info(orderPlacedEvent);
        try {
            inventoryService.checkInventory(orderPlacedEvent);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
//            throw new RuntimeException(e);
        }
    }
}
