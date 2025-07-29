package com.ecommerce.inventoryservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryProducer {

    private final KafkaTemplate<String, String> inventoryTemplate;

    @Value("${topic.inventory-reserved}")
    private String inventoryReservedTopic;

    @Value("${topic.inventory-failed}")
    private String inventoryFailedTopic;

    public InventoryProducer(KafkaTemplate<String, String> inventoryTemplate) {
        this.inventoryTemplate = inventoryTemplate;
    }

    public void publishToInventoryReserved(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", inventoryReservedTopic);
        inventoryTemplate.send(inventoryReservedTopic, message);
    }

    public void publishToInventoryFailed(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", inventoryFailedTopic);
        inventoryTemplate.send(inventoryFailedTopic, message);
    }

}
