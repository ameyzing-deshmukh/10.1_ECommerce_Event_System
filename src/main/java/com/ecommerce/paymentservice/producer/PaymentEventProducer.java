package com.ecommerce.paymentservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> paymentKafkaTemplate;

    @Value("${topic.payment-confirmed}")
    private String paymentConfirmedTopic;

    @Value("${topic.payment-failed}")
    private String paymentFailedTopic;

    public PaymentEventProducer(KafkaTemplate<String, String> paymentKafkaTemplate) {
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }

    public void publishPaymentConfirmedEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", paymentConfirmedTopic);
        paymentKafkaTemplate.send(paymentConfirmedTopic, message);
    }

    public void publishPaymentFailedEvent(String message) {
        log.info("Message: " + message + " is getting published to Kafka topic: {}", paymentFailedTopic);
        paymentKafkaTemplate.send(paymentFailedTopic, message);
    }

}
