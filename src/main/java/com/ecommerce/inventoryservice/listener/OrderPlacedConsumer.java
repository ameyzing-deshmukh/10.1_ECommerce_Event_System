package com.ecommerce.inventoryservice.listener;

import com.ecommerce.inventoryservice.service.InventoryService;
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
        log.info("Order created event is getting consumed. Message: {}", orderPlacedEvent);
        inventoryService.checkInventory(orderPlacedEvent);
    }

    @KafkaListener(topics = "${topic.inventory-reserved}", groupId = "inventory-topics")
    public void consumeInventoryReservedEvent(String inventoryReservedEvent) {
        log.info("Inventory Reserved event is getting consumed. Message: {}", inventoryReservedEvent);
        inventoryService.updateInventory(inventoryReservedEvent);
    }
}
