package com.ecommerce.shippingdeliveryservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShippingProducer {
    private KafkaTemplate<String, String> shippingTemplate;

    @Value("${topic.shipping-initiated}")
    private String shippingInitiatedTopic;

    public ShippingProducer(KafkaTemplate<String, String> shippingTemplate) {
        this.shippingTemplate = shippingTemplate;
    }

    public void publishShippingInitiatedEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", shippingInitiatedTopic);
        shippingTemplate.send(shippingInitiatedTopic, message);
    }
}
