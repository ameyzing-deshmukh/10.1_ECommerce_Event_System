package com.ecommerce.orderservice.producer;

import com.ecommerce.orderservice.entity.OutboxEventEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventProducer {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.order-placed}")
    private String orderPlacedTopic;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(OutboxEventEntity outboxEvent){
        log.info("Event is getting published to Kafka: {}",outboxEvent.getPayload());
        kafkaTemplate.send(orderPlacedTopic, outboxEvent.getPayload());
    }
}
