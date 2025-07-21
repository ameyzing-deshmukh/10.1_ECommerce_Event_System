package com.ecommerce.inventoryservice.listener;

import com.ecommerce.orderservice.events.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPlacedListener {

    @KafkaListener(topics = "${topic.order-placed}", groupId = "inventory-topics")
    public void listToOrderPlacedEvent(String orderPlacedEvent) {
        log.info("Order created event is getting listened");
        log.info(orderPlacedEvent);
    }
}
