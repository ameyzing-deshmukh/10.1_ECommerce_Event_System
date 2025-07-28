package com.ecommerce.orderservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderPlacedEventProducer {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.order-placed}")
    private String orderPlacedTopic;

    public OrderPlacedEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", orderPlacedTopic);
        kafkaTemplate.send(orderPlacedTopic, message);
    }
}
