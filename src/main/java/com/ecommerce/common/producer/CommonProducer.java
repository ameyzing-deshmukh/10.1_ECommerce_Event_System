package com.ecommerce.common.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonProducer {

    private final KafkaTemplate<String, String> commonKafkaTemplate;

    @Value("${topic.order-cancelled}")
    private String orderCancelledTopic;

    @Value("${topic.inventory-released}")
    private String inventoryReleasedTopic;

    public CommonProducer(KafkaTemplate<String, String> commonKafkaTemplate) {
        this.commonKafkaTemplate = commonKafkaTemplate;
    }

    public void publishOrderCancelledEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", orderCancelledTopic);
        commonKafkaTemplate.send(orderCancelledTopic, message);
    }

    public void publishInventoryReleasedEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", inventoryReleasedTopic);
        commonKafkaTemplate.send(inventoryReleasedTopic, message);
    }
}
